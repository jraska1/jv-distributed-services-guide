package cz.dsw.distrib_services_guide.route;

import cz.dsw.distrib_services_guide.component.ApplicationStateComponent;
import cz.dsw.distrib_services_guide.entity.Request;
import cz.dsw.distrib_services_guide.entity.Response;
import cz.dsw.distrib_services_guide.entity.ResponseCodeType;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile(value = "applicant")
public class ApplicantCamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ApplicantCamelRoutes.class);

    public static final String RECIPIENT_LIST = "Recipients";

    @Value("${routes.applicants.checkerInterval:100}")
    private long checkerInterval;

    @Autowired(required = false)
    private ApplicationStateComponent applicationState;

    @Override
    public void configure() throws Exception {

        from("direct:applicant").routeId("applicant")
            .onException(ExchangeTimedOutException.class)
                .continued(true)
                .process(exchange -> exchange.getMessage().setBody(null))
            .end()
            .process(exchange -> {
                List<String> recipients = exchange.getMessage().getHeader(RECIPIENT_LIST, List.class);
                exchange.setProperty("Request", exchange.getMessage().getBody(Request.class));
                exchange.setProperty(RECIPIENT_LIST, recipients);
                exchange.getMessage().setHeader("RecipientsURL", recipients.stream()
                        .map(s -> "activemq:queue:" + s + "?explicitQosEnabled=true&preserveMessageQos=true&requestTimeoutCheckerInterval=" + checkerInterval)
                        .collect(Collectors.toList()));
            })
            .recipientList()
                .header("RecipientsURL")
                .parallelProcessing()
                .streaming()
                .aggregationStrategy((oldExchange, newExchange) -> {
                    Exchange result;
                    List<Response> list;
                    if (oldExchange != null) {
                        list = oldExchange.getIn().getBody(List.class);
                        result = oldExchange;
                    }
                    else {
                        list = new ArrayList<>();
                        result = newExchange;
                    }
                    Response resp = newExchange.getMessage().getBody(Response.class);
                    if (resp != null && resp.getCode() == ResponseCodeType.OK) {
                        list.add(newExchange.getMessage().getBody(Response.class));
                    }
                    result.getMessage().setBody(list, List.class);
                    return result;
                })
            .end()
            .process(exchange -> { if (applicationState != null) applicationState.requestMade(); })
            .choice()
                .when(simple("${camelContext.hasEndpoint(direct:applicant-audit)} != null"))
                    .wireTap("direct:applicant-audit")
            .end();
    }
}
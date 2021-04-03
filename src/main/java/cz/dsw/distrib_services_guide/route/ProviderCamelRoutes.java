package cz.dsw.distrib_services_guide.route;

import cz.dsw.distrib_services_guide.component.ApplicationStateComponent;
import cz.dsw.distrib_services_guide.component.TokenFactory;
import cz.dsw.distrib_services_guide.entity.Request;
import cz.dsw.distrib_services_guide.entity.Response;
import cz.dsw.distrib_services_guide.entity.ResponseCodeType;
import cz.dsw.distrib_services_guide.entity.service.RequestA;
import cz.dsw.distrib_services_guide.entity.service.RequestB;
import cz.dsw.distrib_services_guide.entity.service.ResponseA;
import cz.dsw.distrib_services_guide.entity.service.ResponseB;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile(value = "provider")
public class ProviderCamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ProviderCamelRoutes.class);

    @Autowired
    TokenFactory factory;

    @Autowired(required = false)
    private ApplicationStateComponent applicationState;

    @Override
    public void configure() throws Exception {

        from("activemq:queue:{{provider.queue}}").routeId("provider")
            .process(exchange -> exchange.setProperty("Request", exchange.getMessage().getBody(Request.class)))
            .choice()
                .when(body().isInstanceOf(RequestA.class))
                    .process(exchange -> {
                        RequestA request = exchange.getMessage().getBody(RequestA.class);
                        ResponseA response = factory.tokenInstance(request.getTid(), ResponseA.class);
                        response.setCode(ResponseCodeType.OK);
                        response.setResult(request.getValue() + new Random().nextInt((int) request.getValue() / 2));
                        exchange.getMessage().setBody(response);
                    })
                .when(body().isInstanceOf(RequestB.class))
                    .process(exchange -> {
                        RequestB request = exchange.getMessage().getBody(RequestB.class);
                        ResponseB response = factory.tokenInstance(request.getTid(), ResponseB.class);
                        response.setCode(ResponseCodeType.OK);
                        response.setText("text length: " + request.getText().length());
                        exchange.getMessage().setBody(response);
                    })
                .otherwise()
                    .process(exchange -> {
                        Request request = exchange.getMessage().getBody(Request.class);
                        Response response = factory.tokenInstance(request.getTid(), Response.class);
                        response.setCode(ResponseCodeType.REFUSED);
                        exchange.getMessage().setBody(response);
                    })
            .end()
            .process(exchange -> { if (applicationState != null) applicationState.responseMade(); })
            .choice()
                .when(simple("${camelContext.hasEndpoint(direct:provider-audit)} != null"))
                    .wireTap("direct:provider-audit")
            .end();
    }
}

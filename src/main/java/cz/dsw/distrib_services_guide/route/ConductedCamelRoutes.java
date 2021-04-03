package cz.dsw.distrib_services_guide.route;

import cz.dsw.distrib_services_guide.Application;
import cz.dsw.distrib_services_guide.entity.Message;
import cz.dsw.distrib_services_guide.entity.conduct.RestartMessage;
import cz.dsw.distrib_services_guide.entity.conduct.TimeStampMessage;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
@Profile(value = "conducted")
public class ConductedCamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ConductedCamelRoutes.class);

    @Override
    public void configure() throws Exception {

        from("{{services.conduct.uri}}").routeId("conducted")
            .choice()
                .when(simple("${camelContext.hasEndpoint(direct:conducted-audit)} != null"))
                    .wireTap("direct:conducted-audit")
                    .endChoice()
                .otherwise()
            .end()
            .choice()
                .when(body().isInstanceOf(TimeStampMessage.class))
                    .process(exchange -> {
                        TimeStampMessage msg = exchange.getMessage().getBody(TimeStampMessage.class);
                        logger.info("Setting machine time to: {}", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(msg.getTs()));
                    })
                .when(body().isInstanceOf(RestartMessage.class))
                    .process(exchange -> {
                        logger.info("Restarting machine ...");
                        Application.restart();
                    })
                .otherwise()
                    .process(exchange -> logger.warn("Conduct Command '{}' is not supported!", exchange.getMessage().getBody(Message.class).getClass().getCanonicalName()))
            .end();
    }
}
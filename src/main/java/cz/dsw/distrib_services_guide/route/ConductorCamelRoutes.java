package cz.dsw.distrib_services_guide.route;

import cz.dsw.distrib_services_guide.component.TokenFactory;
import cz.dsw.distrib_services_guide.entity.conduct.TimeStampMessage;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(value = "conductor")
public class ConductorCamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ConductorCamelRoutes.class);

    @Autowired
    TokenFactory factory;

    @Override
    public void configure() throws Exception {

        from("timer://conductor-ts?fixedRate=true&delay=1000&period={{conduct.timestamp.period:30000}}&repeatCount=0").routeId("conductor-ts")
            .process(exchange -> exchange.getMessage().setBody(factory.tokenInstance(TimeStampMessage.class)))
            .to("{{services.conduct.uri}}");

        from("direct:conductor-restart").routeId("conductor-restart")
            .to("{{services.conduct.uri}}")
            .choice()
                .when(simple("${camelContext.hasEndpoint(direct:conductor-audit)} != null"))
                    .wireTap("direct:conductor-audit")
            .end();

    }
}
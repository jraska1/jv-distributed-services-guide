package cz.dsw.distrib_services_guide.route;

import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(value = "state-aware")
public class StateAwareCamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(StateAwareCamelRoutes.class);

    @Override
    public void configure() throws Exception {

        from("direct:state-aware").routeId("state-aware")
            .onException(ExchangeTimedOutException.class)
                .handled(true)
                .process(exchange -> exchange.getMessage().setBody(null))
            .end()
            .to("{{services.repository.uri}}");
    }
}

package cz.dsw.distrib_services_guide.route;

import cz.dsw.distrib_services_guide.component.TokenFactory;
import cz.dsw.distrib_services_guide.entity.configuration.NodeConfiguration;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;

@Component
@Profile(value = "configurable")
public class ConfigurableCamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurableCamelRoutes.class);

    @Autowired
    private Map<URI, NodeConfiguration> configurations;

    @Value("${node.id}")
    private String nodeId;

    @Value("${provider.queue:#{null}}")
    private String queue;

    @Autowired
    TokenFactory factory;

    @Override
    public void configure() throws Exception {

        from("direct:configuration").routeId("configuration")
            .process(exchange -> logger.info("PUBLISHED: {}", exchange.getMessage().getBody(NodeConfiguration.class).toString()))
            .to("{{services.configurable.uri}}");

        from("{{services.configurable.uri}}").routeId("configurable")
            .process(exchange -> {
                NodeConfiguration config = exchange.getMessage().getBody(NodeConfiguration.class);
                exchange.getMessage().setBody(null);
                if (config != null) {
                    if (config.getAlive() != null && config.getAlive()) {
                        logger.info("CONFIGURATION: PUT {}", config.getNid().toString());
                        configurations.put(config.getNid(), config);
                        if (config.getPlea() != null && config.getPlea() && !config.getNid().toString().equals(nodeId)) {
                            NodeConfiguration myConfig = factory.tokenInstance(NodeConfiguration.class);
                            myConfig.setQueue(queue);
                            myConfig.setAlive(true);
                            myConfig.setPlea(false);
                            exchange.getMessage().setBody(myConfig);
                        }
                    }
                    else {
                        logger.info("CONFIGURATION: REMOVE {}", config.getNid().toString());
                        configurations.remove(config.getNid());
                    }
                }
            })
            .choice()
                .when(body().isNotNull())
                    .to("direct:configuration")
                .endChoice()
            .end();
    }
}

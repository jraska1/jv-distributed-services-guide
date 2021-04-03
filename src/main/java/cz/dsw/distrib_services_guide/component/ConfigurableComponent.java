package cz.dsw.distrib_services_guide.component;

import cz.dsw.distrib_services_guide.entity.configuration.NodeConfiguration;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@Profile(value = "configurable")
public class ConfigurableComponent {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurableComponent.class);

    @Bean
    public Map<URI, NodeConfiguration> configurations() {
        return Collections.synchronizedMap(new HashMap<>());
    }

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    TokenFactory factory;

    @Value("${provider.queue:#{null}}")
    private String queue;

    public void init() {
        NodeConfiguration config = factory.tokenInstance(NodeConfiguration.class);
        config.setQueue(queue);
        config.setAlive(true);
        config.setPlea(true);
        producerTemplate.sendBody("direct:configuration", config);
    }

    public void close() {
        NodeConfiguration config = factory.tokenInstance(NodeConfiguration.class);
        config.setAlive(false);
        config.setPlea(false);
        producerTemplate.sendBody("direct:configuration", config);
    }
}

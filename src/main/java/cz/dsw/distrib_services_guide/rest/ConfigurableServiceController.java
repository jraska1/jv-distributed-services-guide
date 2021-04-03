package cz.dsw.distrib_services_guide.rest;

import cz.dsw.distrib_services_guide.entity.configuration.NodeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@Profile(value = "configurable")
public class ConfigurableServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurableServiceController.class);

    @Autowired
    private Map<URI, NodeConfiguration> configurations;

    @RequestMapping(value = "/configuration")
    public ResponseEntity<Map<URI, NodeConfiguration>> getConfigurations() {

        return new ResponseEntity<>(configurations, HttpStatus.OK);
    }
}
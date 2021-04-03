package cz.dsw.distrib_services_guide.rest;

import cz.dsw.distrib_services_guide.component.TokenFactory;
import cz.dsw.distrib_services_guide.entity.conduct.RestartMessage;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile(value = "conductor")
public class ConductorServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ConductorServiceController.class);

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    TokenFactory factory;

    @RequestMapping(value = "/conduct/restart")
    public void claimRestart() {

        producerTemplate.sendBody("direct:conductor-restart", factory.tokenInstance(RestartMessage.class));
    }
}
package cz.dsw.distrib_services_guide.rest;

import cz.dsw.distrib_services_guide.component.TokenFactory;
import cz.dsw.distrib_services_guide.entity.configuration.NodeConfiguration;
import cz.dsw.distrib_services_guide.entity.service.RequestA;
import cz.dsw.distrib_services_guide.entity.service.RequestB;
import cz.dsw.distrib_services_guide.entity.service.ResponseA;
import cz.dsw.distrib_services_guide.entity.service.ResponseB;
import cz.dsw.distrib_services_guide.route.ApplicantCamelRoutes;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Profile(value = "applicant")
public class ApplicantServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicantServiceController.class);

    private static final String JMS_EXPIRATION = "JMSExpiration";

    @Autowired
    private ProducerTemplate producerTemplate;

    @Value("${applicant.destinations:#{null}}")
    List<String> destinations;

    @Autowired(required = false)
    private Map<URI, NodeConfiguration> configurations;

    @Autowired
    TokenFactory factory;

    @RequestMapping(value = "/rest/appl01")
    public ResponseEntity<List<ResponseA>> restApplicant01(
            @RequestParam(value = "value") int value,
            @RequestParam(value = "expire", required = false, defaultValue = "3000") Long expire) {

        RequestA request = factory.tokenInstance(RequestA.class);
        request.setValue(value);
        Map<String, Object> headers = new HashMap<>();
        headers.put(JmsConstants.JMS_REQUEST_TIMEOUT, expire);
        headers.put(JMS_EXPIRATION, System.currentTimeMillis() + expire);

        List<String> recipients = null;
        if (configurations != null)
            recipients = configurations.values().stream().filter(x -> x.getQueue() != null).map(NodeConfiguration::getQueue).collect(Collectors.toList());
        else if (destinations != null)
            recipients = destinations;

        if (recipients != null && recipients.size() > 0)
            headers.put(ApplicantCamelRoutes.RECIPIENT_LIST, recipients);
        else
            return ResponseEntity.notFound().build();

        List<ResponseA> response = producerTemplate.requestBodyAndHeaders("direct:applicant", request, headers, List.class);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/rest/appl02")
    public ResponseEntity<List<ResponseB>> restApplicant02(
            @RequestParam(value = "text") String text,
            @RequestParam(value = "expire", required = false, defaultValue = "3000") Long expire) {

        RequestB request = factory.tokenInstance(RequestB.class);
        request.setText(text);
        Map<String, Object> headers = new HashMap<>();
        headers.put(JmsConstants.JMS_REQUEST_TIMEOUT, expire);
        headers.put(JMS_EXPIRATION, System.currentTimeMillis() + expire);

        List<String> recipients = null;
        if (configurations != null)
            recipients = configurations.values().stream().filter(x -> x.getQueue() != null).map(NodeConfiguration::getQueue).collect(Collectors.toList());
        else if (destinations != null)
            recipients = destinations;

        if (recipients != null && recipients.size() > 0)
            headers.put(ApplicantCamelRoutes.RECIPIENT_LIST, recipients);
        else
            return ResponseEntity.notFound().build();

        List<ResponseB> response = producerTemplate.requestBodyAndHeaders("direct:applicant", request, headers, List.class);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
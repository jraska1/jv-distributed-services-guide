package cz.dsw.distrib_services_guide.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.dsw.distrib_services_guide.entity.ResponseCodeType;
import cz.dsw.distrib_services_guide.entity.repository.OperationType;
import cz.dsw.distrib_services_guide.entity.repository.RepositoryRequest;
import cz.dsw.distrib_services_guide.entity.repository.RepositoryResponse;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.jms.JmsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Profile(value = "state-aware")
public class ApplicationStateComponent {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStateComponent.class);

    private static final long SERVICE_EXPIRE = 1000;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    TokenFactory factory;

    private final Map<String, Integer> statistics;

    public ApplicationStateComponent() {
        statistics = new HashMap<>();
        statistics.put("requests", 0);
        statistics.put("responses", 0);
    }

    public void init() {
        RepositoryRequest request = factory.tokenInstance(RepositoryRequest.class);
        request.setOperation(OperationType.READ);
        request.setKey(request.getNid().toString());
        Map<String, Object> headers = new HashMap<>();
        headers.put(JmsConstants.JMS_REQUEST_TIMEOUT, SERVICE_EXPIRE);

        RepositoryResponse response = producerTemplate.requestBodyAndHeaders("direct:state-aware", request, headers, RepositoryResponse.class);
        if (response != null && response.getCode() == ResponseCodeType.OK) {
            logger.info("Read from Repository succeeded.");
            if (response.getValue() != null) {
                try {
                    Map<String, Integer> data = (Map) jsonMapper.readValue(response.getValue(), Map.class);
                    data.entrySet().forEach(e -> statistics.put(e.getKey(), e.getValue()));
                } catch (JsonProcessingException e) {
                    logger.error("Cannot load application state", e);
                }
            }
        }
        else {
            logger.warn("Read from Repository failed!");
        }
        logger.info("Application State: {}", statistics.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", ")));
    }

    public void close() {
        try {
            String data = jsonMapper.writeValueAsString(statistics);
            RepositoryRequest request = factory.tokenInstance(RepositoryRequest.class);
            request.setOperation(OperationType.UPSERT);
            request.setKey(request.getNid().toString());
            request.setValue(data);
            Map<String, Object> headers = new HashMap<>();
            headers.put(JmsConstants.JMS_REQUEST_TIMEOUT, SERVICE_EXPIRE);

            RepositoryResponse response = producerTemplate.requestBodyAndHeaders("direct:state-aware", request, headers, RepositoryResponse.class);
            if (response != null && response.getCode() == ResponseCodeType.OK)
                logger.info("Upsert to Repository succeeded.");
            else
                logger.warn("Upsert to Repository failed!");
        } catch (JsonProcessingException e) {
            logger.error("Cannot store application state", e);
        }
    }

    public void requestMade() {
        statistics.put("requests", statistics.get("requests") + 1);
    }

    public void responseMade() {
        statistics.put("responses", statistics.get("responses") + 1);
    }
}

package cz.dsw.distrib_services_guide.route;

import cz.dsw.distrib_services_guide.component.TokenFactory;
import cz.dsw.distrib_services_guide.entity.Request;
import cz.dsw.distrib_services_guide.entity.ResponseCodeType;
import cz.dsw.distrib_services_guide.entity.repository.RepositoryRequest;
import cz.dsw.distrib_services_guide.entity.repository.RepositoryResponse;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Profile(value = "repository")
public class RepositoryCamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryCamelRoutes.class);

    @Autowired
    Map<String, String> repository;

    @Autowired
    TokenFactory factory;

    @Override
    public void configure() throws Exception {

        from("{{services.repository.uri}}").routeId("repository")
            .choice()
                .when(body().isInstanceOf(RepositoryRequest.class))
                    .process(exchange -> {
                        RepositoryRequest request = exchange.getMessage().getBody(RepositoryRequest.class);
                        RepositoryResponse response = factory.tokenInstance(request.getTid(), RepositoryResponse.class);
                        response.setCode(ResponseCodeType.OK);
                        switch (request.getOperation()) {
                            case CREATE:
                                if (!repository.containsKey(request.getKey()))
                                    repository.put(request.getKey(), request.getValue());
                                else
                                    response.setCode(ResponseCodeType.REFUSED);
                                break;
                            case READ:
                                response.setValue(repository.getOrDefault(request.getKey(), null));
                                break;
                            case UPDATE:
                                if (repository.containsKey(request.getKey()))
                                    repository.put(request.getKey(), request.getValue());
                                else
                                    response.setCode(ResponseCodeType.REFUSED);
                                break;
                            case UPSERT:
                                repository.put(request.getKey(), request.getValue());
                                break;
                            case DELETE:
                                if (repository.containsKey(request.getKey()))
                                    repository.remove(request.getKey());
                                else
                                    response.setCode(ResponseCodeType.REFUSED);
                                break;
                        }
                        exchange.getMessage().setBody(response);
                    })
                .otherwise()
                    .process(exchange -> {
                        Request request = exchange.getMessage().getBody(Request.class);
                        RepositoryResponse response = factory.tokenInstance(request.getTid(), RepositoryResponse.class);
                        response.setCode(ResponseCodeType.REFUSED);
                        exchange.getMessage().setBody(response);
                    })
            .end();
    }
}

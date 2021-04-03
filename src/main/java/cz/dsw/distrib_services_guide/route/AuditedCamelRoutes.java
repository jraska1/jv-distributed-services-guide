package cz.dsw.distrib_services_guide.route;

import cz.dsw.distrib_services_guide.component.TokenFactory;
import cz.dsw.distrib_services_guide.entity.Request;
import cz.dsw.distrib_services_guide.entity.Response;
import cz.dsw.distrib_services_guide.entity.audit.ApplicantAuditRecord;
import cz.dsw.distrib_services_guide.entity.audit.AuditRecord;
import cz.dsw.distrib_services_guide.entity.audit.ConductedAuditRecord;
import cz.dsw.distrib_services_guide.entity.audit.ProviderAuditRecord;
import cz.dsw.distrib_services_guide.entity.conduct.RestartMessage;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile(value = "audited")
public class AuditedCamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AuditedCamelRoutes.class);

    @Autowired
    TokenFactory factory;

    @Override
    public void configure() throws Exception {

        from("direct:applicant-audit").routeId("applicant-audit")
            .process(exchange -> {
                Request request = exchange.getProperty("Request", Request.class);
                List<String> recipients = exchange.getProperty("Recipients", List.class);
                List<Response> response = exchange.getMessage().getBody(List.class);

                ApplicantAuditRecord auditRecord = factory.tokenInstance(request.getTid(), ApplicantAuditRecord.class);
                auditRecord.setEvent(request.auditEvent());
                auditRecord.setSeverity("info");
                auditRecord.setDestinations(recipients);
                auditRecord.setResponseFrom(response.stream().map(r -> r.getNid().toString()).collect(Collectors.toList()));
                exchange.getMessage().setBody(auditRecord);
            })
            .to("{{services.audit.uri}}");

        from("direct:provider-audit").routeId("provider-audit")
            .process(exchange -> {
                Request request = exchange.getProperty("Request", Request.class);
                Response response = exchange.getMessage().getBody(Response.class);

                ProviderAuditRecord auditRecord = factory.tokenInstance(response.getTid(), ProviderAuditRecord.class);
                auditRecord.setEvent(response.auditEvent());
                auditRecord.setSeverity("info");
                auditRecord.setRequestFrom(request.getNid().toString());
                auditRecord.setCode(response.getCode());
                exchange.getMessage().setBody(auditRecord);
            })
            .to("{{services.audit.uri}}");

        from("direct:conducted-audit").routeId("conducted-audit")
            .choice()
                .when(body().isInstanceOf(RestartMessage.class))
                    .process(exchange -> {
                        RestartMessage request = exchange.getMessage().getBody(RestartMessage.class);
                        ConductedAuditRecord auditRecord = factory.tokenInstance(request.getTid(), ConductedAuditRecord.class);
                        auditRecord.setEvent("conducted.restarted");
                        auditRecord.setSeverity("info");
                        auditRecord.setRequestFrom(request.getNid().toString());
                        exchange.getMessage().setBody(auditRecord);
                    })
                    .to("{{services.audit.uri}}")
            .end();

        from("direct:conductor-audit").routeId("conductor-audit")
            .choice()
                .when(body().isInstanceOf(RestartMessage.class))
                    .process(exchange -> {
                        RestartMessage request = exchange.getMessage().getBody(RestartMessage.class);
                        AuditRecord auditRecord = factory.tokenInstance(request.getTid(), AuditRecord.class);
                        auditRecord.setEvent("conductor.restart");
                        auditRecord.setSeverity("info");
                        exchange.getMessage().setBody(auditRecord);
                    })
                    .to("{{services.audit.uri}}")
            .end();
    }
}

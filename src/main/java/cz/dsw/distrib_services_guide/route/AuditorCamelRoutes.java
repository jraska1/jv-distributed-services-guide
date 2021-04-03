package cz.dsw.distrib_services_guide.route;

import cz.dsw.distrib_services_guide.entity.audit.AuditRecord;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile(value = "auditor")
public class AuditorCamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(AuditorCamelRoutes.class);

    @Autowired
    List<AuditRecord> auditStorage;

    @Override
    public void configure() throws Exception {

        from("{{services.audit.uri}}").routeId("auditor")
            .process(exchange -> {
                AuditRecord auditRecord = exchange.getMessage().getBody(AuditRecord.class);
                logger.info("Audit Record written to the Storage: {}", auditRecord);
                auditStorage.add(auditRecord);
            });
    }
}

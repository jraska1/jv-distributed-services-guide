package cz.dsw.distrib_services_guide.component;

import cz.dsw.distrib_services_guide.entity.audit.AuditRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Profile(value = "auditor")
public class AuditorComponent {

    @Bean
    public List<AuditRecord> auditStorage() {
        return Collections.synchronizedList(new ArrayList<>());
    }
}

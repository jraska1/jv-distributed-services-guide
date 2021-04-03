package cz.dsw.distrib_services_guide.rest;

import cz.dsw.distrib_services_guide.entity.audit.AuditRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Profile(value = "auditor")
public class AuditorServiceController {

    private static final Logger logger = LoggerFactory.getLogger(AuditorServiceController.class);

    @Autowired
    List<AuditRecord> auditStorage;

    @RequestMapping(value = "/audit")
    public ResponseEntity<List<AuditRecord>> getAuditRecords() {

        return new ResponseEntity<>(auditStorage, HttpStatus.OK);
    }
}
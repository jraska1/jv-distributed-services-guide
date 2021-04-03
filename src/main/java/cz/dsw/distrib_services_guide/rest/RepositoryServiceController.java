package cz.dsw.distrib_services_guide.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Profile(value = "repository")
public class RepositoryServiceController {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryServiceController.class);

    @Autowired
    Map<String, String> repository;

    @RequestMapping(value = "/repository")
    public ResponseEntity<Map<String, String>> getAuditRecords() {

        return new ResponseEntity<>(repository, HttpStatus.OK);
    }
}
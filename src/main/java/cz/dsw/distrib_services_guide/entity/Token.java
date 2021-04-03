package cz.dsw.distrib_services_guide.entity;

import cz.dsw.distrib_services_guide.rest.ApplicantServiceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.UUID;

public abstract class Token implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Token.class);

    public static final String DEFAULT_TID_SCHEMA = "uuid";

    private final URI nid;
    private final String name;
    private final URI tid;
    private final Date ts;

    public Token(URI nid, String name) {
        this.nid = nid;
        this.name = name;
        URI tid = null;
        try {
            tid = new URI(DEFAULT_TID_SCHEMA, UUID.randomUUID().toString(), null);
        } catch (URISyntaxException e) {
            logger.error("Cannot created Transaction ID!", e);
        }
        this.tid = tid;
        this.ts = new Date();
    }

    public Token(URI nid, String name, URI tid) {
        this.nid = nid;
        this.name = name;
        this.tid = tid;
        this.ts = new Date();
    }

    public Token(URI nid, String name, URI tid, Date ts) {
        this.nid = nid;
        this.name = name;
        this.tid = tid;
        this.ts = ts;
    }

    public URI getNid() { return nid; }
    public String getName() {
        return name;
    }
    public URI getTid() { return tid; }
    public Date getTs() {
        return ts;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Token(nid={0}, name={1}, tid={2}, ts={3,time,long})", nid, name, tid, ts);
    }
}

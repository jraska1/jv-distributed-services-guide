package cz.dsw.distrib_services_guide.entity.service;

import cz.dsw.distrib_services_guide.entity.Request;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Objects;

public class RequestB extends Request {

    public static final String AUDIT_EVENT = "applicant.serviceB";

    private String text;

    public RequestB(URI nid, String name) {
        super(nid, name);
    }

    public RequestB(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public RequestB(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    public String getText() { return Objects.toString(text); }
    public void setText(String text) { this.text = text; }

    @Override
    public String toString() {
        return MessageFormat.format("RequestB(text={0}, {1})", text, super.toString());
    }

    public String auditEvent() { return AUDIT_EVENT; }
}

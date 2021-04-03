package cz.dsw.distrib_services_guide.entity.audit;

import cz.dsw.distrib_services_guide.entity.Message;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public class AuditRecord extends Message {

    private String event;
    private String severity;

    public AuditRecord(URI nid, String name) {
        super(nid, name);
    }

    public AuditRecord(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public AuditRecord(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    @Override
    public String toString() {
        return MessageFormat.format("AuditRecord(event={0}, severity={1}, {2})", event, severity, super.toString());
    }
}

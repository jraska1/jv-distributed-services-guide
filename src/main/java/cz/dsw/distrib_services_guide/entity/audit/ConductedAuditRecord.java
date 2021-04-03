package cz.dsw.distrib_services_guide.entity.audit;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public class ConductedAuditRecord extends AuditRecord {

    private String requestFrom;

    public ConductedAuditRecord(URI nid, String name) {
        super(nid, name);
    }

    public ConductedAuditRecord(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public ConductedAuditRecord(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    public String getRequestFrom() { return requestFrom; }
    public void setRequestFrom(String requestFrom) { this.requestFrom = requestFrom; }

    @Override
    public String toString() {
        return MessageFormat.format("ConductedAuditRecord(requestFrom={0}, code={1}, {2})", requestFrom, super.toString());
    }
}

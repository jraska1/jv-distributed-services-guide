package cz.dsw.distrib_services_guide.entity.audit;

import cz.dsw.distrib_services_guide.entity.ResponseCodeType;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public class ProviderAuditRecord extends AuditRecord {

    private String requestFrom;
    private ResponseCodeType code;

    public ProviderAuditRecord(URI nid, String name) {
        super(nid, name);
    }

    public ProviderAuditRecord(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public ProviderAuditRecord(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    public String getRequestFrom() { return requestFrom; }
    public void setRequestFrom(String requestFrom) { this.requestFrom = requestFrom; }
    public ResponseCodeType getCode() { return code; }
    public void setCode(ResponseCodeType code) { this.code = code; }

    @Override
    public String toString() {
        return MessageFormat.format("ProviderAuditRecord(requestFrom={0}, code={1}, {2})", requestFrom, code, super.toString());
    }
}

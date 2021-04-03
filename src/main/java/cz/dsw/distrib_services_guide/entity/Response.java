package cz.dsw.distrib_services_guide.entity;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public class Response extends Token {

    private static final String AUDIT_EVENT = "provider";

    private ResponseCodeType code;

    public Response(URI nid, String name) {
        super(nid, name);
        this.code = ResponseCodeType.OK;
    }

    public Response(URI nid, String name, URI tid) {
        super(nid, name, tid);
        this.code = ResponseCodeType.OK;
    }

    public Response(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
        this.code = ResponseCodeType.OK;
    }

    public ResponseCodeType getCode() { return code; }
    public void setCode(ResponseCodeType code) { this.code = code; }

    @Override
    public String toString() {
        return MessageFormat.format("Response(code={0}, {1})", code, super.toString());
    }

    public String auditEvent() { return AUDIT_EVENT; }
}

package cz.dsw.distrib_services_guide.entity.audit;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

public class ApplicantAuditRecord extends AuditRecord {

    private List<String> destinations;
    private List<String> responseFrom;

    public ApplicantAuditRecord(URI nid, String name) {
        super(nid, name);
    }

    public ApplicantAuditRecord(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public ApplicantAuditRecord(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    public List<String> getDestinations() { return destinations; }
    public void setDestinations(List<String> destinations) { this.destinations = destinations; }
    public List<String> getResponseFrom() { return responseFrom; }
    public void setResponseFrom(List<String> responseFrom) { this.responseFrom = responseFrom; }

    @Override
    public String toString() {
        return MessageFormat.format("ApplicantAuditRecord(destinations={0}, responseFrom={1}, {2})", destinations, responseFrom, super.toString());
    }
}

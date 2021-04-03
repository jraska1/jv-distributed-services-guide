package cz.dsw.distrib_services_guide.entity.repository;

import cz.dsw.distrib_services_guide.entity.Response;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public class RepositoryResponse extends Response {

    private String value;

    public RepositoryResponse(URI nid, String name) {
        super(nid, name);
    }

    public RepositoryResponse(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public RepositoryResponse(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() {
        return MessageFormat.format("RepositoryResponse(value={0}, {1})", value, super.toString());
    }
}

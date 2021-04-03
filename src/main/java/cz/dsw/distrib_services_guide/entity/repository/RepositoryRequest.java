package cz.dsw.distrib_services_guide.entity.repository;

import cz.dsw.distrib_services_guide.entity.Request;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public class RepositoryRequest extends Request {

    private OperationType operation;
    private String key;
    private String value;

    public RepositoryRequest(URI nid, String name) {
        super(nid, name);
    }

    public RepositoryRequest(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public RepositoryRequest(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    public OperationType getOperation() { return operation; }
    public void setOperation(OperationType operation) { this.operation = operation; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() {
        return MessageFormat.format("RepositoryRequest(operation={0}, key={1}, value={2}, {3})", operation, key, value, super.toString());
    }
}

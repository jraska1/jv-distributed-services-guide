package cz.dsw.distrib_services_guide.entity.conduct;

import cz.dsw.distrib_services_guide.entity.Message;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public class RestartMessage extends Message {

    public RestartMessage(URI nid, String name) {
        super(nid, name);
    }

    public RestartMessage(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public RestartMessage(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    @Override
    public String toString() {
        return MessageFormat.format("RestartMessage({0})", super.toString());
    }
}

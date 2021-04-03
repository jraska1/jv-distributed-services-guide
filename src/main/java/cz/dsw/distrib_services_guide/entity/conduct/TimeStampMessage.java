package cz.dsw.distrib_services_guide.entity.conduct;

import cz.dsw.distrib_services_guide.entity.Message;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public class TimeStampMessage extends Message {

    public TimeStampMessage(URI nid, String name) {
        super(nid, name);
    }

    public TimeStampMessage(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public TimeStampMessage(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    @Override
    public String toString() {
        return MessageFormat.format("TimeStampMessage({0})", super.toString());
    }
}

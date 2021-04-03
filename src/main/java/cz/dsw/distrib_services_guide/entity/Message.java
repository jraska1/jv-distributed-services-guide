package cz.dsw.distrib_services_guide.entity;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public abstract class Message extends Token {

    public Message(URI nid, String name) {
        super(nid, name);
    }

    public Message(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public Message(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    @Override
    public String toString() {
        return MessageFormat.format("Message({0})", super.toString());
    }
}

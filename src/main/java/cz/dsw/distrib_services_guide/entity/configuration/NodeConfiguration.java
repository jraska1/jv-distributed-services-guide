package cz.dsw.distrib_services_guide.entity.configuration;

import cz.dsw.distrib_services_guide.entity.Message;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Date;

public class NodeConfiguration extends Message {

    private String queue;
    private Boolean alive;
    private Boolean plea;

    public NodeConfiguration(URI nid, String name) {
        super(nid, name);
    }

    public NodeConfiguration(URI nid, String name, URI tid) {
        super(nid, name, tid);
    }

    public NodeConfiguration(URI nid, String name, URI tid, Date ts) {
        super(nid, name, tid, ts);
    }

    public String getQueue() {
        return queue;
    }
    public void setQueue(String queue) {
        this.queue = queue;
    }
    public Boolean getAlive() {
        return alive;
    }
    public void setAlive(Boolean alive) {
        this.alive = alive;
    }
    public Boolean getPlea() {
        return plea;
    }
    public void setPlea(Boolean plea) {
        this.plea = plea;
    }

    @Override
    public String toString() {
        return MessageFormat.format("NodeConfiguratin(queue={0}, alive={1}, plea={2}, {3})", queue, alive, plea, super.toString());
    }
}

package cz.dsw.distrib_services_guide.component;

import cz.dsw.distrib_services_guide.entity.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Date;

@Component
public class TokenFactory {

    private static final Logger logger = LoggerFactory.getLogger(TokenFactory.class);

    @Value("${node.id}")
    private String nodeId;
    @Value("${node.name}")
    private String nodeName;

    public <T extends Token> T tokenInstance(Class<T> aClass) {
        try {
            return aClass.getDeclaredConstructor(URI.class, String.class).newInstance(new URI(nodeId), nodeName);
        } catch (Exception e) {
            logger.error("Token instance creation failed!", e);
            return null;
        }
    }

    public <T extends Token> T tokenInstance(URI tid, Class<T> aClass) {
        try {
            return aClass.getDeclaredConstructor(URI.class, String.class, URI.class).newInstance(new URI(nodeId), nodeName, tid);
        } catch (Exception e) {
            logger.error("Token instance creation failed!", e);
            return null;
        }
    }

    public <T extends Token> T tokenInstance(URI tid, Date ts, Class<T> aClass) {
        try {
            return aClass.getDeclaredConstructor(URI.class, String.class, URI.class, Date.class).newInstance(new URI(nodeId), nodeName, tid, ts);
        } catch (Exception e) {
            logger.error("Token instance creation failed!", e);
            return null;
        }
    }
}

package cz.dsw.distrib_services_guide;

import cz.dsw.distrib_services_guide.component.ApplicationStateComponent;
import cz.dsw.distrib_services_guide.component.ConfigurableComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

@SpringBootApplication
public class Application implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static ConfigurableApplicationContext context;

    @Autowired(required = false)
    private ApplicationStateComponent applicationState;

    @Autowired(required = false)
    private ConfigurableComponent configurable;

    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
        addContextListeners();
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("*** Hello World, greetings from Dwarf ***");
        if (applicationState != null)
            applicationState.init();
        if (configurable != null)
            configurable.init();
    }

    public static void shutdown() {
        context.close();
    }

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);
        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(Application.class, args.getSourceArgs());
            addContextListeners();
        });
        thread.setDaemon(false);
        thread.start();
    }

    private static void addContextListeners() {
        context.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                try {
                    context.getBean(ApplicationStateComponent.class).close();
                }
                catch (NoSuchBeanDefinitionException ignored) { }
                try {
                    context.getBean(ConfigurableComponent.class).close();
                }
                catch (NoSuchBeanDefinitionException ignored) { }
            }
        });
    }
}

//    java -jar application.jar --spring.profiles.active=prod --spring.config.location=c:\config

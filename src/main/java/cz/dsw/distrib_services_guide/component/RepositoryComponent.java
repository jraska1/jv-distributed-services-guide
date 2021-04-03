package cz.dsw.distrib_services_guide.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@Profile(value = "repository")
public class RepositoryComponent {

    @Bean
    public Map<String, String> repository() {
        return Collections.synchronizedMap(new HashMap<>());
    }
}

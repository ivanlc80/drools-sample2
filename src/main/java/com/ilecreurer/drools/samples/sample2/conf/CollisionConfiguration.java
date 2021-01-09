package com.ilecreurer.drools.samples.sample2.conf;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CollisionConfiguration {
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CollisionConfiguration.class);

    @Bean
    public KieContainer kieContainer() {
        LOGGER.info("Creating ks...");
        final KieServices kieServices = KieServices.Factory.get();
        LOGGER.info("Creating kc...");
        final KieContainer kieContainer = kieServices.getKieClasspathContainer();

        return kieContainer;
    }
}

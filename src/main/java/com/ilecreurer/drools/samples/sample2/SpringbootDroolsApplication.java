package com.ilecreurer.drools.samples.sample2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class to launch the service.
 */
//CHECKSTYLE:OFF - Reason: spring-boot classes can not be declared as final and should have a default constructor.
@SpringBootApplication
public class SpringbootDroolsApplication {
     // CHECKSTYLE:ON
    /**
     * Main method to run the service.
     *
     * @param args argumentos de entrada
     */
    public static void main(final String[] args) {
        SpringApplication.run(SpringbootDroolsApplication.class, args);
    }
}

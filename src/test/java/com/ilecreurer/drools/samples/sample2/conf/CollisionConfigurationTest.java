package com.ilecreurer.drools.samples.sample2.conf;

import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CollisionConfigurationTest {

    @Autowired
    private AreaProperties areaProperties;

    @Autowired
    private KieContainer kieContainer;

    @Test
    void checkKBase() {
        KieBase kieBase = kieContainer.getKieBase("kbaseceprules");
        assertThat(kieBase).isNotNull();
    }

    @Test
    void checkAreaProperties() {
        assertThat(areaProperties).isNotNull();
        assertThat(areaProperties.getMaxLatitude()).isEqualTo(80.0d);
    }
}

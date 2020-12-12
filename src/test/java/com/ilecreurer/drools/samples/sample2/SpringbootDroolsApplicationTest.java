package com.ilecreurer.drools.samples.sample2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringbootDroolsApplicationTest {
    @Test
    void contextLoads() {
        assertThat(new Object()).isNotNull();
    }
}

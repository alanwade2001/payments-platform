package com.acme.payments;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModularityTests {

    @Test
    void verifiesModularity() {

        ApplicationModules.of(PaymentsApplication.class)
                .verify();
    }
}

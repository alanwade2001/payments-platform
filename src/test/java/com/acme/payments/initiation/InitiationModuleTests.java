package com.acme.payments.initiation;

import com.acme.payments.reference.Accounts;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ApplicationModuleTest
class InitiationModuleTests {

    @MockitoBean
    Accounts accounts;

    @Test
    void bootstraps() {
    }
}
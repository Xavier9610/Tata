package com.banco.account.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.banco.account.domain.*;
import com.banco.account.messaging.ClientEventConsumer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;

@SpringBootTest(properties = "spring.rabbitmq.listener.simple.auto-startup=false")
@AutoConfigureMockMvc
class MovementIntegrationTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    AccountRepository accounts;
    @Autowired
    ClientProjectionRepository clients;

    @BeforeEach
    void seed() {
        accounts.deleteAll();
        clients.deleteAll();
        var c = new ClientProjection();
        c.setClientId("1717");
        c.setName("Jose");
        c.setActive(true);
        c.setUpdatedAt(java.time.Instant.now());
        clients.save(c);
        var a = new Account();
        a.setNumber("478758");
        a.setType(AccountType.AHORRO);
        a.setOpeningBalance(new BigDecimal("100.00"));
        a.setCurrentBalance(new BigDecimal("100.00"));
        a.setClientId("1717");
        a.setActive(true);
        accounts.save(a);
    }

    @Test
    void withdrawalWithoutBalanceReturns422() throws Exception {
        mvc.perform(post("/movimientos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"numeroCuenta\":\"478758\",\"tipoMovimiento\":\"RETIRO\",\"valor\":101,\"estado\":true}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("Saldo no disponible"));
    }

    @Test
    void depositReturnsCreated() throws Exception {
        mvc.perform(post("/movimientos").contentType(MediaType.APPLICATION_JSON)
                .content("{\"numeroCuenta\":\"478758\",\"tipoMovimiento\":\"DEPOSITO\",\"valor\":50,\"estado\":true}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.saldo").value(150));
    }
}

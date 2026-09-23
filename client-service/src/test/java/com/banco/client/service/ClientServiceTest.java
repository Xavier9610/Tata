package com.banco.client.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import com.banco.client.api.*;
import com.banco.client.domain.*;
import com.banco.client.messaging.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    ClientRepository repo;
    @Mock
    ClientEventPublisher events;
    @InjectMocks
    ClientService service;

    @BeforeEach
    void setup() {
        service = new ClientService(repo, events, new BCryptPasswordEncoder());
    }

    ClientRequest request() {
        return new ClientRequest("Jose Lema", "Otavalo", "098", "1234", "1717", true);
    }

    @Test
    void createsAndPublishes() {
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        var result = service.create(request());
        assertThat(result.identificacion()).isEqualTo("1717");
        verify(events).publish(any(Client.class), eq("CREATED"));
    }

    @Test
    void rejectsDuplicate() {
        when(repo.existsByIdentification("1717")).thenReturn(true);
        assertThatThrownBy(() -> service.create(request())).isInstanceOf(ApiException.class)
                .hasMessageContaining("ya existe");
    }
}

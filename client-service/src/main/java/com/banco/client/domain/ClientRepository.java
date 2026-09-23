package com.banco.client.domain;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientId(String clientId);

    boolean existsByClientId(String clientId);

    boolean existsByIdentification(String identification);
}

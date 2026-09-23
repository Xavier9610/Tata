package com.banco.account.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientProjectionRepository extends JpaRepository<ClientProjection, String> {
    List<ClientProjection> findAllByOrderByNameAsc();
}

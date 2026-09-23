package com.banco.account.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByAccountNumberOrderByOccurredAtAscIdAsc(String number);

    List<Movement> findAllByOrderByOccurredAtAscIdAsc();

    Optional<Movement> findByIdempotencyKey(String key);
}

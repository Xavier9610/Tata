package com.banco.account.domain;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findByClientIdOrderByNumber(String clientId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Account a where a.number=:number")
    Optional<Account> lockByNumber(@Param("number") String number);
}

package com.cloudcmr.app.sales.repository;

import com.cloudcmr.app.domain.User;
import com.cloudcmr.app.sales.domain.SalesSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesSessionRepository extends JpaRepository<SalesSession, Long> {

    @Query("SELECT s FROM SalesSession s WHERE " +
        "s.status IN ('NEW', 'IN_PROGRESS')")
    Optional<SalesSession> findOpenBySeller(User seller);

}

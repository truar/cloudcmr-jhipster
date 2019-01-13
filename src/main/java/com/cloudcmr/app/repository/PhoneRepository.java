package com.cloudcmr.app.repository;

import com.cloudcmr.app.domain.Phone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Phone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

}

package com.cloudcmr.app.member.repository;

import com.cloudcmr.app.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


/**
 * Spring Data  repository for the Member entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByLastNameAndFirstNameAndBirthDate(String lastName, String firstName, LocalDate birthDate);

    @Query("SELECT m FROM Member m WHERE " +
        "LOWER(m.lastName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
        "LOWER(m.firstName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
        "LOWER(m.licenceNumber) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
        "LOWER(m.uscaNumber) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    Page<Member> searchMembers(@Param("searchText") String searchText, Pageable pageable);

}

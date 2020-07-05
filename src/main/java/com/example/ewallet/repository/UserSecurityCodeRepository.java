package com.example.ewallet.repository;

import com.example.ewallet.domain.UserSecurityCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author EhSan
 */
@Repository
public interface UserSecurityCodeRepository extends JpaRepository<UserSecurityCode, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE UserSecurityCode SET securityCode = :securityCode , codeExpirationDate = :date WHERE id = :phoneNum")
    void update(@Param("phoneNum") Long phoneNum, @Param("securityCode") String securityCode, @Param("date") Date date);

}

package com.example.ewallet.repository;

import com.example.ewallet.domain.TempUser;
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
public interface TempUserRepository extends JpaRepository<TempUser, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE TempUser SET securityCode = :secCode, codeExpiration = :date WHERE id = :num")
    void update(@Param("num") Long num, @Param("secCode") String secCode, @Param("date") Date date);
}

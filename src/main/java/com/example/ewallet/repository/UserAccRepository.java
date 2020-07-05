package com.example.ewallet.repository;

import com.example.ewallet.domain.UserAcc;
import com.example.ewallet.enums.Status;
import com.example.ewallet.resourcesimpl.dto.ResGetUserByPhoneNumberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserAccRepository extends JpaRepository<UserAcc, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE UserAcc SET password = :pass WHERE phoneNumber = :phoneNumber")
    void updatePassword(Long phoneNumber, String pass);

    @Transactional
    @Modifying
    @Query("UPDATE UserAcc SET name = :name, lastName = :lName, email = :email, status = :status WHERE phoneNumber = :phoneNumber")
    void updatePersonalInfo(@Param("phoneNumber") Long userId, @Param("name") String firstName,
                            @Param("lName") String lastName, @Param("email") String email, @Param("status") Status status);

    @Query("SELECT phoneNumber AS id, name AS name, lastName AS lastName FROM UserAcc WHERE phoneNumber = :id")
    Optional<ResGetUserByPhoneNumberDTO> getUserByPhoneNumber(@Param("id") Long id);
}

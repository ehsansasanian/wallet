package com.example.ewallet.repository;

import com.example.ewallet.domain.UserAcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author EhSan
 */
@Repository
public interface UserRepository extends JpaRepository<UserAcc, Long> {

}

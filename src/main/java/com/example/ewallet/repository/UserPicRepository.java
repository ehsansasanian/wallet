package com.example.ewallet.repository;

import com.example.ewallet.domain.UserPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserPicRepository extends JpaRepository<UserPic, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE UserPic SET picture = :pic WHERE phoneNum = :userId")
    Integer update(@Param("userId") Long userId, @Param("pic") Byte[] pic);
}

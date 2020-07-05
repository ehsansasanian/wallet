package com.example.ewallet.repository;

import com.example.ewallet.domain.UserAcc;
import com.example.ewallet.domain.Wallet;
import com.example.ewallet.enums.Status;
import com.example.ewallet.resourcesimpl.dto.ResGetAccountBalanceDTO;
import com.example.ewallet.resourcesimpl.dto.ResGetListWalletDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT id AS id, userAcc.phoneNumber AS num, balance AS balance, lastModified AS date FROM Wallet WHERE userAcc =:userAcc ")
    Optional<ResGetAccountBalanceDTO> getAccountBalance(@Param("userAcc") UserAcc userAcc);

    @Query("SELECT balance FROM Wallet WHERE userAcc = :user AND status <> :status")
    Double getBalanceByUser(@Param("user") UserAcc user, @Param("status") Status status);

    @Transactional
    @Modifying
    @Query("UPDATE Wallet SET balance = :amount WHERE userAcc = :user AND balance > :amount AND status <> :status")
    Integer withdraw(@Param("user") UserAcc user, @Param("amount") Double amount, @Param("status") Status status);


    @Transactional
    @Modifying
    @Query("UPDATE Wallet SET balance = balance + :amount WHERE userAcc = :user AND status <> :status")
    Integer deposit(@Param("user") UserAcc user, @Param("amount") Double amount, @Param("status") Status status);

    @Transactional
    @Modifying
    @Query("UPDATE Wallet SET status = :status WHERE userAcc.phoneNumber = :phoneNumber")
    void update(@Param("phoneNumber") Long phoneNumber, @Param("status") Status status);

    @Query(value = "SELECT w.id AS walletId, w.status AS walletCurrentStatus, w.balance AS balance, w.userAcc.phoneNumber AS phoneNumber, w.status AS walletStatus " +
            ", ua.status AS userStatus, ua.email AS email, ua.name AS name, ua.lastName AS lastName " +
            "FROM Wallet w " +
            "INNER JOIN UserAcc ua ON ua = w.userAcc "
            , countQuery = "SELECT COUNT (w.id) " +
            "FROM Wallet w " +
            "INNER JOIN UserAcc ua ON ua = w.userAcc ")
    Page<ResGetListWalletDTO> getAllByPagination(Pageable pagination);
}

package com.example.ewallet.repository;

import com.example.ewallet.domain.TransactionLogs;
import com.example.ewallet.resourcesimpl.dto.ResTransactionLogReportToAdminDTO;
import com.example.ewallet.resourcesimpl.dto.ResTurnoverDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE TransactionLogs SET coupleRef = :otherSide WHERE id = :ownerId")
    Integer updateCoupleRef(@Param("ownerId") Integer ownerId, @Param("otherSide") TransactionLogs otherSide);

    @Query(value = "SELECT  tl.amount AS transferredAmount, tl.date AS date, " +
            "tl.balanceAfterTransaction AS balanceAfterTransmission, tl.toAcc.phoneNumber AS destAccountNum," +
            "ua.name AS destFirstName, ua.lastName AS destLastName " +
            "FROM TransactionLogs AS tl " +
            "LEFT JOIN UserAcc ua ON ua = tl.logsOwner " +
            "WHERE (tl.toAcc.phoneNumber = :userId) " +
            "AND(:fromDate IS NULL OR tl.date >= :fromDate) " +
            "AND(:toDate IS NULL OR tl.date <= :toDate) " +
            "ORDER BY tl.date ASC "
            , countQuery = "SELECT COUNT(tl.id) " +
            "FROM TransactionLogs AS tl " +
            "LEFT JOIN UserAcc ua ON ua = tl.logsOwner " +
            "WHERE (tl.toAcc.phoneNumber = :userId) " +
            "AND(:fromDate IS NULL OR tl.date >= :fromDate) " +
            "AND(:toDate IS NULL OR tl.date <= :toDate) ")
    Page<ResTurnoverDTO> reportToUserByPagination(@Param("userId") Long userId, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);

    @Query(value = "SELECT  tl.amount AS transferredAmount, tl.date AS date, " +
            "tl.balanceAfterTransaction AS balanceAfterTransmission, tl.toAcc.phoneNumber AS destAccountNum," +
            "ua.name AS destFirstName, ua.lastName AS destLastName " +
            "FROM TransactionLogs AS tl " +
            "LEFT JOIN UserAcc ua ON ua = tl.logsOwner " +
            "WHERE tl.toAcc.phoneNumber = :userId " +
            "AND tl.date <= :toDate " +
            "ORDER BY tl.date ASC "
            , countQuery = "SELECT COUNT(tl.id) " +
            "FROM TransactionLogs AS tl " +
            "LEFT JOIN UserAcc ua ON ua = tl.logsOwner " +
            "WHERE tl.toAcc.phoneNumber = :userId " +
            "AND  tl.date <= :toDate ")
    Page<ResTurnoverDTO> reportToUserByPagination(@Param("userId") Long userId, @Param("toDate") Date toDate, Pageable pageable);

    @Query(value = "SELECT tl.id AS logId, w.status AS walletCurrentStatus,tl.amount AS transferredAmount,tl.balanceAfterTransaction AS balanceAfterTransaction " +
            ",tl.logsOwner.phoneNumber AS logOwnersPhoneNumber, tl.toAcc.phoneNumber AS destAccountPhoneNumber,ua.status AS userStatus, ua.email AS email, ua.name AS name, ua.lastName AS lastName " +
            "FROM TransactionLogs AS tl " +
            "LEFT JOIN UserAcc ua ON ua = tl.logsOwner " +
            "INNER JOIN Wallet w ON w.userAcc = tl.logsOwner " +
            "WHERE tl.date <= :toDate " +
            "ORDER BY tl.date ASC "
            , countQuery = "SELECT COUNT(tl.id) " +
            "FROM TransactionLogs AS tl " +
            "LEFT JOIN UserAcc ua ON ua = tl.logsOwner " +
            "INNER JOIN Wallet w ON w.userAcc = tl.logsOwner " +
            "WHERE tl.date <= :toDate ")
    Page<ResTransactionLogReportToAdminDTO> reportToAdminByPagination(@Param("toDate") Date toDate, Pageable pageable);

    @Query(value = "SELECT tl.id AS logId, w.status AS walletCurrentStatus,tl.amount AS transferredAmount,tl.balanceAfterTransaction AS balanceAfterTransaction " +
            ",tl.logsOwner.phoneNumber AS logOwnersPhoneNumber, tl.toAcc AS destAccountPhoneNumber,ua.status AS userStatus, ua.email AS email, ua.name AS name, ua.lastName AS lastName " +
            "FROM TransactionLogs AS tl " +
            "LEFT JOIN UserAcc ua ON ua = tl.logsOwner " +
            "INNER JOIN Wallet w ON w.userAcc = tl.logsOwner " +
            "WHERE tl.date <= :toDate " +
            "AND tl.date >= :fromDate " +
            "ORDER BY tl.date ASC "
            , countQuery = "SELECT COUNT(tl.id) " +
            "FROM TransactionLogs AS tl " +
            "LEFT JOIN UserAcc ua ON ua = tl.logsOwner " +
            "INNER JOIN Wallet w ON w.userAcc = tl.logsOwner " +
            "WHERE tl.date <= :toDate " +
            "AND tl.date >= :fromDate")
    Page<ResTransactionLogReportToAdminDTO> reportToAdminByPagination(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);


}

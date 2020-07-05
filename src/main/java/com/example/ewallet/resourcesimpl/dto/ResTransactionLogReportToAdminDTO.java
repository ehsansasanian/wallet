package com.example.ewallet.resourcesimpl.dto;

import com.example.ewallet.enums.Status;

public interface ResTransactionLogReportToAdminDTO {

    Integer getLogId();
    Status getWalletCurrentStatus();
    Double getTransferredAmount();
    Double getBalanceAfterTransaction();
    Long getLogOwnersPhoneNumber();
    Long getDestAccountPhoneNumber();
    Status getUserStatus();
    String getEmail();
    String getName();
    String getLastName();


}

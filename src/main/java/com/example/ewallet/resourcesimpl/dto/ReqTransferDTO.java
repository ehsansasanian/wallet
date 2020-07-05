package com.example.ewallet.resourcesimpl.dto;

import com.example.ewallet.base.ValidationTools;
import lombok.Data;

@Data
public class ReqTransferDTO {

    private Long destAccountNumber;
    private Double amount;

    public void validation(Long userId) {
        ValidationTools.phoneNumber(destAccountNumber, "destAccountNumber");
        ValidationTools.phoneNumber(userId, "userId");
        ValidationTools.nullObject(amount, "amount");
    }
}

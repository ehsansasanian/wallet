package com.example.ewallet.resourcesimpl.dto;

import lombok.Data;

@Data
public class ResTransferDTO {
    Long userId;
    Double balance;

    public ResTransferDTO(Long userId, Double balance) {
        this.userId = userId;
        this.balance = balance;
    }
}

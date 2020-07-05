package com.example.ewallet.resourcesimpl.dto;

import com.example.ewallet.enums.Status;

public interface ResGetListWalletDTO {

       Long getWalletId();
       Status getWalletCurrentStatus();
       Double getBalance();
       Long getPhoneNumber();
       Status getWalletStatus();
       Status getUserStatus();
       String getEmail();
       String getName();
       String getLastName();



}

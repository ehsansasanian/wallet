package com.example.ewallet.resourcesimpl.dto;

import java.util.Date;

public interface ResTurnoverDTO {
    Double getTransferredAmount();
    Date getDate();
    Double getBalanceAfterTransmission();
    Long getDestAccountNum();
    String getDestFirstName();
    String getDestLastName();

}

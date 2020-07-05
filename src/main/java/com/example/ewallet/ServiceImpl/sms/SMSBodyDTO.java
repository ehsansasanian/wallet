package com.example.ewallet.ServiceImpl.sms;

import lombok.Data;
/**
 * @author EhSan
 */
@Data
public class SMSBodyDTO {
    private String message;
    private String to;
    private String from;

    public SMSBodyDTO(String verificationCode, String number, String from) {
        this.message = verificationCode;
        this.to = number;
        this.from = from;
    }
}

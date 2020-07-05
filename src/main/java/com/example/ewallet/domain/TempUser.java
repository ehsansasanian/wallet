package com.example.ewallet.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author EhSan
 */
@Entity
@Data
public class TempUser {

    @Id
    private Long id;
    private String securityCode;
    private Date codeExpiration;

    public TempUser() {
    }

    public TempUser(Long num, String secCode, Date date) {
        this.id = num;
        this.securityCode = secCode;
        this.codeExpiration = date;
        System.out.println(this);
    }
}

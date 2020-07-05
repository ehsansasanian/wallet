package com.example.ewallet.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author EhSan
 */
@Entity
@Data
public class UserSecurityCode {

    @Id
    private Long id;

    private String securityCode;
    private Date codeExpirationDate;

    public UserSecurityCode() {
    }

    public UserSecurityCode(Long phoneNumber) {
        this.id = phoneNumber;
    }
}

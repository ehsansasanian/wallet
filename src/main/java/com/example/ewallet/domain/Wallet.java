package com.example.ewallet.domain;

import com.example.ewallet.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author EhSan
 */
@Entity
@Data
public
class Wallet {

    @Id
    @GeneratedValue
    private Long id;

    private Double balance;
    private Date lastModified;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "SMALLINT")
    private Status status;

    @ManyToOne
    private UserAcc userAcc;

    public Wallet() {
    }

    public Wallet(Double balance, Date date, Status status, UserAcc userAcc) {
        this.balance = balance;
        this.lastModified = date;
        this.status = status;
        this.userAcc = userAcc;

    }
}

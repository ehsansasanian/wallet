package com.example.ewallet.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
public class TransactionLogs {

    @Id
    @GeneratedValue
    private Integer id;

    //transferred amount
    private Double amount;
    private Date date;
    private Double balanceAfterTransaction;

    @ManyToOne
    private UserAcc logsOwner;
    @ManyToOne
    private UserAcc toAcc;

    @ManyToOne
    private TransactionLogs coupleRef;

    public TransactionLogs() {
    }

    public TransactionLogs(Double amount, Date date, double newBalance, UserAcc fromUser, UserAcc toUser) {
        this.amount = amount;
        this.date = date;
        this.balanceAfterTransaction = newBalance;
        this.logsOwner = fromUser;
        this.toAcc = toUser;
    }
}

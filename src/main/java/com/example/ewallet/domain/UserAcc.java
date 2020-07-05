package com.example.ewallet.domain;

import com.example.ewallet.constants.PatternList;
import com.example.ewallet.enums.Status;
import com.example.ewallet.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

/**
 * @author EhSan
 */
@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAcc {

    @Id
    private Long phoneNumber;
    private String name;
    private String lastName;

    @Pattern(regexp = PatternList.PASSWORD)
    private String password;

    @Pattern(regexp = PatternList.EMAIL)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "SMALLINT")
    private Status status;


    public UserAcc() {
    }

    public UserAcc(Long userId, Status status, UserRole role) {
        this.phoneNumber = userId;
        this.status = status;
        this.userRole = role;
        System.out.println(this);
    }

    public UserAcc(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

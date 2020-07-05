package com.example.ewallet.resourcesimpl.dto;

import lombok.Data;

/**
 * @author EhSan
 */
@Data
public class ResTokenDTO {

    private String token;
    private String refreshToken;
    private Status status;

    public ResTokenDTO(String token, String refreshToken, Status status) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.status = status;
    }

    public ResTokenDTO(Status status) {
        this.status = status;
    }

    public enum Status {
        INVALID_USERNAME_OR_PASSWORD, INVALID_SESSION, INVALID_CODE, EXPIRED_CODE_DATE, THIS_POST_HAS_NOT_ANY_ACCESS,
        HAS_NOT_ANY_POST, HAS_NOT_ANY_EMPLOYEE, OK, WMS_SERVICE_PROBLEM, NO_DEFAULT_EMPLOYEE, FAIL;
    }
}

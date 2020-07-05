package com.example.ewallet.resourcesimpl.dto;

import com.example.ewallet.enums.LoginResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author EhSan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResOAuthDTO {

    private Long userId;
    private LoginResponse response;

    public ResOAuthDTO(LoginResponse res) {
        this.response = res;
    }

    public ResOAuthDTO(Long id, LoginResponse res) {
        this.userId = id;
        this.response = res;
    }
}

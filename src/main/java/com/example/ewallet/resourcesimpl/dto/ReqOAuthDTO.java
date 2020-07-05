package com.example.ewallet.resourcesimpl.dto;

import com.example.ewallet.base.ValidationTools;
import lombok.Data;

/**
 * @author EhSan
 */
@Data
public class ReqOAuthDTO {

    private Long userId;
    private String securityCode;

    public void validation(){
        ValidationTools.phoneNumber(userId,"userId");
        ValidationTools.nullObject(securityCode,"securityCode");
    }
}

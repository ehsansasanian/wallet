package com.example.ewallet.resourcesimpl.dto;

import com.example.ewallet.base.ValidationTools;
import lombok.Data;

@Data
public class ReqSetPasswordDTO {

    private String password;
    private String securityCode;

    public void validation(String id, String smsCodeLength) {
        ValidationTools.password(password, "password");
        ValidationTools.phoneNumber(id, "phoneNumber");
        ValidationTools.securityCode(Integer.parseInt(smsCodeLength), securityCode, "securityCode");
    }

}

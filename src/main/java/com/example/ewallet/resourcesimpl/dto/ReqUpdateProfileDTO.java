package com.example.ewallet.resourcesimpl.dto;

import com.example.ewallet.base.ValidationTools;
import lombok.Data;


@Data
public class ReqUpdateProfileDTO {

    String firstName;
    String lastName;
    String email;
    Byte[] pic;
    public ReqUpdateProfileDTO() {
    }

    public void validation(String userId){
        ValidationTools.phoneNumber(userId,"phoneNumber");
        ValidationTools.name(firstName,"firstName");
        ValidationTools.name(lastName,"lastName");
        ValidationTools.email(email,"email");
    }
}

package com.example.ewallet.constants;

/**
 * @author EhSan
 */
public interface PatternList {

    String EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
    String PASSWORD = "^[A-Za-z0-9_!@#$&*]{6,30}$";
    Integer PHONE_LENGTH = 10;
//            "\\9[0-9]{9}$";

}

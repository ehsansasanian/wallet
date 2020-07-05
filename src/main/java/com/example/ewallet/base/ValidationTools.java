package com.example.ewallet.base;

import com.example.ewallet.exception.FieldValidationException;
import org.springframework.beans.factory.annotation.Value;

public class ValidationTools {

    @Value("${sms.code.length: default}")
    private String smsCodeLength;

    public static void phoneNumber(String value, Object fieldName) throws FieldValidationException {
        if (value == null || !PatternValidation.phone(value))
            throw new FieldValidationException("id");
    }


    public static void phoneNumber(Long value, Object fieldName) throws FieldValidationException {
        if (value == null || !PatternValidation.phone(String.valueOf(value)))
            throw new FieldValidationException("id");
    }

    public static void password(String value, Object fieldName) throws FieldValidationException {
        if (value == null)
            throw new FieldValidationException(fieldName.toString());
        else if (!PatternValidation.password(value))
            throw new FieldValidationException("password pattern not valid !", value);
    }

    public static void email(String value, Object fieldName) throws FieldValidationException {
        if (value == null || !PatternValidation.email(value))
            throw new FieldValidationException(fieldName.toString(), value);
    }

    public static void nullObject(Object value, Object fieldName) throws FieldValidationException {
        if (value == null)
            throw new FieldValidationException(fieldName.toString());
    }

    public static void securityCode(int length, String value, String fieldName) {
        if (value == null || value.length() != length)
            throw new FieldValidationException("code is wrong or null");
    }

    public static void name(String value, String fieldName) {
        if (value == null || value.isEmpty() || value.length()>15)
            throw new FieldValidationException(fieldName,value);
    }
}

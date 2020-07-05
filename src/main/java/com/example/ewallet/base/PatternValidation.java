package com.example.ewallet.base;

import com.example.ewallet.constants.PatternList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternValidation {

    public static Boolean base(String value, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(value);
        return m.find();
    }

    public static Boolean password(String value) {
        return base(value, PatternList.PASSWORD);
    }

    public static Boolean email(String value) {
        return base(value, PatternList.EMAIL);
    }

    public static Boolean phone(String value) {
        return value.length() == PatternList.PHONE_LENGTH;
    }


}

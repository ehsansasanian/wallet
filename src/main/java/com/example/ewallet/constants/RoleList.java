package com.example.ewallet.constants;

public class RoleList {

    private static final String ROLE = "ROLE_";
    private static final String HR_START = "hasRole('";
    private static final String HR_END = "')";

    public static final String ROLE_USER = ROLE + "USER";
    public static final String ROLE_ADMIN = ROLE + "ADMIN";


    public static final String PA_USER = HR_START + ROLE_USER + HR_END;
    public static final String PA_ADMIN = HR_START + ROLE_ADMIN + HR_END;

    public static final String PA_BOTH = PA_USER + "OR" + PA_ADMIN;


}

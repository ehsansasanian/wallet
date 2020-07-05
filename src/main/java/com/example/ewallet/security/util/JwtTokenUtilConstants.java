package com.example.ewallet.security.util;

/**
 * @author EhSan
 */
public class JwtTokenUtilConstants {
    /*
     * Base constants
     */
    public static final String CLAIM_KEY_ROLE_LIST = "rl";
    public static final String CLAIM_KEY_TOKEN_TYPE = "tt";
    public static final String CLAIM_KEY_CREATED = "created";


    /*
     * User other info
     */
    public static final String CLAIM_KEY_USER_ID = "userId";

    /*
     * Token type
     */
    public static final String TOKEN_TYPE_REFRESH_TOKEN = "rt";

//	public enum TokenType {
//		T, // TOKEN
//		RT // REFRESH_TOKEN
//	}

}

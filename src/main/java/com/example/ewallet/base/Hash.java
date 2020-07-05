package com.example.ewallet.base;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Author: Ian Gallagher <igallagher@securityinnovation.com>
 * <p>
 * This code utilizes jBCrypt, which you need installed to use. jBCrypt:
 * http://www.mindrot.org/projects/jBCrypt/
 */

public class Hash {
    // Define the BCrypt workload to use when generating hashes. 10-31 is a
    // valid value.
    private static int workload = 12;

    /**
     * This method can be used to generate a string representing an account suitable
     * for storing in a database. It will be an OpenBSD-style crypt(3) formatted
     * hash string of length=60 The bcrypt workload is specified in the above static
     * variable, a value from 10 to 31. Adas workload of 12 is a very reasonable
     * safe default as of 2013. This automatically handles secure 128-bit salt
     * generation and storage within the hash.
     *
     * @param plaintext The account's plaintext as provided during account creation,
     *                  or when changing an account's .
     * @return String - a string of length 60 that is the bcrypt hashed in crypt(3)
     *         format.
     */
    public static String hash(String plaintext) {
        String salt = BCrypt.gensalt(workload);
        String hashed = BCrypt.hashpw(plaintext, salt);

        return (hashed);
    }

    /**
     * This method can be used to verify a computed hash from a plaintext (e.g.
     * during a login request) with that of a stored hash from a database. The hash
     * from the database must be passed as the second variable.
     *
     * @param plaintext   The account's plaintext , as provided during a login
     *                    request
     * @param stored_hash The account's stored hash, retrieved from the
     *                    authorization database
     * @return boolean - true if the matches the of the stored hash, false otherwise
     */
    public static boolean check(String plaintext, String stored_hash) {
        boolean verified = false;

        if (null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new IllegalArgumentException("Invalid hash provided for comparison");

        verified = BCrypt.checkpw(plaintext, stored_hash);

        return (verified);
    }
}

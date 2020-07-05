package com.example.ewallet.base;

import java.util.Random;

/**
 * @author EhSan
 */
public class GenerateRandomChars {

    public static String generator(String candidateChars, int length) {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++)
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));

        return sb.toString();
    }

    public static String generatedCode(int length) {
        return generator("0123456789", length);
    }
}

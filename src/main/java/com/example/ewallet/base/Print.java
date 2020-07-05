package com.example.ewallet.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Print {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void print(Object subject, Object body) {
        try {
            System.out.println("\n*******************************\n" + "\t\t\t" + subject + "\n*******************************\n");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));
            System.out.println("\n*******************************\n" + "\t\t\t" + subject + "\n*******************************\n");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void print(Object body) {
        try {
            System.out.println("\n*******************************\n" + "\t\t\t");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body));
            System.out.println("\n*******************************\n" + "\t\t\t");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

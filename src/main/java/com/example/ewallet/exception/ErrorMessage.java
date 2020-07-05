package com.example.ewallet.exception;

import lombok.Data;

import java.util.Date;

/**
 * author : @EhSan
 */

@Data
public class ErrorMessage {

    private final String message;
    //    private final Throwable throwable;
    //    private final String details;
    private final Date timeStamp;

    public ErrorMessage(String message, Throwable throwable, Date date, String details) {
        this.message = message;
//        this.details = details;
//        this.throwable = throwable;
        this.timeStamp = date;
    }

    public ErrorMessage(String message, Date date) {
        this.message = message;
//        this.throwable = throwable;
        this.timeStamp = date;
    }
}

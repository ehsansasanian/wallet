package com.example.ewallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author EhSan
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UncheckedException extends RuntimeException {

    public UncheckedException(String message) {
        super(message);
    }

}

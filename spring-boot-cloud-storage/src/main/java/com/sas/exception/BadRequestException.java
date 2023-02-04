package com.sas.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends Exception {

    private int code;

    private HttpStatus httpStatus;

    private Object responseObject;

    public BadRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public BadRequestException(String exceptionMessage, HttpStatus httpStatus) {
        super(exceptionMessage);
        this.code = httpStatus.value();
        this.httpStatus = httpStatus;
    }

    public BadRequestException(String exceptionMessage, Object responseObject, HttpStatus httpStatus) {
        super(exceptionMessage);
        this.code = httpStatus.value();
        this.httpStatus = httpStatus;
        this.responseObject = responseObject;
    }

}

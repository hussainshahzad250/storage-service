package com.sas.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ObjectNotFoundException extends Exception {

    private int code;

    private HttpStatus httpStatus;

    private Object responseObject;

    public ObjectNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public ObjectNotFoundException(String exceptionMessage, HttpStatus httpStatus) {
        super(exceptionMessage);
        this.code = httpStatus.value();
        this.httpStatus = httpStatus;
    }

    public ObjectNotFoundException(String exceptionMessage, Object responseObject, HttpStatus httpStatus) {
        super(exceptionMessage);
        this.code = httpStatus.value();
        this.httpStatus = httpStatus;
        this.responseObject = responseObject;
    }

}

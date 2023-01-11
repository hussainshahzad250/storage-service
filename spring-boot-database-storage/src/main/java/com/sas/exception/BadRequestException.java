package com.sas.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BadRequestException extends Exception {

	private static final long serialVersionUID = -282913843145658333L;

	private int code;
	private Object data;
	private HttpStatus status;

	public BadRequestException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public BadRequestException(String exceptionMessage, HttpStatus httpStatus) {
		super(exceptionMessage);
		this.code = httpStatus.value();
		this.status = httpStatus;
	}

	public BadRequestException(String exceptionMessage, Object data, HttpStatus httpStatus) {
		super(exceptionMessage);
		this.code = httpStatus.value();
		this.status = httpStatus;
		this.data = data;
	}

}

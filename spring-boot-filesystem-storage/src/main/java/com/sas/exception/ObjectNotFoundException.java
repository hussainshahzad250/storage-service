package com.sas.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ObjectNotFoundException extends Exception {

	private static final long serialVersionUID = 460396670844622215L;

	private int code;
	private Object data;
	private HttpStatus status;

	public ObjectNotFoundException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public ObjectNotFoundException(String exceptionMessage, HttpStatus httpStatus) {
		super(exceptionMessage);
		this.code = httpStatus.value();
		this.status = httpStatus;
	}

	public ObjectNotFoundException(String exceptionMessage, Object data, HttpStatus httpStatus) {
		super(exceptionMessage);
		this.code = httpStatus.value();
		this.status = httpStatus;
		this.data = data;
	}

}

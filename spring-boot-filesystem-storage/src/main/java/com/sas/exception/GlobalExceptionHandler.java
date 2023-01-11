package com.sas.exception;

import java.util.LinkedHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String ERROR_MSG = "Something went Wrong, Please try Later";

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseEntity<Object> handleException(Exception ex) {
		log.error("Exception  occurs =>", ex);
		return new ResponseEntity<>(new Response(ERROR_MSG, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = BadRequestException.class)
	@ResponseBody
	public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
		log.error("BadRequestException occurs =>", ex);
		return new ResponseEntity<>(new Response(ex.getMessage(), ex.getStatus()), HttpStatus.OK);
	}

	@ExceptionHandler(value = ObjectNotFoundException.class)
	@ResponseBody
	public ResponseEntity<Object> handleObjectNotFoundException(ObjectNotFoundException ex) {
		log.error("ObjectNotFoundException occurs =>", ex);
		return new ResponseEntity<>(new Response(ex.getMessage(), ex.getStatus()), HttpStatus.OK);
	}

	@ExceptionHandler(value = MissingPathVariableException.class)
	@ResponseBody
	public ResponseEntity<Object> handleMissingPathVariableException(MissingPathVariableException ex) {
		log.error("HttpStatus.OK occurs =>", ex);
		return new ResponseEntity<>(new Response(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.OK);
	}

	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException ex) {
		log.error("HttpRequestMethodNotSupportedException occurs =>", ex);
		return new ResponseEntity<>(new Response(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MissingServletRequestParameterException.class)
	@ResponseBody
	public ResponseEntity<Object> handleException(MissingServletRequestParameterException ex) {
		log.error("MissingServletRequestParameterException occurs =>", ex);
		return new ResponseEntity<>(new Response(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		log.error("HttpMessageNotReadableException occurred =>", ex);
		return new ResponseEntity<>(new Response("Incorrect request body supplied", HttpStatus.BAD_REQUEST),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
		log.error("MethodArgumentNotValidException occurred =>", ex);
		StringBuilder fieldsValidated = new StringBuilder();
		LinkedHashMap<String, String> errors = new LinkedHashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			fieldsValidated.append(",").append(fieldName);
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(
				new Response("Please pass mandatory attributes " + fieldsValidated, errors, HttpStatus.BAD_REQUEST),
				HttpStatus.BAD_REQUEST);
	}
}

@Getter
@Setter
@NoArgsConstructor
class Response {

	private int code;
	private String message;
	private HttpStatus status;
	private Object data;
	private Long totalCount;

	public Response(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
		this.code = status.value();
	}

	public Response(String message, Object responseObject, HttpStatus status) {
		this.message = message;
		this.status = status;
		this.code = status.value();
		this.data = responseObject;
	}

	public Response(String message, Object data, Long totalCount, HttpStatus status) {
		this.code = status.value();
		this.message = message;
		this.status = status;
		this.data = data;
		this.totalCount = totalCount;
	}
}
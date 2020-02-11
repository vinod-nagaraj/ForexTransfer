package com.fund.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.fund.transfer.exception.NotFoundException;
import com.fund.transfer.exception.ImProperDataException;
import com.fund.transfer.exception.NoAccountFoundException;

import com.fund.transfer.dto.ErrorDto;




@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorDto> dateNotFoundException(NotFoundException e){
		ErrorDto errorDto = new ErrorDto();
		errorDto.setMessage(e.getMessage());
		errorDto.setStatusCode(HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}
	
	@ExceptionHandler(ImProperDataException.class)
	public ResponseEntity<ErrorDto> ImProperDataException(NotFoundException e){
		ErrorDto errorDto = new ErrorDto();
		errorDto.setMessage(e.getMessage());
		errorDto.setStatusCode(HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}
	@ExceptionHandler(NoAccountFoundException.class)
	public ResponseEntity<ErrorDto> NoAccountFoundException(NotFoundException e){
		ErrorDto errorDto = new ErrorDto();
		errorDto.setMessage(e.getMessage());
		errorDto.setStatusCode(HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}
	
	
}

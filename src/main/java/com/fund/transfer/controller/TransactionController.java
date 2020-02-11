package com.fund.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fund.transfer.dto.TransactionHistoryResponseDto;
import com.fund.transfer.exception.NotFoundException;
import com.fund.transfer.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
@RestController
@RequestMapping("/users/{userId}")
@Slf4j
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;

	@GetMapping
	public ResponseEntity<TransactionHistoryResponseDto> getTransactionHistory(
			@PathVariable Long userId) throws NotFoundException {
		

		return ResponseEntity.ok().body(transactionService.getTransactionHistory(userId));

	}
}

package com.fund.transfer.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.fund.transfer.constant.ApplicationConstant;
import com.fund.transfer.dto.ExchangeRateResponse;
import com.fund.transfer.dto.ResponseDto;
import com.fund.transfer.dto.TransactionDto;
import com.fund.transfer.entity.Account;
import com.fund.transfer.entity.Transaction;
import com.fund.transfer.repository.AccountRepository;
import com.fund.transfer.repository.TransactionRepository;
/*
 * Method transfer fund from one recipient to another recipient
 * 
 */
@Service
public class TransferServiceImpl implements TransferService {

	private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

	/**
	 * @author Vinod B N
	 * 
	 *         Method transfer fund from one recipient to another recipient
	 * 
	 * @param TransactionDto,userId
	 * @return responseDto 
	 * 
	 */

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public ResponseDto transfer(TransactionDto transactionDto, Long userId) {
		Account fromAcc = accountRepository.findByAccountNumber(transactionDto.getFromAcc());
		Account toAcc = accountRepository.findByAccountNumber(transactionDto.getToAcc());
		Double rate = getExchangeRates(fromAcc.getCurrency(), toAcc.getCurrency());

		ResponseDto responseDto = new ResponseDto();
		Transaction transaction = new Transaction();
		transaction.setFromAccount(transactionDto.getFromAcc());
		transaction.setToAccount(transactionDto.getToAcc());
		transaction.setTranferAmount(transactionDto.getAmount());
		transaction.setTransactionDate(LocalDateTime.now());
		transaction.setTransactionStatus(ApplicationConstant.PENDING_STATUS);
		transaction.setCalculatedAmount(transactionDto.getAmount() * rate);
		transaction.setUserId(userId);
		transactionRepository.save(transaction);
		responseDto.setStatusCode(ApplicationConstant.SUCCESS_CODE);
		return responseDto;
	}

	public Double getExchangeRates(String fromCur, String toCur) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity ReqEntity = new HttpEntity(headers);
		String base = fromCur;
		String url = "https://api.exchangeratesapi.io/latest?base=" + base;
		ResponseEntity<ExchangeRateResponse> rateResponse = restTemplate.exchange(url, HttpMethod.GET, ReqEntity,
				ExchangeRateResponse.class);
		ExchangeRateResponse exchange = rateResponse.getBody();
		return exchange.getRates().get(toCur);

	}

	@Scheduled(fixedDelay = 300000)
	public void completeTransfer() {
		List<Transaction> transactions = transactionRepository
				.findAllByTransactionStatus(ApplicationConstant.PENDING_STATUS);
		transactions.forEach(transaction -> {
			Account fromAcc = accountRepository.findByAccountNumber(transaction.getFromAccount());
			Account toAcc = accountRepository.findByAccountNumber(transaction.getToAccount());
			if (ObjectUtils.isEmpty(toAcc)|| (transaction.getTranferAmount()+ApplicationConstant.TRANSACTION_CHARGE) > fromAcc.getBalance()) {
				transaction.setTransactionStatus(ApplicationConstant.CANCELLED);
				transactionRepository.save(transaction);
				return;
			}
			transaction.setTransactionStatus(ApplicationConstant.COMPLETED);
			Double fromAccBal = fromAcc.getBalance() - transaction.getTranferAmount();
			Double toAccBal = toAcc.getBalance() + transaction.getCalculatedAmount();
			fromAcc.setBalance(fromAccBal);
			toAcc.setBalance(toAccBal);
			accountRepository.save(fromAcc);
			accountRepository.save(toAcc);
			System.out.println(transaction.getTransactionStatus());
			transactionRepository.save(transaction);
		});

	}

}

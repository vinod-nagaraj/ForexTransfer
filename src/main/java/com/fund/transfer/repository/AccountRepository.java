package com.fund.transfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fund.transfer.entity.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	public List<Account> findByUserId(Long userId);	
}

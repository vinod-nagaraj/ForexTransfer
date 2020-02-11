package com.fund.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fund.transfer.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Account findByAccountNumber(Long fromAcc);

}

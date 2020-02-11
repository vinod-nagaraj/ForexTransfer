package com.fund.transfer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fund.transfer.entity.User;




@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUserIdAndPassword(Long userId, String password);

}

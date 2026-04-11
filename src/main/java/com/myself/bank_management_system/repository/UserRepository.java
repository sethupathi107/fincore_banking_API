package com.myself.bank_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myself.bank_management_system.entity.User;

public interface UserRepository extends JpaRepository<User , Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);
}

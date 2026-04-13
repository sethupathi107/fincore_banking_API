package com.myself.FinCoreBankingApiApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myself.FinCoreBankingApiApplication.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String>{
        
}

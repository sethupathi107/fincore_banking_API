package com.myself.FinCoreBankingApiApplication.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.myself.FinCoreBankingApiApplication.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,String>{
         @Query("SELECT t FROM Transaction t WHERE (t.fromAccountNumber = :acct OR t.toAccountNumber = :acct) AND t.createAt BETWEEN :start AND :end")
        List<Transaction> findByAccountAndDateRange(@Param("acct") String accountNumber,
                                                 @Param("start") LocalDate start,
                                                 @Param("end") LocalDate end);
}

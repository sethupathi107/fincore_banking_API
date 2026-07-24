package com.myself.FinCoreBankingApiApplication.service.impl;


import org.springframework.stereotype.Component;

import com.myself.FinCoreBankingApiApplication.dto.TransactionDTO;
import com.myself.FinCoreBankingApiApplication.entity.Transaction;
import com.myself.FinCoreBankingApiApplication.repository.TransactionRepository;

@Component
public class TransactionServiceimpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceimpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(TransactionDTO transactionDto) {
        Transaction transaction = Transaction.builder()
        .transactionType(transactionDto.getTransactionType())
        .fromAccountNumber(transactionDto.getFromAccountNumber())
        .toAccountNumber(transactionDto.getToAccountNumber()) 
        .amount(transactionDto.getAmount())
        .status("SUCCESS")
        .build();
        transactionRepository.save(transaction);
        
    }

}

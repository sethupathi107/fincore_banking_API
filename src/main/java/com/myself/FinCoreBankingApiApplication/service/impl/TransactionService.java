package com.myself.FinCoreBankingApiApplication.service.impl;

import com.myself.FinCoreBankingApiApplication.dto.TransactionDTO;

public interface TransactionService {
    void saveTransaction(TransactionDTO transactionDto);
}

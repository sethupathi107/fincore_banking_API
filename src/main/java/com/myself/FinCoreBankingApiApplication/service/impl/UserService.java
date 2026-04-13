package com.myself.FinCoreBankingApiApplication.service.impl;

import com.myself.FinCoreBankingApiApplication.dto.BankResponse;
import com.myself.FinCoreBankingApiApplication.dto.CreditDebitRequest;
import com.myself.FinCoreBankingApiApplication.dto.TransferRequest;
import com.myself.FinCoreBankingApiApplication.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(String request);
    String nameEnquiry(String reques);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);
}

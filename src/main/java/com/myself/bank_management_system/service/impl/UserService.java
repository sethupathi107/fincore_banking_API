package com.myself.bank_management_system.service.impl;

import com.myself.bank_management_system.dto.BankResponse;
import com.myself.bank_management_system.dto.CreditDebitRequest;
import com.myself.bank_management_system.dto.TransferRequest;
import com.myself.bank_management_system.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(String request);
    String nameEnquiry(String reques);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);
}

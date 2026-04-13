package com.myself.FinCoreBankingApiApplication.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myself.FinCoreBankingApiApplication.dto.BankResponse;
import com.myself.FinCoreBankingApiApplication.dto.CreditDebitRequest;
import com.myself.FinCoreBankingApiApplication.dto.TransferRequest;
import com.myself.FinCoreBankingApiApplication.dto.UserRequest;
import com.myself.FinCoreBankingApiApplication.service.impl.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest user) {        
        return userService.createAccount(user);
    }
    
    @GetMapping("/balanceEnquiry/{accountno}")
    public BankResponse balanceEnquiry(@PathVariable String accountno){
        return userService.balanceEnquiry(accountno);
    }

    @GetMapping("/nameEnquiry/{accountno}")
    public String nameEnquiry(@PathVariable String accountno){
        return userService.nameEnquiry(accountno);
    }

    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }

    
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }

    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }
}

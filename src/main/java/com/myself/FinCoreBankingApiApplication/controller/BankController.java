package com.myself.FinCoreBankingApiApplication.controller;

import java.io.FileNotFoundException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.myself.FinCoreBankingApiApplication.service.impl.BankService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@AllArgsConstructor
@Slf4j

public class BankController {
    private BankService bankService;
    @GetMapping
    public void generateBankStatement(@RequestParam  String accountNumber, @RequestParam String startDate, @RequestParam String endDate ) throws FileNotFoundException, DocumentException{
        bankService.generateStatement(accountNumber, startDate, endDate);
    }
}

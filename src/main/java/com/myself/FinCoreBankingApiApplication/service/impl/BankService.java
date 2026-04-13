package com.myself.FinCoreBankingApiApplication.service.impl;

import java.io.FileNotFoundException;

import com.itextpdf.text.DocumentException;

public interface BankService {
    public void generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException;
}

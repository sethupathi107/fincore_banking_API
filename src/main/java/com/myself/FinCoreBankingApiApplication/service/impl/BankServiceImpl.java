package com.myself.FinCoreBankingApiApplication.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.myself.FinCoreBankingApiApplication.dto.EmailDetails;
import com.myself.FinCoreBankingApiApplication.entity.Transaction;
import com.myself.FinCoreBankingApiApplication.entity.User;
import com.myself.FinCoreBankingApiApplication.repository.TransactionRepository;
import com.myself.FinCoreBankingApiApplication.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BankServiceImpl implements BankService{

    private static final String FILE = "S:\\Learning\\Temp\\statement.pdf";
    
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void generateStatement(String accountNumber, String startDate, String endDate)
        throws FileNotFoundException, DocumentException {

        User user = userRepository.findByAccountNumber(accountNumber);
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

         List<Transaction> transactionList = transactionRepository.findAll().stream().filter(transaction -> transaction.getFromAccountNumber().equals(accountNumber) || transaction.getToAccountNumber().equals(accountNumber))
                                                                 .filter(transaction -> transaction.getCreateAt().isAfter(start) && transaction.getCreateAt().isBefore(end.plusDays(1))).toList();


        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);

        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);

        PdfPCell bankName = new PdfPCell(new Phrase("FinCore Bank"));
        bankName.setPadding(20f);
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);

        PdfPCell bankAddress = new PdfPCell(new Phrase(
                "23, Saveetha Nagar, Chennai, Tamil Nadu, India - 602105"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        document.add(bankInfoTable);

        Paragraph statementTitle = new Paragraph("Bank Statement");
        statementTitle.setSpacingAfter(10f);
        document.add(statementTitle);

        document.add(new Paragraph("Account Number: " + accountNumber));
        document.add(new Paragraph("From: " + startDate + " To: " + endDate));
        document.add(new Paragraph(" ")); 

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        table.addCell("Date");
        table.addCell("Type");
        table.addCell("Amount");
        table.addCell("From");
        table.addCell("To");

        for (Transaction txn : transactionList) {
            table.addCell(txn.getCreateAt().toString());
            table.addCell(txn.getTransactionType());
            table.addCell(String.valueOf(txn.getAmount()));
            table.addCell(txn.getFromAccountNumber());
            table.addCell(txn.getToAccountNumber());
        }

        document.add(table);

        document.close();

        emailService.sendEmailwithAttachment(EmailDetails.builder()
                                                         .Attachment(FILE)
                                                         .recipient(user.getEmail())
                                                         .subject("BANKSTATEMENT OF ACCOUNT")
                                                         .messageBody("The BankStatement for this Account Number : "+accountNumber+"is attached to this email")
                                                         .build());
    }
}

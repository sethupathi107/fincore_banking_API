package com.myself.FinCoreBankingApiApplication.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myself.FinCoreBankingApiApplication.dto.AccountInfo;
import com.myself.FinCoreBankingApiApplication.dto.BankResponse;
import com.myself.FinCoreBankingApiApplication.dto.CreditDebitRequest;
import com.myself.FinCoreBankingApiApplication.dto.EmailDetails;
import com.myself.FinCoreBankingApiApplication.dto.TransactionDTO;
import com.myself.FinCoreBankingApiApplication.dto.TransferRequest;
import com.myself.FinCoreBankingApiApplication.dto.UserRequest;
import com.myself.FinCoreBankingApiApplication.entity.User;
import com.myself.FinCoreBankingApiApplication.repository.UserRepository;
import com.myself.FinCoreBankingApiApplication.utils.AccountUtils;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                               .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                               .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                               .accountinfo(null)
                               .build();
        }

        User newUser = User.builder()
                           .firstName(userRequest.getFirstName())
                           .lastName(userRequest.getLastName())
                           .otherName(userRequest.getOtherName())
                           .gender(userRequest.getGender())
                           .address(userRequest.getAddress())
                           .stateOfOrgin(userRequest.getStateOfOrgin())
                           .accountNumber(AccountUtils.randomNumberGenerator())
                           .accountBalance(BigDecimal.ZERO)
                           .email(userRequest.getEmail())
                           .phoneNumber(userRequest.getPhoneNumber())
                           .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                           .status("ACTIVE")
                           .build();
        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                                                .recipient(savedUser.getEmail())
                                                .subject("ACCOUNT SUCCESS")
                                                .messageBody("Congratulations! Your Account has been Successfully Created.\nYour Account Details"+
                                                    "Account Name : "+newUser.getFirstName()+" "+newUser.getLastName()+" "+newUser.getOtherName()+"\nAccount Number : "+newUser.getAccountNumber()
                                                )
                                                .build();
        emailService.sendEmailAlert(emailDetails);
        
        return BankResponse.builder()
                           .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                           .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                           .accountinfo(AccountInfo.builder()
                                                   .accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName())
                                                   .accountBalance(newUser.getAccountBalance())
                                                   .accountNumber(savedUser.getAccountNumber())     
                                                   .build())   
                           .build();
    }

    @Override
    public BankResponse balanceEnquiry(String request) {
        if(!userRepository.existsByAccountNumber(request)){
            return BankResponse.builder()
                               .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                               .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                               .accountinfo(null) 
                               .build();
        }

        User fountUser = userRepository.findByAccountNumber(request);

        return BankResponse.builder()
                               .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                               .responseMessage(AccountUtils.ACCOUNT_FOUNT_MESSAGE)
                               .accountinfo(AccountInfo.builder()
                                                       .accountBalance(fountUser.getAccountBalance())
                                                       .accountNumber(fountUser.getAccountNumber())
                                                       .accountName(fountUser.getFirstName()+" "+fountUser.getLastName()+" "+fountUser.getOtherName()) 
                                                       .build() ) 
                               .build();
    }

    @Override
    public String nameEnquiry(String request) {
        if(!userRepository.existsByAccountNumber(request)){
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request);
        return foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName();
    }

    public BankResponse creditAccount(CreditDebitRequest request){
        if(!userRepository.existsByAccountNumber(request.getAccountNumber())){
            return BankResponse.builder()
                               .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                               .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                               .accountinfo(null) 
                               .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());

        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        TransactionDTO transactionDTO = TransactionDTO.builder()
        .fromAccountNumber(userToCredit.getAccountNumber())
        .toAccountNumber("BANK")
        .transactionType("CREDIT")
        .amount(request.getAmount())
        .build();

        transactionService.saveTransaction(transactionDTO);

        EmailDetails creditAlert= EmailDetails.builder()
        .subject("CREDIT ALERT")
        .recipient(userToCredit.getEmail())
        .messageBody("Dear Customer, ₹"+request.getAmount()+" has been Credited to your account. Your current balance is "+userToCredit.getAccountBalance())   
        .build();
        
        emailService.sendEmailAlert(creditAlert);

        return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                    .accountinfo(AccountInfo.builder()
                                            .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+userToCredit.getOtherName())
                                            .accountBalance(userToCredit.getAccountBalance())
                                            .accountNumber(userToCredit.getAccountNumber())
                                            .build()) 
                    .build(); 
    }

    public BankResponse debitAccount(CreditDebitRequest request){
        if(!userRepository.existsByAccountNumber(request.getAccountNumber())){
            return BankResponse.builder()
                               .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                               .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                               .accountinfo(null) 
                               .build();
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

        if(userToDebit.getAccountBalance().compareTo(request.getAmount()) < 0){
            return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE)
                        .accountinfo(null) 
                        .build(); 
        }

        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(userToDebit);

        TransactionDTO transactionDTO = TransactionDTO.builder()
        .fromAccountNumber(userToDebit.getAccountNumber())
        .toAccountNumber("BANK")
        .transactionType("DEBIT")
        .amount(request.getAmount())
        .build();

        transactionService.saveTransaction(transactionDTO);

        EmailDetails debitAlert= EmailDetails.builder()
        .subject("CREDIT ALERT")
        .recipient(userToDebit.getEmail())
        .messageBody("Dear Customer, ₹"+request.getAmount()+" has been Debited from your account. Your current balance is "+userToDebit.getAccountBalance())   
        .build();
        
        emailService.sendEmailAlert(debitAlert); 

        return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                    .accountinfo(AccountInfo.builder()
                                            .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName())
                                            .accountBalance(userToDebit.getAccountBalance())
                                            .accountNumber(userToDebit.getAccountNumber())
                                            .build()) 
                    .build(); 
    }

    @Override
    @Transactional
    public BankResponse transfer(TransferRequest request) {
        if(!userRepository.existsByAccountNumber(request.getDestinationAccountNumber())){
            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
            .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
            .accountinfo(null) 
            .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        User userToDebit = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        String sourseName = userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName();
        String destinationName = userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+userToCredit.getOtherName();

        if(userToDebit.getAccountBalance().compareTo(request.getAmount()) < 0){
            return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE)
                        .accountinfo(null) 
                        .build(); 
        }

        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(userToDebit);
        EmailDetails debitAlert= EmailDetails.builder()
        .subject("DEBIT ALERT")
        .recipient(userToDebit.getEmail())
        .messageBody("The sum of "+request.getAmount()+"has been deducted from your account! your current balance is "+userToDebit.getAccountBalance())
        .messageBody(String.format(
                                    "Dear Customer, ₹%.2f has been debited from your account and transferred to %s (Acct: XXXX%s). Your current balance is ₹%.2f.",
                                    request.getAmount(),
                                    destinationName,
                                    userToCredit.getAccountNumber().substring(5),
                                    userToDebit.getAccountBalance()
                                ))
        .build();
        
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);
        
        EmailDetails creditAlert= EmailDetails.builder()
        .subject("CREDIT ALERT")
        .recipient(userToCredit.getEmail())
        .messageBody("Dear Customer, ₹"+request.getAmount()+" has been credited to your account from "+sourseName+" (Acct: XXXX"+userToDebit.getAccountNumber().substring(5)+"). Your current balance is "+userToCredit.getAccountBalance())   
        .build();
        
        emailService.sendEmailAlert(debitAlert); 
        emailService.sendEmailAlert(creditAlert);
        
        TransactionDTO transactionDTO = TransactionDTO.builder()
        .fromAccountNumber(userToDebit.getAccountNumber())
        .toAccountNumber(userToCredit.getAccountNumber())
        .transactionType("TRANSFER")
        .amount(request.getAmount())
        .build();

        transactionService.saveTransaction(transactionDTO);

        return BankResponse.builder()
                           .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                           .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                           .accountinfo(null) 
                           .build(); 
    }

}

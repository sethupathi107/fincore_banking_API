package com.myself.bank_management_system.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myself.bank_management_system.dto.AccountInfo;
import com.myself.bank_management_system.dto.BankResponse;
import com.myself.bank_management_system.dto.CreditDebitRequest;
import com.myself.bank_management_system.dto.EmailDetails;
import com.myself.bank_management_system.dto.UserRequest;
import com.myself.bank_management_system.entity.User;
import com.myself.bank_management_system.repository.UserRepository;
import com.myself.bank_management_system.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

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
                           .AlternatePhoneNumber(userRequest.getAlternatePhoneNumber())
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
        // emailService.sendEmailAlert(emailDetails);
        
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

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());

        if(userToCredit.getAccountBalance().compareTo(request.getAmount()) < 0){
            return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE)
                        .accountinfo(null) 
                        .build(); 
        }

        userToCredit.setAccountBalance(userToCredit.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(userToCredit);

        return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                    .accountinfo(AccountInfo.builder()
                                            .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+userToCredit.getOtherName())
                                            .accountBalance(userToCredit.getAccountBalance())
                                            .accountNumber(userToCredit.getAccountNumber())
                                            .build()) 
                    .build(); 
    }

}

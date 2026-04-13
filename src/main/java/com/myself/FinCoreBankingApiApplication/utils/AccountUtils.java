package com.myself.FinCoreBankingApiApplication.utils;

import java.time.Year;

public class AccountUtils {


    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="This user is Already has an Account Created";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE="002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE="Account has been successfully Created";

    public static final String ACCOUNT_NOT_EXIST_CODE="003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE="User with that account Number does not Exist";

    public static final String ACCOUNT_FOUND_CODE="004";
    public static final String ACCOUNT_FOUNT_MESSAGE="User with that account Number Found";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE="005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE="Money Successfully Credited to your Account";

    public static final String ACCOUNT_INSUFFICIENT_BALANCE_CODE="006";
    public static final String ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE="Insufficient Balance";

    public static final String ACCOUNT_DEBIT_SUCCESS_CODE="007";
    public static final String ACCOUNT_DEBIT_SUCCESS_MESSAGE="Money Successfully Debited from your Account";

    public static final String TRANSFER_SUCCESSFUL_CODE="008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE="Transfer Successful";


    public static String randomNumberGenerator(){
        String year = String.valueOf(Year.now());
        String random = String.valueOf((int)Math.floor(Math.random()*1000000));
        return new StringBuilder().append(year).append(random).toString();
    }
}
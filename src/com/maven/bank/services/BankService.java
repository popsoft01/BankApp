package com.maven.bank.services;

public class BankService {
    private static long currentBVN = 0;
    private static long currentAccountNumber = 0;
    public static long generateBvn(){
        currentBVN++;
        return currentBVN;
    }

    public static long generateAccountNumber(){
        currentAccountNumber++;
        return currentAccountNumber;

    }
}

package com.maven.bank.engines;

import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.enums.LoanRequestStatus;
import com.maven.bank.enums.LoanType;
import com.maven.bank.entities.Account;
import com.maven.bank.entities.Customer;
import com.maven.bank.entities.LoanRequest;
import com.maven.bank.entities.SavingsAccount;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankLoanException;
import com.maven.bank.services.AccountService;
import com.maven.bank.services.AccountServiceImpl;
import com.maven.bank.services.BankService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanEngineByCustomerAgeTest {
    private LoanRequest johnLoanRequest;
    LoanEngine loanEngine;
    private AccountService accountService;
    private Customer john;

    @BeforeEach
    void setUp() throws MavenBankException {
        accountService = new AccountServiceImpl( );
        loanEngine = new LoanEngineByCustomerAge();

        johnLoanRequest = new LoanRequest ();
        johnLoanRequest.setApplyDate (LocalDateTime.now());
        johnLoanRequest.getInterestRate (0.1);
        johnLoanRequest.setStatus (LoanRequestStatus.NEW);
        johnLoanRequest.setTenor (25);
        johnLoanRequest.setTypeOfLoan (LoanType.SME);
        Optional<Customer> optionalCustomer = CustomerRepo.getCustomers ().values ().stream ().findFirst ();
        john = optionalCustomer.orElse(null);
        assertNotNull (john);
    }

    @AfterEach
    void tearDown() {
        BankService.reset ();
        CustomerRepo.reset ();
        LocalDate dob = LocalDate.of(1991, Month.MAY, 12);
        john.setDateOfBirth(dob);
    }

    @Test
    void approveLoanRequestWithoutCustomer(){

        assertThrows (MavenBankLoanException.class,
                () -> loanEngine.calculateAmountAutoApproved (null, new SavingsAccount(  ))) ;
    }

    @Test
    void approveLoanRequestWithNullAccount(){
        assertThrows (MavenBankLoanException.class, () -> loanEngine.calculateAmountAutoApproved (john, null));
    }

    @Test
    void calculateAmountAutoApprovedForBelowEighteen(){
        try{
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            LocalDate dob = john.getDateOfBirth().plusYears(7);
            john.setDateOfBirth(dob);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (BigDecimal.ZERO.intValue(), amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }


    @Test
    void calculateAmountAutoApprovedForTwentyOne(){
        try{
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (1009000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedForTwentyFiveToThirty(){
        try{
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            LocalDate dob = john.getDateOfBirth().minusYears(7);
            john.setDateOfBirth(dob);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (2018000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedForThirty0neToForty(){
        try{
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            LocalDate dob = john.getDateOfBirth().minusYears(12);
            john.setDateOfBirth(dob);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (3027000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedForForty0neToFiftyFive(){
        try{
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            LocalDate dob = john.getDateOfBirth().minusYears(27);
            john.setDateOfBirth(dob);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (5045000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedForFiftySixToSixtyFive(){
        try{
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            LocalDate dob = john.getDateOfBirth().minusYears(27);
            john.setDateOfBirth(dob);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (5045000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

}

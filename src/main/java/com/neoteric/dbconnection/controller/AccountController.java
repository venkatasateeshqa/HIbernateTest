package com.neoteric.dbconnection.controller;
import com.neoteric.dbconnection.exception.AccountCreationFailedException;
import com.neoteric.dbconnection.model.Account;
import com.neoteric.dbconnection.service.AccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class AccountController {

    @GetMapping(value="/api/searchAccount/jpa",
            consumes = "application/json",
            produces = "application/json")
    public Account searchAccountUsingJpa(@RequestHeader (name = "accountinput")
                                         String accountNumber) {
        AccountService accountService=new AccountService();
        return accountService.searchAccountByJpa(accountNumber);
    }

    @PostMapping(value = "/api/createAccount/jpa", consumes = "application/json", produces = "application/json")
    public Account createAccountUsingJPA(@RequestBody Account account) throws AccountCreationFailedException {

        AccountService accountService=new AccountService();
         String accountNumber= accountService.createAccountByJpa(account);
         account.setAccountNumber(accountNumber);
          return account;

    }

    @PostMapping(value = "/api/createAccount",consumes = "application/json",
            produces = "application/json")
    public Account getAccountNumber(@RequestBody Account account) throws AccountCreationFailedException {
        AccountService accountService=new AccountService();
        String accountNumber=accountService.createAccount(account);
        account.setAccountNumber(accountNumber);
        return account;
    }

    @PostMapping(value = "/api/createAccount/ui",consumes = "application/json",
            produces = "application/json")
    public Account oneToManyUsingUI(@RequestBody Account account){
        AccountService accountService=new AccountService();
        String accountNumber=accountService.oneToManyUsingUI(account);
        account.setAccountNumber(accountNumber);
        return account;
    }

    @GetMapping(value = "/api/searchAccount",consumes = "application/json",
            produces = "application/json")
    public Account searchAccount(@RequestHeader("accountinput") String accountNumber){
       AccountService accountService=new AccountService();
      return accountService.searchAccount(accountNumber);

    }
}

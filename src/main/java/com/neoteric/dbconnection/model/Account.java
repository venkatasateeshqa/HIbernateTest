package com.neoteric.dbconnection.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Account {

    private String name;
    private String accountNumber;
    private String pan;
    private String mobileNumber;
    private double balance;
    private Address address;

}

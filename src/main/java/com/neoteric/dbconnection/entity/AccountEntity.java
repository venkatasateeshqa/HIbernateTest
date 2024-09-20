package com.neoteric.dbconnection.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "account",schema = "bank")

@Data
public class AccountEntity {

    public AccountEntity() {
    }

    @Column(name = "name")
    private String name;
    @Id
    @Column(name = "accountnumber")
    private String accountNumber;
    @Column(name = "pan")
    private String pan;
    @Column(name = "mobile")
    private String mobileNumber;
    @Column(name = "balance")
    private double balance;

    @OneToMany(mappedBy = "accountEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AccountAddressEntity> accountAddressEntitiesList;


}

package com.neoteric.dbconnection.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "address",schema = "aadharcard")
@Data
public class AddressEntity {
    public AddressEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;

    @Column(name = "state")
    public String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aadharnumber")
    public AadharEntity aadharEntity;

}

package com.neoteric.dbconnection.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "aadhar",schema = "aadharcard")
@Data
public class AadharEntity {

    public AadharEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aadharnumber")
    public  Integer aadharNumber;

    @Column(name = "name")
    public String name;

    @OneToMany(mappedBy = "aadharEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AddressEntity> addressEntities;

}

package com.works.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Suppliers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suid", nullable = false)
    private Integer suid;

    private String suname;
    private String sumail;
    private String sutel;
    private Integer suactive;
    private Integer sudebt;


}

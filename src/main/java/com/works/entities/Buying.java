package com.works.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Buying {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid", nullable = false)
    private Integer bid;

    private String tname;
    private String pname;
    private String bnote;
    private int proprice;
    private int proamount;
    private int probirim;
    private int taxtype;
    private int pdiscount;
    private int purtype;
    private int btotalPrice;
    private int bnetPrice;
    private int bgrossPrice;
    private int bfaturaNo;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buyingDate;

    private int status;
}

package com.works.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sid", nullable = false)
    private Integer sid;

    private boolean status;
   private String cname;
   private String pname;
   private int faturaNo;
   private int pprice;

   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date saleDate;

   @Column(length = 250)
   private String note;

   private int totalPrice;
   private int pamount;
   private int grossPrice;
   private int netPrice;
   private int proType;
   private int tax_type;
   private int pdiscount;
   private int purType;

}

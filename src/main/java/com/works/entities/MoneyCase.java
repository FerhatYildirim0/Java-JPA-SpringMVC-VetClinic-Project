package com.works.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class MoneyCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid", nullable = false)
    private Integer cid;

    private Integer cmoney;
    private Integer cbuysell; // 1 buy ekleme | 2 sell çıkarma

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date cdatenow;

    private String cpeople;
    private String cname;
    private String ccomment;
}

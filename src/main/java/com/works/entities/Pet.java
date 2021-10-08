package com.works.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid", nullable = false)
    private Integer pid;

    private String pname;

    private Integer pchip;
    private String pearnumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pbirthday;
    private Integer ptype;
    private Integer pspay;
    private String prace;
    private Integer pcolor;
    private Integer pgender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cid", nullable = false)
    private Customer cid;

}

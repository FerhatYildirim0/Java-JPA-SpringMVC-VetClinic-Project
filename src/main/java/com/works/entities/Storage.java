package com.works.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stid", nullable = false)
    private Integer stid;

    private Integer staction; // Add 1 - Update 2 - Sale 3
    private Date stdate;
    private Integer stchangeamount;
    private Integer stlastamount;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stvac")
    private Vaccine stvac;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stpro")
    private Product stpro;


}

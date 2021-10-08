package com.works.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Cure {
    @Id
    @Column(name = "curid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer curid;

    private String curtitle;
    private String curcomment;
    private Integer curdebt;

    @OneToOne
    private  Pet pet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curvac")
    private Vaccine curvac;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curpro")
    private Product curpro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curla")
    private Labor curla;


}

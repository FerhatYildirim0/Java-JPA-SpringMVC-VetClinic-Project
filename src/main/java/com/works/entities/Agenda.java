package com.works.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agid", nullable = false)
    private Integer agid;

    private String agtitle;
    private String agtext;
    private String agdate;
    private String agtime;

}

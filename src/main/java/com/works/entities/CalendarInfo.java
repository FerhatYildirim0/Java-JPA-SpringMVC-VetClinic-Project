package com.works.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CalendarInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid", nullable = false)
    private Integer cid;

    private String cname;
    private String ccolor;
    private String cbgColor;
    private String cdragBgColor;
    private String cborderColor;

}

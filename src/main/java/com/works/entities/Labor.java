package com.works.entities;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Labor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laid", nullable = false)
    private Integer laid;

    private String laname;
    private Integer lamoney;
}

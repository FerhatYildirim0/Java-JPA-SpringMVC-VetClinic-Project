package com.works.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name="vaccine")
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacid", nullable = false)
    private Integer vacid;

    @NotNull(message = "Name parametresi null olamaz!")
    @NotEmpty(message = "Bu alan boş olamaz!")
    @Length(min = 2, max = 150, message = "Name min = 2, max = 100")
    private String vacname;

    private Integer vacunit;
    private Integer vactype;
    private Integer vaccategory;
    private Integer vacsupplier;
    private String vacbarcode;
    private String vaccode;
    private Integer vacbuying;
    private Integer vacsales;
    private Integer vactax;
    private Integer vactaxincluded;
    private Integer vacstock;
    private Integer vacactive;
    private Integer vacpettype;
    private Integer vacrepeat;

    @OneToMany(mappedBy = "stvac", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Storage> storages;

}

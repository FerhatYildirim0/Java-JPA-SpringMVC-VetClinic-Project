package com.works.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prid", nullable = false)
    private Integer prid;

    @NotNull(message = "Name parametresi null olamaz!")
    @NotEmpty(message = "Bu alan boş olamaz!")
    @Length(min = 2, max = 150, message = "Name min = 2, max = 100")
    private String prname;

    private Integer prunit;
    private Integer prtype;
    private Integer prcategory;
    private Integer prsupplier;
    private String prbarcode;
    private String prcode;
    private Integer prbuying;
    private Integer prsales;
    private Integer prtax;
    private Integer practive;
    private Integer prtaxincluded;
    private Integer prstock;

    @OneToMany(mappedBy = "stpro", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Storage> storages;


}

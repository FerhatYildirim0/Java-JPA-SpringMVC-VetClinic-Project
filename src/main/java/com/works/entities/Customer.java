package com.works.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cid", nullable = false)
    private Integer cid;

    @NotNull(message = "Name parametresi null olamaz!")
    @NotEmpty(message = "Bu alan boş olamaz!")
    @Length(min = 2, max = 100, message = "Name min = 2, max = 100")
    private String cname;

    @NotNull(message = "Surname parametresi null olamaz!")
    @NotEmpty(message = "Bu alan boş olamaz!")
    private String csurname;

    private String ctel;
    private String ctel_2;

    @NotNull(message = "Email parametresi null olamaz!")
    @NotEmpty(message = "Bu alan boş olamaz!")
    @Email(message = "E-Mail formatı hatalı!")
    private String cmail;

    private String ctax_office;
    private String cnote;

    private Integer ccitizenship;
    private Integer cdiscount;
    private String ccity;
    private String ctown;
    private String caddress;

    private String cdate;
    private Integer cdebpt;

    @OneToMany(mappedBy = "cid", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Pet> pets;
}

package com.works.controllers;

import com.works.entities.Customer;
import com.works.entities.Pet;
import com.works.projections.PetCustomerProjection;
import com.works.repositories.CustomerRepository;
import com.works.repositories.PetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    final CustomerRepository cRepo;
    final PetRepository pRepo;

    public CustomerController(CustomerRepository cRepo, PetRepository pRepo) {
        this.cRepo = cRepo;
        this.pRepo = pRepo;
    }

    // Customer listeleme kısmı
    @GetMapping("")
    public String customer(Model model) {
        model.addAttribute("customer", new Customer());

        List<PetCustomerProjection> list = cRepo.findByAllIgnoreCaseOrderByCidDesc();
/*        list.forEach( item -> {
            System.out.println(item.getCname());
        });*/
        model.addAttribute("ls",list);

        return "customer";
    }



    // Customer ekleme kısmı
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult) {
        if ( !bindingResult.hasErrors() ) {
            System.out.println(customer);
            customer.setCdate(new Date().toString());
            customer.setCdebpt(0);
            cRepo.save(customer);
            return "redirect:/customer";
        }
        return "customer";
    }



 /*  @GetMapping("/delete/${cidx}")
    public String deleteCu(@PathVariable String cidx){
        Integer cu_id = Integer.parseInt(cidx);

        return "customer";
    }*/



    @PostMapping("/petadd")
    public String petadd(Pet pet) {

        System.out.println(pet);


        pRepo.save(pet);
        return "redirect:/customer";
    }

}

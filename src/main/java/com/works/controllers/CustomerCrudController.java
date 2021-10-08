package com.works.controllers;

import com.works.entities.Customer;
import com.works.projections.PetCustomerProjection;
import com.works.repositories.CustomerRepository;
import com.works.repositories.PetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("customer-list")
public class CustomerCrudController {

    final CustomerRepository cRepo;
    final PetRepository pRepo;

    public CustomerCrudController(CustomerRepository cRepo, PetRepository pRepo) {
        this.cRepo = cRepo;
        this.pRepo = pRepo;
    }

    @GetMapping("")
    public String crud(Model model){
        model.addAttribute("customer", new Customer());

      /*  List<Customer> list = cRepo.findByCnameContains("a");
        list.forEach( item -> {
            System.out.println(item.getCname());
        });*/


        /*  model.addAttribute("ls",list);*/
        return "customer-list";
    }


    @GetMapping("/list")
    public String customerList(Model model) {
        model.addAttribute("customer", new Customer());

        List<PetCustomerProjection> list = cRepo.findByAllIgnoreCaseOrderByCidDesc();
/*        list.forEach( item -> {
            System.out.println(item.getCname());
        });*/
        model.addAttribute("sCust",list);
        return "customer-list";
    }

    @GetMapping("/list/{index}")
    public String customerLists(Model model,@PathVariable String index) {
        int page =Integer.parseInt(index);
        Pageable pagex = PageRequest.of(page, 5);
        long pageTotal=cRepo.countByCidAllIgnoreCase();

        List<Customer> list = cRepo.findByOrderByCidAsc(pagex);
/*        list.forEach( item -> {
            System.out.println(item.getCname());
        });*/


        model.addAttribute("sCust",list);
        return "customer-list";
    }




    @GetMapping("/cDelete/{c}")
    public String del(@PathVariable String c){
        Integer cu_id = Integer.parseInt(c);

        try {
            cRepo.deleteById(cu_id);
        } catch (Exception e) {
            System.out.println("Catche düsştü "+e);
        }
        return "redirect:/customer-list";

    }


    @GetMapping("/cUpdate/{sUptade}")
    public String updateCu(@PathVariable String sUptade){
        int cid=Integer.parseInt(sUptade);
        PetCustomerProjection cust=cRepo.findByCidEquals(cid);
        PetCustomerProjection c= cust;
        /* ; */

 /*       List<PetProjection> ls= pRepo.findByCid_CidIsAllIgnoreCase(c);
    System.out.println("-----"+ls);*/
        System.out.println("putade edilecek customer"+c.getCmail()+"--"+c.getCname()+"--"+c.getCsurname());
        /*  cRepo.saveAndFlush(c);*/
        return"redirect:/customer-list";
    }

   /* @GetMapping("/list/keyword")
    public String liveSearch(@RequestParam("keyword") String keyword, Model model){

        if (keyword != null) {
            List<Customer> list = cRepo.findByCnameContains(keyword);
            model.addAttribute("sCust",list);
            System.out.println("İFE GİRDİ"+keyword);
        }else {
            List<Customer> customerList=cRepo.findAll();
            model.addAttribute("sCust",customerList);
            System.out.println("ELSE GİRDİ"+keyword);
        }
        return "customer-list";
    }*/


}

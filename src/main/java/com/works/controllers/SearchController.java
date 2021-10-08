package com.works.controllers;

import com.works.entities.Customer;
import com.works.repositories.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("customer-search")
public class SearchController {
    final CustomerRepository cRepo;

    public SearchController(CustomerRepository cRepo) {
        this.cRepo = cRepo;
    }

    @GetMapping("")
    public String vie(){
        return "customer-search";
    }

    @GetMapping("/keyword")
    public String viewHomePage(Model model, @RequestParam("keyword") String keyword) {
        List<Customer> listProducts = cRepo.findByCnameContains(keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("keyword", keyword);

        return "customer-search";
    }

}

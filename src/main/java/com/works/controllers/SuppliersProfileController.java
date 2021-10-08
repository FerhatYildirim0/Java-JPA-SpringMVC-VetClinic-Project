package com.works.controllers;

import com.works.entities.Buying;
import com.works.entities.Suppliers;
import com.works.repositories.BuyingRepository;
import com.works.repositories.SuppliersRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/suppliers-profile")
public class SuppliersProfileController {

    final SuppliersRepository supRepo;
    final BuyingRepository buyRepo;

    private Integer supid;

    public SuppliersProfileController(SuppliersRepository supRepo, BuyingRepository buyRepo) {
        this.supRepo = supRepo;
        this.buyRepo = buyRepo;
    }

    @GetMapping("")
    public String supProfile(){

        return "suppliers-profile";
    }

    @GetMapping("/{index}")
    private String suppliersProfile(@PathVariable String index, Model model) {
        supid = Integer.parseInt(index);
        model.addAttribute("suppliers", new Suppliers());
        Optional<Suppliers> sup = supRepo.findById(supid);
        model.addAttribute("sup", sup.get());

        model.addAttribute("buyings", new Buying());
        String str_sup=String.valueOf(supid);//Now it will return "10"

        List<Buying> buyingList = buyRepo.findByTnameEqualsAndStatusEqualsOrderByBidDesc(str_sup, 2);
        model.addAttribute("buyingList", buyingList);

        buyingList.forEach( item -> {
            System.out.println("------------" + item.getBfaturaNo());
        });

        return "suppliers-profile";
    }

    // Suppliers Update
    @PostMapping("/update/{index}")
    private String suppiliersVac (Suppliers suppliers, String index ) {

        suppliers.setSuid(supid);
        supRepo.saveAndFlush(suppliers);

        return "redirect:/suppliers-profile/" + supid;
    }

    // Debt
    @PostMapping("/debt")
    private String debt(String sudebt) {

        int sdebt = Integer.parseInt(sudebt);
        Suppliers suppliers = supRepo.findById(supid).get();
        suppliers.setSudebt(suppliers.getSudebt()-sdebt);
        supRepo.saveAndFlush(suppliers);

        return "redirect:/suppliers-profile/" + supid;
    }




}

package com.works.controllers;

import com.works.entities.Suppliers;
import com.works.repositories.SuppliersRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/suppliers")
public class SuppliersController {

    final SuppliersRepository supRepo;

    public SuppliersController(SuppliersRepository supRepo) {
        this.supRepo = supRepo;
    }

    @GetMapping("")
    public String suppliers(Model model) {

        model.addAttribute("suppliers", new Suppliers() );
        List<Suppliers> listSuppliers = supRepo.findByOrderBySuidDesc();
        model.addAttribute("listSuppliers", listSuppliers);

        return "suppliers";
    }


    @PostMapping("/add")
    public String addSup( Suppliers suppliers) {
        try{
            supRepo.save(suppliers);
        } catch (Exception ex) {
            System.out.println("Hata: " + ex);
        }
        return "redirect:/suppliers";
    }

    @GetMapping("/delete/{sup}")
    public String delete(@PathVariable String sup) {
        Integer supid = Integer.parseInt(sup);
        try{
            supRepo.deleteById(supid);
        } catch (Exception ex) {
            System.out.println("Hata: " + ex);
        }
        return "redirect:/suppliers";
    }

}

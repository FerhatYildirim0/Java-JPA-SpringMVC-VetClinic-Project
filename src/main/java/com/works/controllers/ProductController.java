package com.works.controllers;

import com.works.entities.Product;
import com.works.entities.Storage;
import com.works.entities.Suppliers;
import com.works.repositories.ProductRepository;
import com.works.repositories.StorageRepository;
import com.works.repositories.SuppliersRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    final ProductRepository prRepo;
    final StorageRepository strRepo;
    final SuppliersRepository supRepo;

    public ProductController(ProductRepository prRepo, StorageRepository strRepo, SuppliersRepository supRepo) {
        this.prRepo = prRepo;
        this.strRepo = strRepo;
        this.supRepo = supRepo;
    }

    @GetMapping("")
    public String product( Model model){

        model.addAttribute("product", new Product() );
        List<Product> list = prRepo.findByAllIgnoreCaseOrderByPridDesc();
        model.addAttribute("ls", list);

        model.addAttribute("supplier", new Suppliers());
        List<Suppliers> suppliersList = supRepo.findByOrderBySuidDesc();
        model.addAttribute("suppliersList",suppliersList);

        return "product";
    }

    @GetMapping("/prdelete/{pr}")
    public String delete(@PathVariable String pr) {
        Integer pr_id = Integer.parseInt(pr);
        try{
            prRepo.deleteById(pr_id);
        } catch (Exception ex) {
            System.out.println("Hata: " + ex);
        }
        return "redirect:/product";
    }

    @PostMapping("/add")
    public String add(Product product) {
        System.out.println(product);
        prRepo.save(product);

        try {
            Storage storage = new Storage();
            Date date = new Date();
            List<Product> productList = prRepo.findByAllIgnoreCaseOrderByPridDesc();
            int last_id = productList.size() +1 ;
            Optional<Product> product1 = prRepo.findById(last_id);

            storage.setStdate(date);
            storage.setStaction(1);
            storage.setStchangeamount(product1.get().getPrstock());
            storage.setStlastamount(product1.get().getPrstock());
            storage.setStpro(product1.get());

            strRepo.save(storage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/product";
    }


}

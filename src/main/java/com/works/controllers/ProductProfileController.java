package com.works.controllers;

import com.works.entities.*;
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
@RequestMapping("product-profile")
public class ProductProfileController {

    final ProductRepository prRepo;
    final StorageRepository strRepo;
    final SuppliersRepository supRepo;

    private  Integer prid; // Storage index

    public ProductProfileController(ProductRepository prRepo, StorageRepository strRepo, SuppliersRepository supRepo) {
        this.prRepo = prRepo;
        this.strRepo = strRepo;
        this.supRepo = supRepo;
    }


    @GetMapping("")
    public String product() {

        return "product-profile";
    }

    // product profil bilgileri ve depo hareketleri
    @GetMapping("/{index}")
    private String productProfile(@PathVariable String index, Model model) {
        prid = Integer.parseInt(index);
        model.addAttribute("Product", new Product());
        Optional<Product> pr = prRepo.findById(prid);
        model.addAttribute("pr", pr.get());

        List<Storage> storageList =  strRepo.findByStproEqualsAllIgnoreCaseOrderByStidDesc(pr.get());
        model.addAttribute("storageList", storageList);

        model.addAttribute("supplier", new Suppliers());
        List<Suppliers> suppliersList = supRepo.findByOrderBySuidDesc();
        model.addAttribute("suppliersList",suppliersList);

        return "product-profile";
    }

    // Pr_select: if==1 -> ekleme | if==2 -> güncelleme
    @PostMapping("/stockadd")
    public String productStockAdd(ProductStockAdd productStockAdd) {
        Date date = new Date();
        Product pr = prRepo.findById(prid).get();

        Storage storage = new Storage();
        storage.setStpro(pr);
        storage.setStdate(date);
        productStockAdd.setPr_id(prid);

        if(productStockAdd.getPr_select() == 1) {
            Integer temp = pr.getPrstock() + productStockAdd.getPr_amount();
            pr.setPrstock(temp);
            storage.setStaction(1);
            storage.setStchangeamount(productStockAdd.getPr_amount());
            storage.setStlastamount(temp);
        } else if ( productStockAdd.getPr_select() == 2 ) {
            pr.setPrstock(productStockAdd.getPr_amount());
            storage.setStaction(2);
            storage.setStchangeamount(productStockAdd.getPr_amount());
            storage.setStlastamount(productStockAdd.getPr_amount());
        }
        System.out.println("Storage: " + storage);
        prRepo.saveAndFlush(pr);
        strRepo.save(storage);

        return "redirect:/product-profile/" + prid;
    }

    // Profile update
    @PostMapping("/update/{index}")
    private String updatePr ( Product product, String index ) {
        product.setPrid(prid);
        prRepo.saveAndFlush(product);

        return "redirect:/product-profile/" + prid;
    }


}

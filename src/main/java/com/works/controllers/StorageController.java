package com.works.controllers;

import com.works.entities.Storage;
import com.works.repositories.StorageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/storage")
public class StorageController {

    final StorageRepository strRepo;

    public StorageController(StorageRepository strRepo, VaccineController vacRepo, ProductController prRepo) {
        this.strRepo = strRepo;
    }


    @GetMapping("")
    private String storage( Model model) {
        List<Storage> storage = strRepo.findByOrderByStidDesc();
        model.addAttribute("storage", storage);

        List<Storage> storageList = strRepo.findByOrderByStdateDesc();
        model.addAttribute("storageList", storageList);

        return "storage";
    }
}

package com.works.controllers;

import com.works.entities.Storage;
import com.works.entities.Suppliers;
import com.works.entities.Vaccine;
import com.works.repositories.StorageRepository;
import com.works.repositories.SuppliersRepository;
import com.works.repositories.VaccineRepository;
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
@RequestMapping("/vaccine")
public class VaccineController {

    final VaccineRepository vacRepo;
    final StorageRepository strRepo;
    final SuppliersRepository supRepo;

    public VaccineController(VaccineRepository vacRepo, StorageRepository strRepo, SuppliersRepository supRepo) {
        this.vacRepo = vacRepo;
        this.strRepo = strRepo;
        this.supRepo = supRepo;
    }

    @GetMapping("")
    public String vaccine( Model model ){

        model.addAttribute("vaccine", new Vaccine());
        List<Vaccine> list = vacRepo.findByAllIgnoreCaseOrderByVacidDesc();

        model.addAttribute("supplier", new Suppliers());
        List<Suppliers> suppliersList = supRepo.findByOrderBySuidDesc();
        model.addAttribute("suppliersList",suppliersList);

        suppliersList.forEach( item -> {
            System.out.println( item.getSuname());
        });

        model.addAttribute("ls", list);
        return "vaccine";
    }

    @GetMapping("/vacdelete/{vac}")
    public String delete(@PathVariable String vac) {
        Integer vac_id = Integer.parseInt(vac);
        try{
            vacRepo.deleteById(vac_id);
        } catch (Exception ex) {
            System.out.println("Hata: " + ex);
        }
        return "redirect:/vaccine";
    }


    @PostMapping("/add")
    public String add(Vaccine vaccine) {

        try {
            System.out.println(vaccine);
            vacRepo.save(vaccine);

            Storage storage = new Storage();
            Date date = new Date();
            List<Vaccine> vaccineList = vacRepo.findByAllIgnoreCaseOrderByVacidDesc();
            int last_id = vaccineList.size() +1 ;
            Optional<Vaccine> vaccine1 = vacRepo.findById(last_id);

            storage.setStdate(date);
            storage.setStaction(1);
            storage.setStchangeamount(vaccine1.get().getVacstock());
            storage.setStlastamount(vaccine1.get().getVacstock());
            storage.setStvac(vaccine1.get());

            strRepo.save(storage);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/vaccine";
        }

        return "redirect:/vaccine";
    }


}

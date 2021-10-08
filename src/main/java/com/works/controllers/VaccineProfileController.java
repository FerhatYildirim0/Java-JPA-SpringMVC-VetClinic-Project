package com.works.controllers;

import com.works.entities.Storage;
import com.works.entities.Suppliers;
import com.works.entities.Vaccine;
import com.works.entities.VaccineStockAdd;
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
@RequestMapping("vaccine-profile")
public class VaccineProfileController {

    final VaccineRepository vacRepo;
    final StorageRepository strRepo;
    final SuppliersRepository supRepo;
    private Integer vacid; // Vaccine index

    public VaccineProfileController(VaccineRepository vacRepo, StorageRepository strRepo, SuppliersRepository supRepo) {
        this.vacRepo = vacRepo;
        this.strRepo = strRepo;
        this.supRepo = supRepo;
    }

    @GetMapping("")
    public String vac() {

        return "vaccine-profile";
    }

    // aşı profil bilgileri ve depo hareketleri
    @GetMapping("/{index}")
    private String vaccineProfile(@PathVariable String index, Model model) {
        vacid = Integer.parseInt(index);
        model.addAttribute("vaccine", new Vaccine());
        Optional<Vaccine> vac = vacRepo.findById(vacid);
        System.out.println(vac.get().getVacname()+"--");
        model.addAttribute("vac", vac.get());

        List<Storage> storageList =  strRepo.findByStvacEqualsAllIgnoreCaseOrderByStidDesc(vac.get());
        model.addAttribute("storageList", storageList);

        model.addAttribute("supplier", new Suppliers());
        List<Suppliers> suppliersList = supRepo.findByOrderBySuidDesc();
        model.addAttribute("suppliersList",suppliersList);

        Date date = new Date();
        System.out.println(date);
        return "vaccine-profile";
    }

    // Vac_select: if==1 -> ekleme | if==2 -> güncelleme
    @PostMapping("/stockadd")
    private String vaccineStockAdd(VaccineStockAdd vaccineStockAdd) {
        Date date = new Date();
        Vaccine vac = vacRepo.findById(vacid).get();

        Storage storage = new Storage();
        storage.setStvac(vac);
        storage.setStdate(date);
        vaccineStockAdd.setVac_id(vacid);

        if(vaccineStockAdd.getVac_select() == 1) {
            Integer temp = vac.getVacstock() + vaccineStockAdd.getVac_amount();
            vac.setVacstock(temp);
            storage.setStaction(1);
            storage.setStchangeamount(vaccineStockAdd.getVac_amount());
            storage.setStlastamount(temp);
        } else if ( vaccineStockAdd.getVac_select() == 2 ) {
            vac.setVacstock(vaccineStockAdd.getVac_amount());
            storage.setStaction(2);
            storage.setStchangeamount(vaccineStockAdd.getVac_amount());
            storage.setStlastamount(vaccineStockAdd.getVac_amount());
        }
        vacRepo.saveAndFlush(vac);
        strRepo.save(storage);

        return "redirect:/vaccine-profile/" + vacid;
    }

    // Vaccine update
    @PostMapping("/update/{index}")
    private String updateVac ( Vaccine vaccine, String index ) {
        vaccine.setVacid(vacid);
        vacRepo.saveAndFlush(vaccine);

        return "redirect:/vaccine-profile/" + vacid;
    }

}

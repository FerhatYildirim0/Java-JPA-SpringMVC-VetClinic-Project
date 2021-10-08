package com.works.controllers;

import com.works.entities.ScheduleCalendar;
import com.works.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/main")
public class MainPageController {

    final CustomerRepository cuRepo;
    final CaseRepository caRepo;
    final StorageRepository stRepo;
    final VaccineRepository vacRepo;
    final ProductRepository prRepo;
    final PetRepository petRepo;
    final CureRepository cureRepo;
    final ScheduleCalendarRepository calRepo;

    public MainPageController(CureRepository cureRepo, CustomerRepository cuRepo, CaseRepository caRepo, StorageRepository stRepo, VaccineRepository vacRepo, ProductRepository prRepo, PetRepository petRepo, ScheduleCalendarRepository calRepo) {
        this.cureRepo = cureRepo;
        this.cuRepo = cuRepo;
        this.caRepo = caRepo;
        this.stRepo = stRepo;
        this.vacRepo = vacRepo;
        this.prRepo = prRepo;
        this.petRepo = petRepo;
        this.calRepo = calRepo;
    }

    @GetMapping("")
    private String mainPage(Model model){

        String dateparse = new String();
        dateparse = String.valueOf(new Date().getDate());
        long cuCount = cuRepo.countByCdateLike(dateparse);
        model.addAttribute("customerCount", cuCount);


        long moneyCount1 = 0;
        try {
            moneyCount1 = caRepo.countByCdatenowLike1(dateparse);
        } catch (Exception e) {
            moneyCount1 = 0;
        }

        long moneyCount2 = 0;
        try {
            moneyCount2 = caRepo.countByCdatenowLike2(dateparse);
        } catch (Exception e) {
            moneyCount2 = 0;
        }

        model.addAttribute("money", moneyCount1 - moneyCount2);

        long storage = stRepo.sumlastamount();
        model.addAttribute("storage", storage);

        long totalVaccine = vacRepo.countByVacactiveEquals(1);
        model.addAttribute("totalVaccine", totalVaccine);

        long totalProduct = prRepo.countByPractiveEquals(1);
        model.addAttribute("totalProduct", totalProduct);

        long totalPet = petRepo.totalpet();
        model.addAttribute("totalPet", totalPet);

        long totallab = cureRepo.totallab();
        model.addAttribute("totallab", totallab);

        List<ScheduleCalendar> scheduleCalendars = calRepo.findByOrderBySidDesc();
        model.addAttribute("scheduleCalendars", scheduleCalendars);


        return "main-page";
    }

}

package com.works.controllers;

import com.works.entities.MoneyCase;
import com.works.repositories.CaseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Controller
@RequestMapping("/case")
public class CaseController {

    final CaseRepository caRepo;

    public CaseController(CaseRepository caRepo) {
        this.caRepo = caRepo;
    }


    @GetMapping("")
    private String casec( Model model){

        List<MoneyCase> moneyCaseList = caRepo.findByOrderByCidDesc();
        model.addAttribute("case", moneyCaseList);

        return "case";
    }

    @PostMapping("/add")
    private String addCase(MoneyCase moneyCase) {

        Authentication aut = SecurityContextHolder.getContext().getAuthentication();
        String email = aut.getName(); // username

        moneyCase.setCbuysell(1);
        moneyCase.setCname(email);
        caRepo.save(moneyCase);


        return "redirect:/case";
    }

    @PostMapping("/remove")
    private String removeCase(MoneyCase moneyCase) {

        Authentication aut = SecurityContextHolder.getContext().getAuthentication();
        String email = aut.getName(); // username

        moneyCase.setCbuysell(2);
        moneyCase.setCname(email);
        caRepo.save(moneyCase);

        return "redirect:/case";
    }

}

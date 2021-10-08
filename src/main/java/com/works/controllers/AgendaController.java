package com.works.controllers;

import com.works.entities.Agenda;
import com.works.repositories.AgendaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/agenda")
public class AgendaController {

    final AgendaRepository agRepo;
    private Integer agid;

    public AgendaController(AgendaRepository agRepo) {
        this.agRepo = agRepo;
    }

    @GetMapping("")
    public String agenda(Model model) {

        Agenda agnull = new Agenda();
        model.addAttribute("ag", agnull);
        model.addAttribute("agenda", new Agenda());
        List<Agenda> agendaList = agRepo.findByOrderByAgidDesc();
        model.addAttribute("agendaList", agendaList);
        return "agenda";
    }

    @GetMapping("/{index}")
    public String agenda(@PathVariable String index, Model model) {
        agid = Integer.parseInt(index);
        Optional<Agenda> ag = agRepo.findById(agid);
        model.addAttribute("ag", ag.get());

        model.addAttribute("agenda", new Agenda());
        List<Agenda> agendaList = agRepo.findByOrderByAgidDesc();
        model.addAttribute("agendaList", agendaList);
        return "agenda";
    }

    @PostMapping("/add")
    public String agendaAdd(Agenda agenda) {

        System.out.println(agenda.getAgdate() + " " +agenda.getAgtime());
        agRepo.saveAndFlush(agenda);

        return "redirect:/agenda";
    }

    @GetMapping("/delete/{ag}")
    public String delete(@PathVariable String ag) {
        Integer ag_id = Integer.parseInt(ag);
        try{
            agRepo.deleteById(ag_id);
        } catch (Exception ex) {
            System.out.println("Hata: " + ex);
        }
        return "redirect:/agenda";
    }
}

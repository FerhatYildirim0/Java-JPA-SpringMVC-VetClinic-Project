package com.works.controllers;

import com.works.entities.User;
import com.works.properites.PassChange;
import com.works.repositories.UserRepository;
import com.works.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;


@Controller
public class PassChangeController {
    final UserRepository uRepo;
    final UserService uService;
    final UserRepository userRepository;

    public PassChangeController(UserRepository uRepo, UserService uService, UserRepository userRepository) {
        this.uRepo = uRepo;
        this.uService = uService;
        this.userRepository = userRepository;
    }

    @GetMapping("/passChange")
    public String passchangex(){

    return "passChange";
    }



    @PostMapping("/passchanger")
    public String passChanger(PassChange passChange){

      UserDetails userDetails =  uService.loadUserByUsername(passChange.getEmail());
        if (userDetails.getUsername().equals(passChange.getEmail())){
            System.out.println("İFİN İÇİ -------------------------------------------------------------------------------");
            System.out.println(userDetails);
           String email=passChange.getEmail();
            Optional<User> oUser = uRepo.findByEmailEqualsAllIgnoreCase(email);
            User us=oUser.get();
            us.setPassword( uService.encoder().encode(passChange.getNewPass()));
            userRepository.saveAndFlush(us);
        }

        return "redirect:/logout";
    }



}

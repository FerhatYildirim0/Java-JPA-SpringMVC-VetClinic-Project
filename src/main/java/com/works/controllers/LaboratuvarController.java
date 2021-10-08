package com.works.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/laboratuvar")
public class LaboratuvarController {

    @GetMapping("")
    public  String lab(){


        return "laboratuvar";
    }
}

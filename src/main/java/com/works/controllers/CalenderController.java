package com.works.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calender")
public class CalenderController {

    @GetMapping("")
    private String calender(){

        return "calender";
    }

}

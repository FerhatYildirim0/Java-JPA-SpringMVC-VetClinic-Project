package com.works.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("calender1")
public class CalenderFrame {
     @GetMapping("")
    private String calender12(){


         return "calender1";
     }
}

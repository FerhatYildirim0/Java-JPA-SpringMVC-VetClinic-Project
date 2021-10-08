package com.works.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VettAppcontroller {

    @GetMapping("content")
    public String sidebar(){

        return "content";
    }

}

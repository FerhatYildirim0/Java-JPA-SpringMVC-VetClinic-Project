package com.works.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //@GetMapping("/logout")
    //public String logout(HttpServletRequest req, HttpServletResponse res) {

        /*
        HttpSession session= req.getSession(false);
        SecurityContextHolder.clearContext();
        session= req.getSession(false);
        if(session != null) {
            session.invalidate();
        }

        if ( req.getCookies() != null ) {
            for (Cookie cookie : req.getCookies()) {
                cookie.setMaxAge(0);
                res.addCookie(cookie);
            }
        }
        */

       // return "redirect:/login";
    //}

}

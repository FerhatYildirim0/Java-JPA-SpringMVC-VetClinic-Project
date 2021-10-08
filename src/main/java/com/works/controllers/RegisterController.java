package com.works.controllers;

import com.works.entities.Role;
import com.works.entities.User;
import com.works.repositories.RoleRepository;
import com.works.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegisterController {

    final UserService uSevice;
    final RoleRepository rRepo;
    public RegisterController(UserService uSevice, RoleRepository rRepo) {
        this.uSevice = uSevice;
        this.rRepo = rRepo;
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User us, @RequestParam(defaultValue = "1") String roleIDSt) {
        try {

            long roleID = Long.parseLong(roleIDSt);
            us.setEnabled(true);
            us.setTokenExpired(true);

            Role role = rRepo.findById(roleID).get();

            List<Role> roles = new ArrayList<>();
            roles.add(role);

            us.setRoles(roles);

            uSevice.register(us);
            return "redirect:/login";
        }catch ( AuthenticationException ex ) {}
        return "register";
    }

}

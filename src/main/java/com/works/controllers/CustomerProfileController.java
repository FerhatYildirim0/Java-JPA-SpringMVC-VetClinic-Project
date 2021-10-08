package com.works.controllers;

import com.works.entities.Customer;
import com.works.entities.MoneyCase;
import com.works.entities.Pet;
import com.works.entities.Suppliers;
import com.works.repositories.CaseRepository;
import com.works.repositories.CustomerRepository;
import com.works.repositories.PetRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customerprofile")
public class CustomerProfileController {
    Customer customerUpdate=new Customer();
    final CustomerRepository cRepo;
    final PetRepository pRepo;
    final CaseRepository caRepo;

    public CustomerProfileController(CaseRepository caRepo, CustomerRepository cRepo, PetRepository pRepo) {
        this.cRepo = cRepo;
        this.caRepo = caRepo;
        this.pRepo = pRepo;
    }

    @GetMapping("")
    public String customerprofile(Model model){

        return "customerprofile";
    }

    @GetMapping("/cProfile/{index}")
    public String customer(@PathVariable String index, Model model){
        int cid=Integer.parseInt(index);
        model.addAttribute("customer", new Customer());
        Optional<Customer> c = cRepo.findById(cid);
        List<Pet> petList= pRepo.findByCid_CidEquals(cid);
        customerUpdate=cRepo.findById(cid).get();
        System.out.println(c.get().getCname()+"--- ---");

        petList.forEach(item->{
            System.out.println("petLİSSST"+item.getPname()+"---"+item.getCid().getCname());
        });

        Customer cu= cRepo.findById(cid).get();
        model.addAttribute("cust",cu);
        model.addAttribute("cst",c.get());
        model.addAttribute("petls",petList);
        return "customerprofile";
    }

    ///// securtiy den sonra
    @PostMapping ("/uProfile")
    public String uCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response){
        String contex= request.getContextPath();
        String referer = request.getHeader("Referer");

        try {
            customer.setCid(customerUpdate.getCid());

            if ( !bindingResult.hasErrors() ) {
                System.out.println(customer);
                cRepo.saveAndFlush(customer);
                customer=new Customer();
                return "redirect:/customer-list/list";
            }
        } catch (Exception e){

        }
        return "redirect:/customer-list/list";
    }



    @GetMapping("/cDelete/{c}")
    public String del(@PathVariable String c){
        Integer cu_id = Integer.parseInt(c);

        try {
            cRepo.deleteById(cu_id);
        } catch (Exception e) {
            System.out.println("E: "+e);
        }

        return "redirect:/customer-list/list";

    }


    @GetMapping("/petDel/{c}")
    public String petDel(@PathVariable String c){
        int petDel= Integer.parseInt(c);

        try {
            pRepo.deleteById(petDel);
        } catch (Exception e) {

        }

        return "redirect:/customer-list/list";
    }

    // Debt
    @PostMapping("/debt")
    private String customerdebt(String cdebpt) {

        int cdebpt1 = Integer.parseInt(cdebpt);
        Customer customer = cRepo.findById(customerUpdate.getCid()).get();
        customer.setCdebpt(customer.getCdebpt() - cdebpt1);
        cRepo.saveAndFlush(customer);

        Authentication aut = SecurityContextHolder.getContext().getAuthentication();
        String email = aut.getName(); // username
        MoneyCase moneyCase = new MoneyCase();
        moneyCase.setCmoney(cdebpt1);
        moneyCase.setCbuysell(1);
        moneyCase.setCdatenow(new Date());
        moneyCase.setCname(email);
        moneyCase.setCpeople(customer.getCname());
        moneyCase.setCcomment("Tedavi Ödemesi");
        caRepo.saveAndFlush(moneyCase);


        return "redirect:/customerprofile/cProfile/" + customer.getCid().toString();
    }


}

package com.works.controllers;

import com.works.entities.MoneyCase;
import com.works.entities.Sale;
import com.works.entities.Storage;
import com.works.entities.Customer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.works.repositories.CaseRepository;
import com.works.repositories.CustomerRepository;
import com.works.repositories.SaleRepository;
import com.works.repositories.StorageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/sale")
public class SaleController {

    final SaleRepository sRepo;
    final StorageRepository strRepo;
    final CustomerRepository cuRepo;
    final CaseRepository caRepo;

    public SaleController(SaleRepository sRepo, StorageRepository strRepo, CustomerRepository cuRepo, CaseRepository caRepo) {
        this.sRepo = sRepo;
        this.strRepo = strRepo;
        this.cuRepo = cuRepo;
        this.caRepo = caRepo;
    }

    @GetMapping("")
    public String sale( Model model){

        model.addAttribute("storage", new Storage());
        List<Storage> storageList = strRepo.findByOrderByStidDesc();
        model.addAttribute("storageList", storageList);

        model.addAttribute("customer", new Customer());
        List<Customer> customerList = cuRepo.findAll();
        model.addAttribute("customerList", customerList);

        return "sale";
    }

    @ResponseBody
    @GetMapping("/list")
    public List<Sale> list(){


        return sRepo.findByStatusEquals(false);
    }


    @ResponseBody
    @PostMapping("/nsert")
    public Sale saleInsert(@RequestBody Sale sale){

      try{ sale.setSaleDate(new Date());
          float tax=0.3f;
          sale.setGrossPrice(sale.getPamount()*sale.getPprice());
          sale.setNetPrice(sale.getGrossPrice()-sale.getGrossPrice()*sale.getPdiscount()/100);
          if(sale.getTax_type()==0){
              tax = 0;
          }
          else if(sale.getTax_type()==1){
              tax = 0.01f;
          }
          else if(sale.getTax_type()==2){
              tax = 0.08f;
          }
          else if(sale.getTax_type()==3){
              tax = 0.18f;
          }

          sale.setTotalPrice((int) (sale.getNetPrice()*(1+tax)));

          Sale s = sRepo.save(sale);

          Authentication aut = SecurityContextHolder.getContext().getAuthentication();
          String email = aut.getName(); // username

          // KASA
          MoneyCase casec = new MoneyCase();
          casec.setCmoney(sale.getTotalPrice());
          casec.setCbuysell(1);
          casec.setCdatenow(new Date());
          casec.setCpeople(sale.getCname());
          casec.setCcomment("Satış");
          casec.setCname(email);
          caRepo.save(casec);

          return s;
      }
      catch (Exception ex){
          System.err.println("Sipariş ekleme sırasında hata meydana geldi :  " + ex);
      }



     return null;
    }

    @ResponseBody
    @DeleteMapping("/sDelete/{sidx}")
    public String sDelete(@PathVariable String sidx){
        String status = "0";
        try{
            int sid = Integer.parseInt(sidx);
            sRepo.deleteById(sid);

            status = "1";
        } catch (Exception ex){
            System.err.println("Silme işlemi sırasında hata oluştu : " + ex);
        }
        return status;
    }

    @ResponseBody
    @GetMapping("/sChange/{scfat}")
    public String schange(@PathVariable String scfat){
        String status = "0";
        try{
            int faturaNo = Integer.parseInt(scfat);
            List<Sale> saleList = sRepo.getByFaturaNoEquals(faturaNo);

            for (Sale sale: saleList){

                sale.setStatus(true);
            }

            sRepo.saveAllAndFlush(saleList);

           status = "1";
        }

        catch (Exception ex){
            System.err.println("Status değiştirme sırasında hata " + ex);
        }



        return status;
    }



}

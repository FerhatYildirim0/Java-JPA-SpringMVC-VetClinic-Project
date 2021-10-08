package com.works.controllers;


import com.works.entities.Buying;
import com.works.entities.MoneyCase;
import com.works.entities.Storage;
import com.works.entities.Suppliers;
import com.works.repositories.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/buying")
public class BuyingController {

    final BuyingRepository bRepo;
    final SuppliersRepository supRepo;
    final StorageRepository strRepo;
    final VaccineRepository vacRepo;
    final ProductRepository proRepo;
    final CaseRepository caRepo;

    public BuyingController(BuyingRepository bRepo, SuppliersRepository supRepo, StorageRepository strRepo, VaccineRepository vacRepo, ProductRepository proRepo, CaseRepository caRepo) {
        this.bRepo = bRepo;
        this.supRepo = supRepo;
        this.strRepo = strRepo;
        this.vacRepo = vacRepo;
        this.proRepo = proRepo;
        this.caRepo = caRepo;
    }

    @GetMapping("")
    public String buying(Model model){

        model.addAttribute("supplier", new Suppliers());
        List<Suppliers> suppliersList = supRepo.findByOrderBySuidDesc();
        model.addAttribute("suppliersList",suppliersList);

        model.addAttribute("storage", new Storage());
        List<Storage> storageList = strRepo.findByOrderByStidDesc();
        model.addAttribute("storageList", storageList);

        return "buying";
    }

    @ResponseBody
    @GetMapping("/blist")
    public List<Buying> blist(){


        return bRepo.findByStatusEquals(0);
    }

    @ResponseBody
    @PostMapping("/binsert")
    public Buying bInsert(@RequestBody Buying buying){

        try {

            float tax=0.3f;
            buying.setBuyingDate(new Date());
            buying.setBgrossPrice(buying.getProprice()*buying.getProamount());
            buying.setBnetPrice(buying.getBgrossPrice()-buying.getBgrossPrice()*buying.getPdiscount()/100);
            buying.setStatus(0);

            if(buying.getTaxtype()==0){
                tax = 0;
            }
            else if(buying.getTaxtype()==1){
                tax = 0.01f;
            }
            else if(buying.getTaxtype()==2){
                tax = 0.08f;
            }
            else if(buying.getTaxtype()==3){
                tax = 0.18f;
            }

            buying.setBtotalPrice((int) (buying.getBnetPrice()*(1+tax)));

             Buying b = bRepo.saveAndFlush(buying);
            return b;
        } catch (Exception e) {

            System.err.println("Alım yapılırken hata oluştu: " + e);

        }
        return null;
    }
    @ResponseBody
    @DeleteMapping("/bDelete/{bidx}")
    public String sDelete(@PathVariable String bidx){
        String status = "0";
        try{
            int bid = Integer.parseInt(bidx);
            Buying dObject = bRepo.findByBidEquals(bid);

            List<Buying> dlist = bRepo.getByBfaturaNoEquals(dObject.getBfaturaNo());

          //  bRepo.deleteAll(dlist);

            for(Buying ditem : dlist){
                bRepo.delete(ditem);
            }

            status = "1";
        } catch (Exception ex){
            System.err.println("Silme işlemi sırasında hata oluştu : " + ex);
        }
        return status;
    }

    @ResponseBody
    @GetMapping("/sChanger/{scfatno}")
    public String schanger(@PathVariable String scfatno ){
        String status = "0";

        try{
            Buying buyingObject = new Buying();

            int objGrossPrice = buyingObject.getBgrossPrice();
            int objNetPrice   = buyingObject.getBnetPrice();
            int objTotalPrice = buyingObject.getBtotalPrice();
            int objFaturaNo = buyingObject.getBfaturaNo();
            int objDiscount = buyingObject.getPdiscount();
            int objPurType = buyingObject.getPurtype();
            String objTname = buyingObject.getTname();
            int objTaxType = buyingObject.getTaxtype();
            String objPname = buyingObject.getPname();

            int bfaturaNo = Integer.parseInt(scfatno);

            List<Buying> buyingList = bRepo.getByBfaturaNoEquals(bfaturaNo);

            for (Buying buying: buyingList){
                buying.setStatus(1);
                objFaturaNo = buying.getBfaturaNo();
                objGrossPrice += buying.getBgrossPrice();
                objNetPrice += buying.getBnetPrice();
                objTotalPrice += buying.getBtotalPrice();
                objDiscount += buying.getPdiscount();
                objTname = buying.getTname();
                objPname = buying.getPname();
                objPurType = buying.getPurtype();
                objTaxType = buying.getTaxtype();


                // product vaccine güncelleme
                Integer pname = Integer.parseInt(buying.getPname());

                if(vacRepo.findById(pname).isPresent()) {
                    int temp = vacRepo.findById(pname).get().getVacstock();
                    vacRepo.findById(pname).get().setVacstock(temp + buying.getProamount());

                    Storage storage = new Storage();
                    storage.setStaction(1);
                    storage.setStdate(new Date());
                    storage.setStchangeamount(buying.getProamount());
                    storage.setStlastamount(temp + buying.getProamount());
                    storage.setStvac(vacRepo.findById(pname).get());
                    strRepo.save(storage);
                }
                else if (proRepo.findById(pname).isPresent()) {
                    int temp = proRepo.findById(pname).get().getPrstock();
                    proRepo.findById(pname).get().setPrstock(temp + buying.getProamount());

                    Storage storage = new Storage();
                    storage.setStaction(1);
                    storage.setStdate(new Date());
                    storage.setStchangeamount(buying.getProamount());
                    storage.setStlastamount(temp + buying.getProamount());
                    storage.setStpro(proRepo.findById(pname).get());
                    strRepo.save(storage);
                }
                else {
                    System.out.println("");
                }

                Authentication aut = SecurityContextHolder.getContext().getAuthentication();
                String email = aut.getName(); // username

                // KASA
                MoneyCase casec = new MoneyCase();
                casec.setCmoney(buying.getBtotalPrice());
                casec.setCbuysell(2);
                casec.setCdatenow(new Date());
                casec.setCpeople(supRepo.findById(Integer.parseInt(buying.getTname())).get().getSuname());
                casec.setCcomment("Fatura");
                casec.setCname(email);
                caRepo.save(casec);
            }

            buyingObject.setBfaturaNo(objFaturaNo);
            buyingObject.setBgrossPrice(objGrossPrice);
            buyingObject.setBnetPrice(objNetPrice);
            buyingObject.setBtotalPrice(objTotalPrice);
            buyingObject.setBuyingDate(new Date());
            buyingObject.setPdiscount(objDiscount/(buyingList.size()));
            buyingObject.setPurtype(objPurType);
            buyingObject.setTaxtype(objTaxType);
            buyingObject.setTname(objTname);
            buyingObject.setPname(objPname);

            buyingObject.setStatus(2);
            bRepo.saveAndFlush(buyingObject);
            bRepo.saveAllAndFlush(buyingList);
            status = "1";

            // Tedarikçiye borç ekleme
            int tname = Integer.parseInt(buyingObject.getTname());
            Suppliers suppliers = supRepo.findById(tname).get();
            suppliers.setSudebt(suppliers.getSudebt()+buyingObject.getBtotalPrice());
            supRepo.saveAndFlush(suppliers);


            // List<Buying> buyings = bRepo.findByPnameEqualsIgnoreCaseAndStatusEqualsOrderByBidDesc(buyingObject.getTname(), 1);

        }

        catch (Exception ex){
            System.err.println("Status değiştirme sırasında hata " + ex);
        }


        return status;
    }

    @ResponseBody
    @GetMapping("/cList")
    public List<Buying> clist(){

        System.out.println("Controller Listeleme Çalıştı =====================================================================");
        return bRepo.findByStatusEquals(2);
    }
    @ResponseBody
    @DeleteMapping("/pDelete/{pdindex}")
    public String pdelete(@PathVariable String pdindex){
        String status = "0";
        int bid = Integer.parseInt(pdindex);
        try{
            bRepo.deleteById(bid);
            status = "1";
        }
        catch (Exception ex ){
            System.err.println("Ürün Silme sırasında hata " + ex);
        }
        return status;
    }

    @Lazy
    @ResponseBody
    @GetMapping("/detail/{detailNo}")
    public List<Buying> detail(@PathVariable String detailNo){
        String status="0";
        int bfaturaNo = Integer.parseInt(detailNo);
        List<Buying> buyings = bRepo.findByBfaturaNoEqualsAndStatusEquals(bfaturaNo, 1);
        try{



            for (Buying buyingx: buyings){
                System.out.println(" DETAYLARRRRRRRRRRRRR    " + buyingx.getBfaturaNo() +"  " +   buyingx.getTname() + "  " + buyingx.getStatus());

            }

            bRepo.saveAllAndFlush(buyings);

            status = "1";
        }
        catch (Exception ex){
            System.err.println("Ürün detay gösterme sırasında hata " + ex);
        }



        return buyings;
    }

}







//================================================================================00


















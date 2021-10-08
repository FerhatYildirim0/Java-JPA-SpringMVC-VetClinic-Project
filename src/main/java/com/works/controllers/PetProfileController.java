package com.works.controllers;

import com.works.entities.*;
import com.works.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Controller
@RequestMapping("/petprofile")
public class PetProfileController {
    Pet petGlob= new Pet();
    Pet petCure= new Pet();
    public Integer petindex = 0;
    final PetRepository pRepo;
    final CustomerRepository cRepo;
    final CureRepository cureRepo;
    final VaccineRepository vacRepo;
    final ProductRepository proRepo;
    final LaborRepository laRepo;
    final StorageRepository strRepo;
    final LabRepository labRepo;

    private final String UPLOAD_DIR =  "src/main/resources/static/uploads/";

    public PetProfileController(LabRepository labRepo, CustomerRepository cRepo, StorageRepository strRepo, PetRepository pRepo, CureRepository cureRepo, VaccineRepository vacRepo, ProductRepository proRepo, LaborRepository laRepo) {
        this.pRepo = pRepo;
        this.cRepo = cRepo;
        this.cureRepo = cureRepo;
        this.vacRepo = vacRepo;
        this.proRepo = proRepo;
        this.laRepo = laRepo;
        this.strRepo = strRepo;
        this.labRepo = labRepo;
    }

    @GetMapping("")
    public String petprofile() {


        return "petprofile";
    }

    @GetMapping("/ppData/{index}")
    public String petProfile(@PathVariable String index, Model model) {
        Integer id=Integer.parseInt(index);
        petindex = id;
        try {
            Pet pet=pRepo.findById(id).get();
            Pet pet1=pRepo.findById(id).get();
            petGlob=pet;
            petCure=pet1;
            model.addAttribute("cureShow",new Cure());
            model.addAttribute("pet",pet);
            List<Cure> cureList=cureRepo.findByPet_Pid(id);
            model.addAttribute("cureList",cureList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Cure> cr = cureRepo.findAll();
        model.addAttribute("cure",cr);

        List<Vaccine> vaccineList = vacRepo.findAll();
        model.addAttribute("vac", vaccineList);

        List<Product> productList = proRepo.findAll();
        model.addAttribute("pro", productList);

        List<Labor> laborList = laRepo.findAll();
        model.addAttribute("la", laborList);

        List<Lab> labList = labRepo.findByPid_PidEqualsOrderByLidDesc(id);
        model.addAttribute("lab", labList);

        return "petprofile";
    }


    @GetMapping("/cProfile")
    public String petcProfile(){

        return "petprofile";

    }


    @PostMapping("/uProfile")
    public String uPetProfile(@Valid @ModelAttribute("pet") Pet pet, BindingResult bindingResult){
        String returnlink = String.valueOf(petGlob.getPid());
        if ( !bindingResult.hasErrors() ) {
            try {
                petGlob.setPname(pet.getPname());
                petGlob.setPchip(pet.getPchip());
                petGlob.setPbirthday(pet.getPbirthday());
                pRepo.saveAndFlush(petGlob);

            } catch (Exception e) {
                e.getLocalizedMessage();
            }
            return "redirect:/petprofile/ppData/"+returnlink;
        }
        return "redirect:/petprofile/ppData/"+returnlink;
    }



    @GetMapping("cDelete/{index}")
    public String petcDelete(@PathVariable String index){
        int pid=Integer.parseInt(index);
        String returnlink = String.valueOf(petGlob.getCid().getCid());

        try {
            petGlob= pRepo.findById(pid).get();

            pRepo.deleteById(petGlob.getPid());

        } catch (Exception e) {
            e.getLocalizedMessage();
        }


        return "customerprofile/cProfile/"+returnlink;

    }


    @PostMapping("/cure")
    public String cureAdd(Cure cure){
        Integer temp_debt = 0;
        Storage storagePro = new Storage();
        Storage storageVac = new Storage();
        storagePro.setStaction(3);
        storageVac.setStaction(3);
        Pet pet = petCure;
        String returnlink = String.valueOf(pet.getPid());
        System.out.println(returnlink);
        try {
            cure.setPet(pet);
            System.out.println("PRO:" + cure.getCurpro());
            if( cure.getCurpro() != null)  {
                temp_debt = temp_debt + cure.getCurpro().getPrsales();
                cure.getCurpro().setPrstock(cure.getCurpro().getPrstock() - 1);
                storagePro.setStpro(cure.getCurpro());
                storagePro.setStchangeamount(1);
                storagePro.setStlastamount(cure.getCurpro().getPrstock() - 1);
                storagePro.setStdate(new Date());
                strRepo.save(storagePro);
            }
            if (cure.getCurvac() != null) {
                temp_debt = temp_debt + cure.getCurvac().getVacsales();
                cure.getCurvac().setVacstock(cure.getCurvac().getVacstock() - 1);
                storageVac.setStvac(cure.getCurvac());
                storageVac.setStchangeamount(1);
                storageVac.setStlastamount(cure.getCurvac().getVacstock() - 1);
                storageVac.setStdate(new Date());
                strRepo.save(storageVac);
            }
            if (cure.getCurla() != null) {
                temp_debt = temp_debt + cure.getCurla().getLamoney();
            }

            cure.setCurdebt(temp_debt);
            Cure c= cureRepo.save(cure);
            Optional<Pet> pet1 = pRepo.findById(cure.getPet().getPid());
            Optional<Customer> customerOptional = cRepo.findById(pet1.get().getCid().getCid());
            customerOptional.get().setCdebpt(customerOptional.get().getCdebpt() + temp_debt);
            cRepo.saveAndFlush(customerOptional.get());

        } catch (Exception e) {
            System.out.println("Exception:  "+ e);
        }

        return "redirect:/petprofile/ppData/"+returnlink;
    }


    @GetMapping("/cureDelete/{index}")
    public String cureDelete(@PathVariable String index){
        int cure_id = Integer.parseInt(index);
        String  returnlink="customer-list";
        try {
            Cure c= cureRepo.findById(cure_id).get();
            returnlink = String.valueOf(c.getPet().getPid());

            cureRepo.deleteById(cure_id);
        } catch (Exception e) {
            return "redirect:/"+returnlink;
        }


        return "redirect:/petprofile/ppData/"+returnlink;
    }

    @GetMapping("/petShow/{index}")
    public String petShow(@PathVariable String index,Model model){
        int curid=Integer.parseInt(index);
        Cure c = cureRepo.findById(curid).get();
        model.addAttribute("cureShow",c);
        System.out.println(c);
        return "petprofile";
    }


    // delete image item
    @GetMapping("deleteImage/{iid}")
    public String deleteImage( @PathVariable String iid ) {
        try {
            Integer lid = Integer.parseInt( iid );
            Optional<Lab> lab = labRepo.findById(lid);
            if ( lab.isPresent() ) {
                labRepo.deleteById(lid);
                // file delete
                String deleteFilePath = lab.get().getFileName();
                File file = new File(UPLOAD_DIR+deleteFilePath);
                file.delete();
            }
        }catch (Exception ex) {
            System.out.println("Ex: " + ex);
        }
        return "redirect:/petprofile/ppData/" + petindex;
    }


}

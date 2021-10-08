package com.works.controllers;

import com.works.entities.Lab;
import com.works.entities.Pet;
import com.works.repositories.LabRepository;
import com.works.repositories.PetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/lab")
public class LabController {

    final PetRepository pRepo;
    final LabRepository labRepo;

    public LabController(PetRepository pRepo, LabRepository labRepo) {
        this.pRepo = pRepo;
        this.labRepo = labRepo;
    }

    private final String UPLOAD_DIR =  "src/main/resources/static/uploads/";
    long maxFileUploadSize = 20048;

    int sendCount = 0;
    int sendSuccessCount = 0;
    String errorMessage = "";

    @GetMapping("")
    private String lab(Model model) {

        List<Pet> petList = pRepo.findAll();
        model.addAttribute("pet", petList);

        List<Lab> labList = labRepo.findByOrderByLidDesc();
        model.addAttribute("lab", labList);


        return "lab";
    }

    // image upload fnc
    @PostMapping("/imageUpload")
    public String imageUpload(@RequestParam("imageName")MultipartFile[] files, Lab lab) {
        System.out.println(files.length +""+files);
        if ( files != null && files.length != 0 ) {
            sendCount = files.length;
            for ( MultipartFile file : files ) {

                long fileSizeMB = file.getSize() / 1024;
                if ( fileSizeMB > maxFileUploadSize ) {
                    System.err.println("Dosya boyutu çok büyük Max 2MB");
                    errorMessage = "Dosya boyutu çok büyük Max "+ (maxFileUploadSize / 1024) +"MB olmalıdır";
                }else {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    String ext = fileName.substring(fileName.length()-5, fileName.length());
                    String uui = UUID.randomUUID().toString();
                    fileName = uui + ext;
                    try {
                        Path path = Paths.get(UPLOAD_DIR + fileName);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        sendSuccessCount += 1;

                        // add database

                        lab.setFileName(fileName);
                        lab.setLdate(new Date());
                        labRepo.saveAndFlush(lab);



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            errorMessage = "Lütfen resim seçiniz!";
        }

        return "redirect:/lab";
    }




}

package com.e_esj.poc.Accueil_Orientation.controller;


import com.e_esj.poc.Accueil_Orientation.entity.Medecin;
import com.e_esj.poc.Accueil_Orientation.exception.CINNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.EmailNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.PhoneNonValideException;
import com.e_esj.poc.Accueil_Orientation.service.MedecinService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/medecins")
public class MedecinController {


    @Autowired
    MedecinService medecinService;

@PostMapping("/registration")
    public ResponseEntity<Medecin> savedMedecin(@RequestBody Medecin medecin) throws CINNonValideException, PhoneNonValideException, EmailNonValideException {
        Medecin savedmedecin = medecinService.savedMedecin(medecin);
        return ResponseEntity.ok(savedmedecin);

}

@GetMapping("/{id}")
    public  ResponseEntity<Medecin> getMedecin(@PathVariable Long id){
    Medecin medecin = medecinService.getMedecin(id);
    return ResponseEntity.ok(medecin);

}
}

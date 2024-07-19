package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.Dto.MedecinResponseDTO;
import com.e_esj.poc.Accueil_Orientation.entity.Medecin;
import com.e_esj.poc.Accueil_Orientation.exception.CINNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.EmailNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.MedecinException;
import com.e_esj.poc.Accueil_Orientation.exception.PhoneNonValideException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MedecinService {
    Medecin savedMedecin(@RequestBody Medecin medecin) throws EmailNonValideException, PhoneNonValideException, CINNonValideException;

    Medecin getMedecin(Long id);

    List<MedecinResponseDTO> getAllMedecin();

    void deleteMedecin(Long id) throws MedecinException;

    //   Medecin saveMedecin(Medecin medecin) throws MedecinException;
}

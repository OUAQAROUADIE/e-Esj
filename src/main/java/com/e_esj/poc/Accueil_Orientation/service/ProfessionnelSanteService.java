package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteDto;
import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteResponseDTO;
import com.e_esj.poc.Accueil_Orientation.entity.ProfessionnelSante;
import com.e_esj.poc.Accueil_Orientation.exception.CINNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.EmailNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.PhoneNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.ProfessionnelSanteException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfessionnelSanteService {
    ProfessionnelSante saveProfessionnelSante(ProfessionnelSante professionnelSante) throws EmailNonValideException, PhoneNonValideException, CINNonValideException;

    ProfessionnelSante getProfessionnelSante(Long id) throws EmailNonValideException, PhoneNonValideException, CINNonValideException;


    List<ProfessionnelSanteResponseDTO> findAllProfessionnelSante();




    @Transactional
    ProfessionnelSanteResponseDTO updateProfessionnelSantePartial(Long id, ProfessionnelSanteDto updatesDto)
            throws ProfessionnelSanteException;

    void deleteProfessionnelSante(Long id) throws ProfessionnelSanteException;
}

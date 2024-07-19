package com.e_esj.poc.Accueil_Orientation.mappers;

import com.e_esj.poc.Accueil_Orientation.Dto.MedecinResponseDTO;
import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteResponseDTO;
import com.e_esj.poc.Accueil_Orientation.entity.Medecin;
import com.e_esj.poc.Accueil_Orientation.entity.ProfessionnelSante;
import org.springframework.stereotype.Service;

@Service
public class MedecinMapper {

    public MedecinResponseDTO fromMedecin(Medecin medecin){
        MedecinResponseDTO medecinResponseDTO =new MedecinResponseDTO();
        medecinResponseDTO.setId( medecin.getId());
        medecinResponseDTO.setPrenom(medecin.getUser().getPrenom());
        medecinResponseDTO.setNom(medecin.getUser().getNom());
        medecinResponseDTO.setMail(medecin.getUser().getEmail());
        medecinResponseDTO.setCin(medecin.getCin());
        medecinResponseDTO.setInpe(medecin.getInpe());
        medecinResponseDTO.setPpr(medecin.getPpr());
        medecinResponseDTO.setEstGeneraliste(medecin.getEstGeneraliste());
        medecinResponseDTO.setEstMedcinESJ(medecin.getEstMedcinESJ());
        medecinResponseDTO.setSpecialite(medecin.getSpecialite());
        return medecinResponseDTO;
    }
}


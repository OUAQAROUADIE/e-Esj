package com.e_esj.poc.Accueil_Orientation.mappers;


import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteResponseDTO;
import com.e_esj.poc.Accueil_Orientation.entity.ProfessionnelSante;
import org.springframework.stereotype.Service;

@Service
public class ProfessionnelSanteMapper {
    public ProfessionnelSanteResponseDTO fromProfessionnelSante(ProfessionnelSante professionnelSante){
        ProfessionnelSanteResponseDTO professionnelSanteResponseDTO=new ProfessionnelSanteResponseDTO();
        professionnelSanteResponseDTO.setId(professionnelSante.getId());
        professionnelSanteResponseDTO.setPrenom(professionnelSante.getUser().getPrenom());
        professionnelSanteResponseDTO.setNom(professionnelSante.getUser().getNom());
        professionnelSanteResponseDTO.setMail(professionnelSante.getUser().getEmail());
        professionnelSanteResponseDTO.setCin(professionnelSante.getCin());
        professionnelSanteResponseDTO.setInpe(professionnelSante.getInpe());
        return professionnelSanteResponseDTO;
    }
}

package com.e_esj.poc.Accueil_Orientation.Dto;

import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import lombok.Data;


@Data
public class ProfessionnelSanteDto {

    private String cin;
    private String inpe;

    private InfoUserDto user;
    // Getters and setters
}

package com.e_esj.poc.Accueil_Orientation.Dto;

import lombok.Data;

@Data
public class MedecinDTO {

    private String cin;

    private String inpe;
    private String ppr;
    private Boolean estMedcinESJ;
    private Boolean estGeneraliste;
    private String specialite;

    private InfoUserDto user;

}

package com.e_esj.poc.Accueil_Orientation.Dto;

import lombok.Data;

@Data
public class MedecinResponseDTO {

    private Long id;
    private String cin;
    private String nom;
    private String prenom;
    private String mail;
    private String inpe;
    private String ppr;
    private Boolean estMedcinESJ;
    private Boolean estGeneraliste;
    private String specialite;
}

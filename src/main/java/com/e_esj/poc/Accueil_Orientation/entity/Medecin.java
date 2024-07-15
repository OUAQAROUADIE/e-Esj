package com.e_esj.poc.Accueil_Orientation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Medecin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String cin;
    private String inpe;
    private String ppr;
    private Boolean estMedcinESJ;
    private Boolean estGeneraliste;
    private String specialite;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private InfoUser user;

    private String ROLE="MEDECIN";

    private boolean confirmed =false;
    private Boolean isFirstAuth = true;
    // Ajoutez des méthodes pour accéder aux attributs de l'utilisateur si nécessaire
}

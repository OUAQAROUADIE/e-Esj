package com.e_esj.poc.Accueil_Orientation.entity;

import com.e_esj.poc.Accueil_Orientation.enums.NiveauEtudes;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SCOLARISE")
@Data @AllArgsConstructor @NoArgsConstructor
public class JeuneScolarise extends Jeune{
    @Enumerated(EnumType.STRING)
    private NiveauEtudes niveauEtudesActuel;
    private String CNE;
    private String codeMASSAR;
}
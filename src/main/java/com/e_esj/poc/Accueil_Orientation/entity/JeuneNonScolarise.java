package com.e_esj.poc.Accueil_Orientation.entity;

import com.e_esj.poc.Accueil_Orientation.enums.NiveauEtudes;
import com.e_esj.poc.Accueil_Orientation.enums.Situation;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("NON_SCOLARISE")
@Data @AllArgsConstructor @NoArgsConstructor
public class JeuneNonScolarise extends Jeune {

    @Enumerated(EnumType.STRING)
    private NiveauEtudes dernierNiveauEtudes;
    @Enumerated(EnumType.STRING)
    private Situation situationActuelle;
}

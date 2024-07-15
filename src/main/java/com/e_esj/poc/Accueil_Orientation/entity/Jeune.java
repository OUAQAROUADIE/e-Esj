package com.e_esj.poc.Accueil_Orientation.entity;

import com.e_esj.poc.Accueil_Orientation.enums.Sexe;
import com.e_esj.poc.Accueil_Orientation.exception.AgeNonValideException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Jeune {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Sexe sexe;
    private Date dateNaissance;
    private int age;
    private int identifiantPatient;
    private boolean scolarise;
    private String cin;
    private Boolean isConfirmed = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private InfoUser user;

    private String ROLE="JEUNE";
    private Boolean isFirstAuth = true;


    public int CalculerAge() {
        if (dateNaissance != null) {
            LocalDate birthDate = dateNaissance.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate currentDate = LocalDate.now();
            int calculatedAge = Period.between(birthDate, currentDate).getYears();
            if (calculatedAge < 10 || calculatedAge > 30) {
                throw new IllegalArgumentException("L'âge doit être entre 10 et 30 ans");
            }
            this.age = calculatedAge;
            return this.age;

        }

        throw new IllegalArgumentException("Date de naissance est requise pour calculer l'âge");





    }

  }

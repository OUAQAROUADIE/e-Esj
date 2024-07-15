package com.e_esj.poc.Accueil_Orientation.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AntecedentPersonnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "jeune_id")
    private Jeune jeune;

    @ElementCollection
    private List<String> medicaux;

    @ElementCollection
    private List<String> habitudes;

    private Boolean chirurgicaux;


}

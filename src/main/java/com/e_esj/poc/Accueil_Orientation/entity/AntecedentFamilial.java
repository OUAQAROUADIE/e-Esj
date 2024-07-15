package com.e_esj.poc.Accueil_Orientation.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AntecedentFamilial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name ="jeune_id")
    private Jeune jeune;

    @ElementCollection
    private List<String> maladiesFamiliales;
}

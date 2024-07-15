package com.e_esj.poc.Accueil_Orientation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "utilisateur")

@Data @NoArgsConstructor @AllArgsConstructor
public class InfoUser  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private boolean enabled;

    @Column(unique = true)
    private String email;
    private String telephone;
    private String password;
}

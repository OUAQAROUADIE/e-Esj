package com.e_esj.poc.Accueil_Orientation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class ProfessionnelSante{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cin;
    private String inpe;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private InfoUser user;
    private boolean confirmed =false;
    private Boolean isFirstAuth = true;

    private String ROLE="PROFESSIONELSANTE";


}

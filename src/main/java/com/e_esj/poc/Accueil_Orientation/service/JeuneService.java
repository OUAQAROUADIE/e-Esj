package com.e_esj.poc.Accueil_Orientation.service;


import com.e_esj.poc.Accueil_Orientation.entity.*;
import com.e_esj.poc.Accueil_Orientation.exception.*;

import java.util.Map;

public interface  JeuneService {

    Jeune saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException, CINNonValideException, AgeNonValideException;



    Jeune updateJeune(Long id, Map<String, Object> updates);

    AntecedentFamilial addAntecedentFamilial(Long jeuneId, AntecedentFamilial antecedentFamilial);

    AntecedentPersonnel addAntecedentPersonnel(Long jeuneId, AntecedentPersonnel antecedentPersonnel);

    Map<String, Object> getAntecedents(Long jeuneId) throws JeuneException;


}

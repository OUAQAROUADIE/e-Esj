package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.entity.*;
import com.e_esj.poc.Accueil_Orientation.enums.NiveauEtudes;
import com.e_esj.poc.Accueil_Orientation.exception.*;
import com.e_esj.poc.Accueil_Orientation.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class JeuneServiceImpl implements  JeuneService{

    private AntecedentFamilialRepository antecedentFamilialRepository;
    private AntecedentPersonnelRepository antecedentPersonnelRepository;
    private PasswordTokenRepository passwordTokenRepository;
    private JeuneRepository jeuneRepository;
    private JeuneScolariseRepository jeuneScolariseRepository;

    private UserRepository userRepository;

    @Transactional
    @Override
    public Jeune saveJeune(Jeune jeune) throws EmailNonValideException, PhoneNonValideException, CINNonValideException, AgeNonValideException {
        if (!CommunService.isValidEmail(jeune.getUser().getEmail())) {
            throw new EmailNonValideException("invalid email format");
        }
        if (!CommunService.isValidMoroccanPhoneNumber(jeune.getUser().getTelephone())) {
            throw new PhoneNonValideException("invalid phone number format");
        }
        if (!CommunService.isValidCIN(jeune.getCin())) {
            throw new CINNonValideException("Invalid cin format");
        }
        if (jeuneRepository.existsByUserId(jeune.getUser().getId())) {
            throw new IllegalArgumentException("Un jeune avec cet identifiant utilisateur existe déjà.");
        }

        // Calculer l'âge avant de sauvegarder
        jeune.CalculerAge();

        // Vérifiez si l'âge est valide
        if (jeune.getAge() < 10 || jeune.getAge() > 30) {
            throw new AgeNonValideException("L'âge doit être entre 10 et 30 ans");
        }

        if(jeune.isScolarise()){
            JeuneScolarise jeuneScolarise = new JeuneScolarise();
            jeuneScolarise.setDateNaissance(jeune.getDateNaissance());
            jeuneScolarise.setAge(jeune.getAge());
            jeuneScolarise.setCin(jeune.getCin());
            jeuneScolarise.setIdentifiantPatient(jeune.getIdentifiantPatient());
            jeuneScolarise.setScolarise(true);
            jeuneScolarise.setSexe(jeune.getSexe());
            jeuneScolarise.setUser(jeune.getUser());

            return jeuneScolariseRepository.save(jeuneScolarise);
        }


        return jeuneRepository.save(jeune);
    }

    @Override
    public Jeune updateJeune(Long id, Map<String, Object> updates) {
        Optional<Jeune> existingJeuneOptional = jeuneRepository.findById(id);
        if (!existingJeuneOptional.isPresent()) {
            throw new EntityNotFoundException("Jeune avec ID " + id + " n'existe pas.");
        }

        Jeune existingJeune = existingJeuneOptional.get();

        updates.forEach((key, value) -> {
            switch (key) {
                case "niveauEtudesActuel":
                    if (existingJeune instanceof JeuneScolarise) {
                        ((JeuneScolarise) existingJeune).setNiveauEtudesActuel(NiveauEtudes.valueOf((String) value));
                    }
                    break;
                case "CNE":
                    if (existingJeune instanceof JeuneScolarise) {
                        ((JeuneScolarise) existingJeune).setCNE((String) value);
                    }
                    break;
                // Ajoutez d'autres cas ici si nécessaire
                default:
                    break;
            }
        });

        return jeuneRepository.save(existingJeune);
    }




    @Override
    public AntecedentFamilial addAntecedentFamilial(Long jeuneId, AntecedentFamilial antecedentFamilial) {
        Jeune jeune = jeuneRepository.findById(jeuneId).orElseThrow(() -> new  IllegalArgumentException("jeune not found"));
        antecedentFamilial.setJeune(jeune);


        return antecedentFamilialRepository.save(antecedentFamilial);
    }

    @Override
    public AntecedentPersonnel addAntecedentPersonnel(Long jeuneId, AntecedentPersonnel antecedentPersonnel) {
        Jeune jeune = jeuneRepository.findById(jeuneId).orElseThrow(()-> new IllegalArgumentException("jeune not found"));
        antecedentPersonnel.setJeune(jeune);

        return antecedentPersonnelRepository.save(antecedentPersonnel);
    }



    @Override
    public Map<String, Object> getAntecedents(Long jeuneId) throws JeuneException {
        Optional<Jeune> saveJeune = jeuneRepository.findById(jeuneId);

        if(saveJeune.isPresent()){
            Jeune jeune= saveJeune.get();
            AntecedentFamilial antecedentFamilial = antecedentFamilialRepository.findByJeune(jeune).orElseThrow(() ->new JeuneException("Not found"));
            AntecedentPersonnel antecedentPersonnel = antecedentPersonnelRepository.findByJeune(jeune).orElseThrow(() -> new JeuneException("Not Found"));
            Map<String, Object> result = new HashMap<>();
            result.put("AntecedentFamilial", antecedentFamilial);
            result.put("AntecedentPersonnel", antecedentPersonnel);
            return result;
        }else {
            throw new JeuneException("jeune not found");
        }    }


}

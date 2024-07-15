package com.e_esj.poc.Accueil_Orientation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class MedecinServiceImpl  implements  MedecinService{
/*
    private MedecinRepository medecinRepository;
    @Override
    public Medecin saveMedecin(Medecin medecin) throws MedecinException {
        try {

            return medecinRepository.save(medecin);
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cause = (ConstraintViolationException) e.getCause();
                String constraintName = cause.getConstraintName();
                if (constraintName.contains("mail")) {
                    throw new MedecinException("L'email spécifié est déjà utilisé par un autre utilisateur");
                } else if (constraintName.contains("cin")) {
                    throw new MedecinException("Le numéro de CIN spécifié est déjà utilisé par un autre utilisateur");
                }
            }
            throw new MedecinException("Une erreur s'est produite lors de l'enregistrement du médecin", e);
        }
    }*/
}

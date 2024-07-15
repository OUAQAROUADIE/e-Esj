package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteDto;
import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.ProfessionnelSante;
import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.VerificationToken;
import com.e_esj.poc.Accueil_Orientation.exception.CINNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.EmailNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.PhoneNonValideException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProfessionnelSanteService {
    ProfessionnelSante saveProfessionnelSante(ProfessionnelSante professionnelSante) throws EmailNonValideException, PhoneNonValideException, CINNonValideException;

    ProfessionnelSante getProfessionnelSante(Long id) throws EmailNonValideException, PhoneNonValideException, CINNonValideException;


    List<ProfessionnelSante> findAllProfessionnelSante();

    //ProfessionnelSante updateProfessionnelSante(Long id, ProfessionnelSante  updateProfessionnel) throws  EmailNonValideException, PhoneNonValideException, CINNonValideException;

    VerificationToken getVerificationToken(String VerificationToken);




    VerificationToken generateNewVerificationToken(String existingVerificationToken);

    void createPasswordResetTokenForUser(InfoUser user, String token);


    void changeUserPassword(InfoUser user, String password);

    boolean checkIfValidOldPassword(InfoUser user, String oldPassword);

    InfoUser getUser(String verificationToken);

    String validateVerificationToken(String token);


    InfoUser getUserByEmail(String email);

    Optional<InfoUser> validUsernameAndPassword(String username, String password);
}

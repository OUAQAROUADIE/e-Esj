package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.PasswordResetToken;
import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserService {
    InfoUser findUserByEmail(String userEmail);

    void createPasswordResetTokenForUser( InfoUser user, String token);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<InfoUser> getUserByPasswordResetToken(String token);

    Optional<InfoUser> getUserByID(long id);



    InfoUser getUser(String verificationToken);

    String validateVerificationToken(String token);

}

package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.PasswordResetToken;
import com.e_esj.poc.Accueil_Orientation.entity.VerificationToken;
import com.e_esj.poc.Accueil_Orientation.repository.JeuneRepository;
import com.e_esj.poc.Accueil_Orientation.repository.PasswordTokenRepository;
import com.e_esj.poc.Accueil_Orientation.repository.UserRepository;
import com.e_esj.poc.Accueil_Orientation.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private JeuneRepository jeuneRepository;


    @Autowired
    private PasswordTokenRepository passwordTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";
    @Override
    public InfoUser findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public void createPasswordResetTokenForUser(InfoUser user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }


    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public Optional<InfoUser> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token) .getUser());
    }

    @Override
    public Optional<InfoUser> getUserByID(final long id) {
        return userRepository.findById(id);
    }



    @Override
    public InfoUser getUser(final String verificationToken) {
        final VerificationToken token = tokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }


    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final InfoUser user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return TOKEN_VALID;
    }




}

package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.PasswordResetToken;
import com.e_esj.poc.Accueil_Orientation.entity.VerificationToken;
import com.e_esj.poc.Accueil_Orientation.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import static com.e_esj.poc.Accueil_Orientation.entity.VerificationToken.EXPIRATION;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private JeuneRepository jeuneRepository;


    @Autowired
    private PasswordTokenRepository passwordTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    private PasswordEncoder passwordEncoder;


    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Override
    public InfoUser findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }


    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public Optional<InfoUser> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
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

    @Override
    public void changeUserPassword(final InfoUser user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    @Override
    @Transactional
    public void createPasswordResetTokenForUser(InfoUser user, String token) {
        PasswordResetToken existingToken = passwordResetTokenRepository.findByUser(user);

        if (existingToken != null) {
            // Si un token existe pour cet utilisateur
            if (existingToken.isExpired()) {
                // Si le token existant n'est pas expiré, mettre à jour le token existant
                existingToken.setExpiryDate(PasswordResetToken.calculateExpiryDate(PasswordResetToken.EXPIRATION));
                existingToken.updateToken(token);
                passwordResetTokenRepository.save(existingToken);
                return; // Sortir de la méthode après la mise à jour
            } else {
                // Si le token existant est expiré, supprimer le token existant
                passwordResetTokenRepository.delete(existingToken);
            }
        }


    }

    @Override
    public Optional<InfoUser> validUsernameAndPassword(String email, String password) {
        InfoUser user = getUserByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
    @Override
    public InfoUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean checkIfValidOldPassword(final InfoUser user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }



    @Override
    public VerificationToken generateNewVerificationToken(String existingToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingToken);
        if (vToken != null) {
            vToken.setExpiryDate(vToken.calculateExpiryDate(EXPIRATION)); // Mettre à jour la date d'expiration
            vToken.updateToken(UUID.randomUUID().toString());
            tokenRepository.save(vToken);
            return vToken;
        }
        return vToken;
    }


}

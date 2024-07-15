package com.e_esj.poc.Accueil_Orientation.config;

import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.PasswordResetToken;
import com.e_esj.poc.Accueil_Orientation.repository.PasswordResetTokenRepository;
import com.e_esj.poc.Accueil_Orientation.repository.UserRepository;
import com.e_esj.poc.Accueil_Orientation.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@Transactional
public class UserSecurityService  {
/*
    @Autowired
    private UserService userService;
    private AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private UserRepository userRepository; // Suppose que vous avez un UserRepository pour gérer les utilisateurs

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

   private PasswordEncoder passwordEncoder;

    @Override
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }
   /* @Override
    public String authenticateAndGenerateToken(String email, String password) {
        // Vérifier l'utilisateur par email
        InfoUser user = userRepository.findByEmail(email);

        // Vérifier si l'utilisateur existe et si le mot de passe est correct
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Génération d'un token d'authentification (utilisation d'un UUID pour l'exemple)
            String authToken = UUID.randomUUID().toString();

            // Logique supplémentaire si nécessaire (comme la gestion des tokens dans la base de données)

            return authToken;
        }

        return null; // Retourner null si l'authentification échoue
    }

    @Override
    public Optional<InfoUser> authenticateAndGenerateToken(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String authToken = jwtUtil.generateToken(userDetails.getUsername());
            return Optional.of(userService.findUserByEmail(email)); // Retourne l'utilisateur authentifié
        } catch (AuthenticationException e) {
            return null; // Échec de l'authentification
        }
    }

    @Override
    public boolean validateToken(String authToken) {
        return true;
    }


    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }*/
}

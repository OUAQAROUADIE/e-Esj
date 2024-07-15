package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.Dto.InfoUserDto;
import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteDto;
import com.e_esj.poc.Accueil_Orientation.entity.PasswordResetToken;
import com.e_esj.poc.Accueil_Orientation.entity.ProfessionnelSante;
import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.VerificationToken;
import com.e_esj.poc.Accueil_Orientation.exception.CINNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.EmailNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.PhoneNonValideException;
import com.e_esj.poc.Accueil_Orientation.repository.PasswordResetTokenRepository;
import com.e_esj.poc.Accueil_Orientation.repository.ProfessionnelSanteRepository;
import com.e_esj.poc.Accueil_Orientation.repository.UserRepository;
import com.e_esj.poc.Accueil_Orientation.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.e_esj.poc.Accueil_Orientation.entity.VerificationToken.EXPIRATION;

@Service
@Transactional
public class ProfessionnelSanteServiceImpl implements ProfessionnelSanteService{

    @Autowired
    private ProfessionnelSanteRepository professionnelSanteRepository;


        private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

@Autowired
private VerificationTokenRepository verificationTokenRepository;

    private PasswordEncoder passwordEncoder;

    // Autres méthodes et constructeurs

    // Constructeur par défaut
    public ProfessionnelSanteServiceImpl() {
    }

    // Autres méthodes

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ProfessionnelSante saveProfessionnelSante(ProfessionnelSante professionnelSante) throws EmailNonValideException, PhoneNonValideException, CINNonValideException {
        if (professionnelSante.getUser() == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if(!CommunService.isValidEmail(professionnelSante.getUser().getEmail())){
            throw new EmailNonValideException("Invalid email format");
        }
        if(!CommunService.isValidMoroccanPhoneNumber(professionnelSante.getUser().getTelephone())){
            throw new PhoneNonValideException("Invalid phone number format ");
        }
        if (!CommunService.isValidCIN(professionnelSante.getCin())){
            throw new CINNonValideException("Invalid CIN format");
        }

        InfoUser user = professionnelSante.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ProfessionnelSante savedProfessionnelSante = professionnelSanteRepository.save(professionnelSante);

        // Create VerificationToken entity
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString()); // Example token generation
        verificationToken.setUser(user); // Set the saved InfoUser

        // Save VerificationToken
        verificationTokenRepository.save(verificationToken);
        // Create and send verification token

        return savedProfessionnelSante;

    }

    private SimpleMailMessage constructEmail(String subject, String body, InfoUser user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }


    @Override
    public ProfessionnelSante getProfessionnelSante(Long id) throws EmailNonValideException, PhoneNonValideException, CINNonValideException {
       return professionnelSanteRepository.findById(id).orElse(null);
    }


    @Override
    public List<ProfessionnelSante> findAllProfessionnelSante() {
        return professionnelSanteRepository.findAll();
    }

    /*@Override
    public ProfessionnelSante updateProfessionnelSante(Long id, ProfessionnelSante updateProfessionnel) throws EmailNonValideException, PhoneNonValideException, CINNonValideException {
        Optional<ProfessionnelSante> optionalProSante = professionnelSanteRepository.findById(id);
        if (optionalProSante.isPresent()) {
            ProfessionnelSante existingProSante = optionalProSante.get();

            // Mise à jour des champs si présents dans la requête
            if (updateProfessionnel.getCin() != null) {
                existingProSante.setCin(updateProfessionnel.getCin());
            }
            if (updateProfessionnel.getInpe() != null) {
                existingProSante.setInpe(updateProfessionnel.getInpe());
            }
            if (updateProfessionnel.getUser() != null) {
                InfoUser updatedUser = updateProfessionnel.getUser();
                InfoUser existingUser = existingProSante.getUser();

                // Mise à jour des attributs de l'utilisateur si présents dans la requête
                if (updatedUser.getNom() != null) {
                    existingUser.setNom(updatedUser.getNom());
                }
                if (updatedUser.getPrenom() != null) {
                    existingUser.setPrenom(updatedUser.getPrenom());
                }
                if (updatedUser.getEmail() != null) {
                    existingUser.setEmail(updatedUser.getEmail());
                }
                if (updatedUser.getTelephone() != null) {
                    existingUser.setTelephone(updatedUser.getTelephone());
                }
                if (updatedUser.getPassword() != null) {
                    existingUser.setPassword(updatedUser.getPassword());
                }
            }

            // Enregistrer les modifications dans la base de données
            return professionnelSanteRepository.save(existingProSante);
        } else {
            throw new IllegalArgumentException("ProfessionnelSante not found with id " + id);
        }
    }*/
    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }



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

        // Créer un nouveau token
        PasswordResetToken newToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(newToken);
    }

    @Override
    public void changeUserPassword(final InfoUser user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final InfoUser user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
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
            return "TOKEN INVALID";
        }

        final InfoUser user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return "TOKEN_EXPIRED";
        }

        user.setEnabled(true);
        // tokenRepository.delete(verificationToken);
        userRepository.save(user);
        return "TOKEN_VALID";
    }
    @Override
    public InfoUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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




}

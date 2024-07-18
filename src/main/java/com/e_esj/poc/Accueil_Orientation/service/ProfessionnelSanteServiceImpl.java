package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.Dto.InfoUserDto;
import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteDto;
import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteResponseDTO;
import com.e_esj.poc.Accueil_Orientation.entity.ProfessionnelSante;
import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.VerificationToken;
import com.e_esj.poc.Accueil_Orientation.exception.CINNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.EmailNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.PhoneNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.ProfessionnelSanteException;
import com.e_esj.poc.Accueil_Orientation.mappers.ProfessionnelSanteMapper;
import com.e_esj.poc.Accueil_Orientation.repository.PasswordResetTokenRepository;
import com.e_esj.poc.Accueil_Orientation.repository.ProfessionnelSanteRepository;
import com.e_esj.poc.Accueil_Orientation.repository.UserRepository;
import com.e_esj.poc.Accueil_Orientation.repository.VerificationTokenRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfessionnelSanteServiceImpl implements ProfessionnelSanteService{

    @Autowired
    private ProfessionnelSanteRepository professionnelSanteRepository;

    @Autowired
    private EntityManager entityManager;

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
    ProfessionnelSanteMapper professionnelSanteMapper;

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
    public List<ProfessionnelSanteResponseDTO> findAllProfessionnelSante() {
        List<ProfessionnelSante> professionnelSantes = professionnelSanteRepository.findAll();
        return professionnelSantes.stream().map(ps -> professionnelSanteMapper.fromProfessionnelSante(ps)).collect(Collectors.toList()) ;
    }

    @Override
    @Transactional
    public ProfessionnelSanteResponseDTO updateProfessionnelSantePartial(Long id, ProfessionnelSanteDto updatesDto) throws  ProfessionnelSanteException {

        // Retrieve existing professionnel de santé entity from repository
        ProfessionnelSante existingProfessionnelSante = professionnelSanteRepository.findById(id)
                .orElseThrow(() -> new ProfessionnelSanteException("Professionnel de santé non trouvé avec l'ID : " + id));

        // Update User fields if updatesDto contains user information
        if (updatesDto.getUser() != null) {
            InfoUserDto userDto = updatesDto.getUser();
            InfoUser existingUser = existingProfessionnelSante.getUser();

            if (userDto.getNom() != null) {
                existingUser.setNom(userDto.getNom());
            }
            if (userDto.getPrenom() != null) {
                existingUser.setPrenom(userDto.getPrenom());
            }
            if (userDto.getEmail() != null) {
                existingUser.setEmail(userDto.getEmail());
            }
            if (userDto.getTelephone() != null) {
                existingUser.setTelephone(userDto.getTelephone());
            }
            if (userDto.getPassword() != null) {
                existingUser.setPassword(userDto.getPassword());
            }
        }

        // Update ProfessionnelSante fields if updatesDto contains professionnel de santé information
        if (updatesDto.getCin() != null) {
            existingProfessionnelSante.setCin(updatesDto.getCin());
        }
        if (updatesDto.getInpe() != null) {
            existingProfessionnelSante.setInpe(updatesDto.getInpe());
        }

        // Save updated entities
        userRepository.save(existingProfessionnelSante.getUser());
        professionnelSanteRepository.save(existingProfessionnelSante);

        return professionnelSanteMapper.fromProfessionnelSante(existingProfessionnelSante);
    }

    @Override
    public  void deleteProfessionnelSante(Long id) throws ProfessionnelSanteException {
        ProfessionnelSante professionnelSante = professionnelSanteRepository.findById(id).orElse(null);
        verificationTokenRepository.deleteByUserId(professionnelSante.getUser().getId());

        if(professionnelSante != null){
            try{
                professionnelSanteRepository.delete(professionnelSante);
            }catch (Exception e){
                throw new ProfessionnelSanteException("error");
            }

        }else{
            throw new ProfessionnelSanteException("Professionnel Sante not found");
        }


    }










}

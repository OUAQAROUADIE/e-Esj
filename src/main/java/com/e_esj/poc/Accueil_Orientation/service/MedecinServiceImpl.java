package com.e_esj.poc.Accueil_Orientation.service;

import com.e_esj.poc.Accueil_Orientation.Dto.InfoUserDto;
import com.e_esj.poc.Accueil_Orientation.Dto.MedecinDTO;
import com.e_esj.poc.Accueil_Orientation.Dto.MedecinResponseDTO;
import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.Medecin;
import com.e_esj.poc.Accueil_Orientation.entity.VerificationToken;
import com.e_esj.poc.Accueil_Orientation.exception.CINNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.EmailNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.MedecinException;
import com.e_esj.poc.Accueil_Orientation.exception.PhoneNonValideException;
import com.e_esj.poc.Accueil_Orientation.mappers.MedecinMapper;
import com.e_esj.poc.Accueil_Orientation.repository.MedecinRepository;
import com.e_esj.poc.Accueil_Orientation.repository.UserRepository;
import com.e_esj.poc.Accueil_Orientation.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class MedecinServiceImpl  implements  MedecinService {

    private PasswordEncoder passwordEncoder;

    @Autowired
    private MedecinRepository medecinRepository;

    private VerificationTokenRepository verificationTokenRepository;

    private MedecinMapper medecinMapper;

    @Autowired
    private UserRepository userRepository;

    private Environment env;
    @Override
    public Medecin savedMedecin( Medecin medecin) throws EmailNonValideException, PhoneNonValideException, CINNonValideException {
        if (medecin.getUser() == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        if (!CommunService.isValidEmail(medecin.getUser().getEmail())) {
            throw new EmailNonValideException("Invalid email format");
        }
        if (!CommunService.isValidMoroccanPhoneNumber(medecin.getUser().getTelephone())) {
            throw new PhoneNonValideException("Invalid phone number format ");
        }
        if (!CommunService.isValidCIN(medecin.getCin())) {
            throw new CINNonValideException("Invalid CIN format");
        }

        InfoUser user = medecin.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Medecin savedmedecin = medecinRepository.save(medecin);


        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return savedmedecin;
    }

    @Override
    public Medecin getMedecin(Long id){
        return  medecinRepository.findById(id).orElse(null);
    }


    @Override
    public List<MedecinResponseDTO> getAllMedecin(){
         List<Medecin> medecins = medecinRepository.findAll();
         return medecins.stream().map(m -> medecinMapper.fromMedecin(m)).collect(Collectors.toList());
    }


    public MedecinResponseDTO updateMedecin(Long id , MedecinDTO medecinDTO) throws MedecinException {
        Medecin medecin = medecinRepository.findById(id).orElseThrow(() -> new MedecinException("medecin not found"));


        InfoUser existingUser = null;
        if (medecinDTO.getUser() != null) {
            InfoUserDto userDto = medecinDTO.getUser();
            existingUser = medecin.getUser();

            if (userDto.getPrenom() != null) {
                existingUser.setPrenom(userDto.getPrenom());
            }
            if (userDto.getNom() != null) {
                existingUser.setNom(userDto.getNom());
            }
            if (userDto.getEmail() != null) {
                existingUser.setEmail(userDto.getEmail());
            }
            if (userDto.getTelephone() != null) {
                existingUser.setTelephone(userDto.getTelephone());
            }
            if (userDto.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }


        }
        if (medecinDTO.getEstMedcinESJ() != null) {
            medecin.setEstMedcinESJ(medecinDTO.getEstMedcinESJ());
        }
        if (medecinDTO.getEstGeneraliste() != null) {
            medecin.setEstGeneraliste(medecinDTO.getEstGeneraliste());
        }
        if (medecinDTO.getSpecialite() != null) {
            medecin.setSpecialite(medecinDTO.getSpecialite());
        }
        userRepository.save(existingUser);
        medecinRepository.save(medecin);

        return  medecinMapper.fromMedecin(medecin);


    }


    @Override
    public void deleteMedecin(Long id) throws MedecinException {
        Medecin medecin = medecinRepository.findById(id).orElseThrow(() -> new MedecinException("not found"));
        verificationTokenRepository.deleteByUserId(medecin.getUser().getId());

        if (medecin != null){
            try {
                medecinRepository.delete(medecin);
            }catch (Exception e){
                throw  new MedecinException("error");
            }
        }else {
            throw new MedecinException("not found");
        }
    }
    private SimpleMailMessage constructEmail(String subject, String body, InfoUser user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }


}

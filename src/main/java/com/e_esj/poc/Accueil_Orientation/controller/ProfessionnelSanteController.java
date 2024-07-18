package com.e_esj.poc.Accueil_Orientation.controller;

import com.e_esj.poc.Accueil_Orientation.Dto.PasswordDto;
import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteDto;
import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteResponseDTO;
import com.e_esj.poc.Accueil_Orientation.config.ISecurityUserService;
import com.e_esj.poc.Accueil_Orientation.entity.*;
import com.e_esj.poc.Accueil_Orientation.exception.*;
import com.e_esj.poc.Accueil_Orientation.service.ProfessionnelSanteService;
import com.e_esj.poc.Accueil_Orientation.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/professionnelsantes")
public class ProfessionnelSanteController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProfessionnelSanteService professionnelSanteService;

    @Autowired
    UserService userService;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messages;


    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment env;

    @PostMapping("/registration")
    public ResponseEntity<?> registerUserAccount(@RequestBody @Valid ProfessionnelSante professionnelSante) throws CINNonValideException, PhoneNonValideException, EmailNonValideException {
        LOGGER.debug("Registering user account with information: {}", professionnelSante);
        System.out.print(professionnelSante);
        final ProfessionnelSante registered = professionnelSanteService.saveProfessionnelSante(professionnelSante);
        return ResponseEntity.ok(professionnelSante);
    }

    @GetMapping("/resendRegistrationToken")
    public ResponseEntity<?> resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final InfoUser user = userService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));

        Map<String, String> response = new HashMap<>();
        response.put("message", "Token renvoyé avec succès");

        return ResponseEntity.ok(response);
        // return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final InfoUser user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }


    private SimpleMailMessage constructEmail(String subject, String body, InfoUser user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionnelSante> getProfessionnelSante(@PathVariable Long id) throws CINNonValideException, PhoneNonValideException, EmailNonValideException {
        ProfessionnelSante professionnelSante = professionnelSanteService.getProfessionnelSante(id);
        return professionnelSante != null ? ResponseEntity.ok(professionnelSante) : ResponseEntity.notFound().build();

    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProfessionnelSante(@PathVariable Long id, @RequestBody ProfessionnelSanteDto updateProfessionnel) {
        try {
            ProfessionnelSanteResponseDTO updatedProSante = professionnelSanteService.updateProfessionnelSantePartial(id, updateProfessionnel);
            return ResponseEntity.ok(updatedProSante);
        } catch (ProfessionnelSanteException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/info")
    public ResponseEntity<String> getTokenInfo(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            // Decodage du token pour affichage des informations
            return ResponseEntity.ok("Token reçu : " + token);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Header Authorization non valide");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProferssionnelSante(@PathVariable Long id) throws ProfessionnelSanteException {
        try {
            professionnelSanteService.deleteProfessionnelSante(id);
            return ResponseEntity.ok("successfully deleted");
        } catch (ProfessionnelSanteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/pro")
    public ResponseEntity<List<ProfessionnelSanteResponseDTO>> getAllProfessionnelSante() {
            List<ProfessionnelSanteResponseDTO> professionnelSanteResponseDTOS = professionnelSanteService.findAllProfessionnelSante();
           return  ResponseEntity.ok(professionnelSanteResponseDTOS);



    }



  /*  @GetMapping("/registrationConfirm")
    public ModelAndView confirmRegistration(final HttpServletRequest request, final ModelMap model, @RequestParam("token") final String token) throws UnsupportedEncodingException {
        Locale locale = request.getLocale();
        model.addAttribute("lang", locale.getLanguage());
        final String result = professionnelSanteService.validateVerificationToken(token);
        if (result.equals("valid")) {
            final InfoUser user = professionnelSanteService.getUser(token);
            // if (user.isUsing2FA()) {
            // model.addAttribute("qr", userService.generateQRUrl(user));
            // return "redirect:/qrcode.html?lang=" + locale.getLanguage();
            // }
            authWithoutPassword(user);
            model.addAttribute("messageKey", "message.accountVerified");
            return new ModelAndView("redirect:/console", model);
        }

        model.addAttribute("messageKey", "auth.message." + result);
        model.addAttribute("expired", "expired".equals(result));
        model.addAttribute("token", token);
        return new ModelAndView("redirect:/badUser", model);
    }

    public void authWithoutPassword(InfoUser user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }*/

}




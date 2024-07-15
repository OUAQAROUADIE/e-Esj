package com.e_esj.poc.Accueil_Orientation.controller;

import com.e_esj.poc.Accueil_Orientation.Dto.PasswordDto;
import com.e_esj.poc.Accueil_Orientation.Dto.ProfessionnelSanteDto;
import com.e_esj.poc.Accueil_Orientation.config.ISecurityUserService;
import com.e_esj.poc.Accueil_Orientation.entity.*;
import com.e_esj.poc.Accueil_Orientation.exception.CINNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.EmailNonValideException;
import com.e_esj.poc.Accueil_Orientation.exception.InvalidOldPasswordException;
import com.e_esj.poc.Accueil_Orientation.exception.PhoneNonValideException;
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

public class ProfessionnelSanteController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProfessionnelSanteService professionnelSanteService;

    @Autowired
    UserService userService;

    /*@Autowired
    ISecurityUserService securityUserService;
*/
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
        final VerificationToken newToken = professionnelSanteService.generateNewVerificationToken(existingToken);
        final InfoUser user = professionnelSanteService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));

        Map<String, String> response = new HashMap<>();
        response.put("message", "Token renvoyé avec succès");

        return ResponseEntity.ok(response);
       // return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }

    @PostMapping("/user/resetPassword")
    public ResponseEntity<?> resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        final InfoUser user = userService.findUserByEmail(userEmail);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            professionnelSanteService.createPasswordResetTokenForUser(user, token);
            mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
        }else {
            throw new IllegalArgumentException("user not found");
        }
        Map<String, String> response = new HashMap<>();

        response.put("Réinitialisation du mot de passe envoyée à ",  userEmail);
        return ResponseEntity.ok(response);
    }
    // Save password
   /* @PostMapping("/user/savePassword")
    public ResponseEntity<?> savePassword(final Locale locale, @Valid @RequestBody PasswordDto passwordDto) {

        final String result = securityUserService.validatePasswordResetToken(passwordDto.getToken());

        if (result != null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", messages.getMessage("auth.message." + result, null, locale));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<InfoUser> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
        if (user.isPresent()) {
            professionnelSanteService.changeUserPassword(user.get(), passwordDto.getNewPassword());
            Map<String, String> response = new HashMap<>();
            response.put("message", messages.getMessage("message.resetPasswordSuc", null, locale));
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", messages.getMessage("auth.message.invalid", null, locale));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }*/

    // Change user password
    @PostMapping("/user/updatePassword")
    public GenericResponse changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) throws InvalidOldPasswordException {
        final InfoUser user = userService.findUserByEmail(((InfoUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        if (!professionnelSanteService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            throw new InvalidOldPasswordException("Invalid mote de passe");
        }
        professionnelSanteService.changeUserPassword(user, passwordDto.getNewPassword());
        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final InfoUser user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final InfoUser user) {
        final String url = contextPath + "/user/changePassword?token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
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
    public  ResponseEntity<ProfessionnelSante> getProfessionnelSante(@PathVariable Long id) throws CINNonValideException, PhoneNonValideException, EmailNonValideException {
        ProfessionnelSante professionnelSante = professionnelSanteService.getProfessionnelSante(id);
        return professionnelSante != null ? ResponseEntity.ok(professionnelSante) : ResponseEntity.notFound().build();

    }


  /*  @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<InfoUser> userOptional = securityUserService.authenticateAndGenerateToken(loginRequest.getEmail(), loginRequest.getPassword());
        if (userOptional.isPresent()) {
            InfoUser user = userOptional.get();
            String authToken = jwtUtil.generateToken(user.getEmail()); // Générer le token JWT avec l'email
            Map<String, Object> response = new HashMap<>();
            response.put("token", authToken);
            response.put("user", user); // Vous pouvez ajouter d'autres informations utilisateur nécessaires
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
*/
   /* // Endpoint pour la connexion
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Recherche de l'utilisateur par email
            InfoUser user = userService.findUserByEmail(loginRequest.getEmail());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Vérification du mot de passe encodé
            if (passwordEncoder.matches(passwordEncoder.encode(loginRequest.getPassword()), user.getPassword())) {
                // Génération du token d'authentification si les identifiants sont valides
                String authToken = securityUserService.authenticateAndGenerateToken(loginRequest.getEmail(), loginRequest.getPassword());

                if (authToken != null) {
                    return ResponseEntity.ok(Collections.singletonMap("token", authToken));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
*/
   /* @PutMapping("/{id}")
    public ResponseEntity<ProfessionnelSante> updateProfessionnelSante(@PathVariable Long id, @RequestBody ProfessionnelSante updatedDetails) {
        try {
            ProfessionnelSante updatedProSante = professionnelSanteService.updateProfessionnelSante(id, updatedDetails);
            return ResponseEntity.ok(updatedProSante);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
*/
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




package com.e_esj.poc.Accueil_Orientation.controller;

import com.e_esj.poc.Accueil_Orientation.exception.*;
import org.springframework.ui.Model;
import com.e_esj.poc.Accueil_Orientation.entity.*;
import com.e_esj.poc.Accueil_Orientation.service.JeuneService;
import com.e_esj.poc.Accueil_Orientation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/jeune")
public class JeuneController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private JeuneService jeuneService;

    @Autowired
    private UserService userService;

    private UserDetailsService userDetailsService;


    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messages;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment env;

    @PostMapping
    public ResponseEntity<?> saveJeune(@RequestBody Jeune jeune) throws PhoneNonValideException, EmailNonValideException, CINNonValideException, AgeNonValideException {
        Jeune savedJeune = jeuneService.saveJeune(jeune);
        return ResponseEntity.ok(savedJeune);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Jeune> updateJeune(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Jeune updatedJeune = jeuneService.updateJeune(id, updates);
        return ResponseEntity.ok(updatedJeune);
    }


    @PostMapping("/{jeuneId}/Antecedents/familiaux")
    public ResponseEntity<?> addAntecedentFamilial(@PathVariable Long jeuneId, @RequestBody AntecedentFamilial antecedentFamilial) {
        try {
            AntecedentFamilial savedAntecedentFamilial = jeuneService.addAntecedentFamilial(jeuneId, antecedentFamilial);
            return ResponseEntity.ok(savedAntecedentFamilial);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{JeuneId}/Antecedents/personnels")
    public ResponseEntity<?> addAntecedentPersonnel(@PathVariable Long JeuneId, @RequestBody AntecedentPersonnel antecedentPersonnel) {
        try {
            AntecedentPersonnel savedAntecedentPersonnel = jeuneService.addAntecedentPersonnel(JeuneId, antecedentPersonnel);
            return ResponseEntity.ok(savedAntecedentPersonnel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{jeuneId}/antecedents")
    public ResponseEntity<?> getAntecedent(@PathVariable Long jeuneId) throws JeuneException {
        try {
            Map<String, Object> result = jeuneService.getAntecedents(jeuneId);
            return ResponseEntity.ok(result);
        } catch (JeuneException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/user/resetPassword")
    public String resetPassword(final HttpServletRequest request, final Model model, @RequestParam("email") final String userEmail) {
        final InfoUser user = userService.findUserByEmail(userEmail);
        if (user == null) {
            model.addAttribute("message", messages.getMessage("message.userNotFound", null, request.getLocale()));
            return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
        }

        final String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        try {
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            final SimpleMailMessage email = constructResetTokenEmail(appUrl, request.getLocale(), token, user);
            mailSender.send(email);
        } catch (final Exception e) {
            LOGGER.debug(e.getLocalizedMessage(), e);
            model.addAttribute("message", e.getLocalizedMessage());
            return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
        }
        model.addAttribute("message", messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
        return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
    }

    @GetMapping("/user/changePassword")
    public String changePassword(final HttpServletRequest request, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final Locale locale = request.getLocale();

        final PasswordResetToken passToken = userService.getPasswordResetToken(token);
        if (passToken == null || passToken.getUser().getId() != id) {
            final String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/login.html?lang=" + locale.getLanguage();
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
            return "redirect:/login.html?lang=" + locale.getLanguage();
        }

        final Authentication auth = new UsernamePasswordAuthenticationToken(passToken.getUser(), null, userDetailsService.loadUserByUsername(passToken.getUser().getEmail()).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
    }

    @PostMapping("/user/savePassword")
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    public String savePassword(final HttpServletRequest request, final Model model, @RequestParam("password") final String password) {
        final Locale locale = request.getLocale();

        final InfoUser user = (InfoUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("message", messages.getMessage("message.resetPasswordSuc", null, locale));
        return "redirect:/login.html?lang=" + locale;
    }

    // NON-API

    private SimpleMailMessage constructResetVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final InfoUser user) {
        final String confirmationUrl = contextPath + "/old/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Resend Registration Token");
        email.setText(message + " \r\n" + confirmationUrl);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final InfoUser user) {
        final String url = contextPath + "/old/user/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
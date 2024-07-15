package com.e_esj.poc.Accueil_Orientation.config;

import com.e_esj.poc.Accueil_Orientation.repository.UserRepository;
import com.e_esj.poc.Accueil_Orientation.service.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class AccueilConfig {
/*
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    private LogoutSuccessHandler myLogoutSuccessHandler;

    private AuthenticationFailureHandler authenticationFailureHandler;




    @Autowired
    private UserRepository userRepository;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .securityContext((securityContext) -> securityContext.requireExplicitSave(true))
                .authorizeHttpRequests(authz -> {
                    authz.requestMatchers(HttpMethod.GET, "/roleHierarchy")
                            .hasRole("STAFF")
                            .requestMatchers("/login*", "/logout*", "/signin/**", "/signup/**", "/customLogin", "/user/registration*", "/registrationConfirm*", "/expiredAccount*", "/registration*", "/badUser*", "/user/resendRegistrationToken*", "/forgetPassword*",
                                    "/user/resetPassword*", "/user/savePassword*", "/updatePassword*", "/user/changePassword*", "/emailError*", "/resources/**", "/old/user/registration*", "/successRegister*", "/qrcode*", "/user/enableNewLoc*")
                            .permitAll()
                            .requestMatchers("/invalidSession*")
                            .anonymous()
                            .requestMatchers("/user/updatePassword*")
                            .hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
                            .requestMatchers("/console")
                            .hasAuthority("READ_PRIVILEGE")
                            .anyRequest()
                            .hasAuthority("READ_PRIVILEGE");
                })
                .formLogin((formLogin) -> formLogin.loginPage("/login")
                        .defaultSuccessUrl("/homepage.html")
                        .failureUrl("/login?error=true")
                        .successHandler(myAuthenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                        .permitAll())
                .sessionManagement((sessionManagement) -> sessionManagement.invalidSessionUrl("/invalidSession.html")
                        .maximumSessions(1)
                        .sessionRegistry(sessionRegistry()));

        return http.build();
    }
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }*/






}

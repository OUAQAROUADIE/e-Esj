package com.e_esj.poc.Accueil_Orientation.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.e_esj.poc.Accueil_Orientation.entity.PasswordResetToken;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);
}


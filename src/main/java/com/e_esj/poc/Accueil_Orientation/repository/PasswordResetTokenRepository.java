package com.e_esj.poc.Accueil_Orientation.repository;

import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByUser(InfoUser user);

    PasswordResetToken findByToken(String token);
}

package com.e_esj.poc.Accueil_Orientation.repository;

import com.e_esj.poc.Accueil_Orientation.entity.ProfessionnelSante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface ProfessionnelSanteRepository extends JpaRepository<ProfessionnelSante, Long> {
    @Query("SELECT p FROM ProfessionnelSante p WHERE p.cin = :cin")
    Optional<ProfessionnelSante> findByCin(@Param("cin") String cin);

    @Query("SELECT p FROM ProfessionnelSante p WHERE p.user.email = :email")
    ProfessionnelSante findByEmail(@Param("email") String email);

    @Query("SELECT p FROM ProfessionnelSante p WHERE p.cin = :recherche OR p.user.email = :recherche")
    Optional<ProfessionnelSante> findByCinOrEmail(@Param("recherche") String recherche);
}


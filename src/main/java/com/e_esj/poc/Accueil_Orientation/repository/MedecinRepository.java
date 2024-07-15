package com.e_esj.poc.Accueil_Orientation.repository;

import com.e_esj.poc.Accueil_Orientation.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MedecinRepository extends JpaRepository<Medecin,Long> {
    @Query("SELECT m FROM Medecin m WHERE m.cin = :cin")
    Optional<Medecin> findByCin(@Param("cin") String cin);

    @Query("SELECT m FROM Medecin m WHERE m.user.email = :mail")
    Optional<Medecin> findByMail(@Param("mail") String mail);

    @Query("SELECT m FROM Medecin m WHERE m.cin = :recherche OR m.user.email = :recherche")
    Optional<Medecin> findByCinOrMail(@Param("recherche") String recherche);
}
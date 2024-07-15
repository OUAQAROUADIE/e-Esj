package com.e_esj.poc.Accueil_Orientation.repository;

import com.e_esj.poc.Accueil_Orientation.entity.JeuneScolarise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JeuneScolariseRepository extends JpaRepository<JeuneScolarise, Long> {
}

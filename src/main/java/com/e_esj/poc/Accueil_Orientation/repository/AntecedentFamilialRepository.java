package com.e_esj.poc.Accueil_Orientation.repository;

import com.e_esj.poc.Accueil_Orientation.entity.AntecedentFamilial;
import com.e_esj.poc.Accueil_Orientation.entity.Jeune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AntecedentFamilialRepository extends JpaRepository<AntecedentFamilial, Long> {

    Optional<AntecedentFamilial> findByJeune(Jeune jeune);

    void deleteByJeune(Jeune jeune);
}

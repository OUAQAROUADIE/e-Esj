package com.e_esj.poc.Accueil_Orientation.repository;

import com.e_esj.poc.Accueil_Orientation.entity.AntecedentPersonnel;
import com.e_esj.poc.Accueil_Orientation.entity.Jeune;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AntecedentPersonnelRepository extends JpaRepository<AntecedentPersonnel, Long> {

    Optional<AntecedentPersonnel> findByJeune(Jeune jeune);

    void deleteByJeune(Jeune jeune);
}

package com.e_esj.poc.Accueil_Orientation.repository;

import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import com.e_esj.poc.Accueil_Orientation.entity.InfoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<InfoUser, Long> {
    Optional<InfoUser> findById(long id);
    InfoUser findByEmail(String email);


}

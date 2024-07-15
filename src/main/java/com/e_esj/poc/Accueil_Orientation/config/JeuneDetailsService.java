package com.e_esj.poc.Accueil_Orientation.config;

import com.e_esj.poc.Accueil_Orientation.entity.Jeune;
import com.e_esj.poc.Accueil_Orientation.repository.JeuneRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JeuneDetailsService implements UserDetailsService {
    JeuneRepository jeuneRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Jeune> jeuneOpt = jeuneRepository.findByMailOrCinOrCNEOrCodeMASSAR(username);

        if (jeuneOpt.isPresent()) {
            Jeune jeune = jeuneOpt.get();
            return User
                    .withUsername(username)
                    .password(jeune.getUser().getPassword())
                    .roles(jeune.getROLE()).build();
        } else {
            throw new UsernameNotFoundException("Jeune not found with username: " + username);
        }
    }
}
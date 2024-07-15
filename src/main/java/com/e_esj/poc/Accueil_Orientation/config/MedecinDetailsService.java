package com.e_esj.poc.Accueil_Orientation.config;

import com.e_esj.poc.Accueil_Orientation.entity.Medecin;
import com.e_esj.poc.Accueil_Orientation.repository.MedecinRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MedecinDetailsService implements UserDetailsService {
    MedecinRepository medecinRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Medecin> medecinOpt = medecinRepository.findByCinOrMail(username);

        if (medecinOpt.isPresent()) {
            Medecin medecin = medecinOpt.get();
            return User
                    .withUsername(username)
                    .password(medecin.getUser().getPassword())
                    .roles(medecin.getROLE()).build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
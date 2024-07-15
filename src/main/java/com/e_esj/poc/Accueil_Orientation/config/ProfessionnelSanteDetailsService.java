package com.e_esj.poc.Accueil_Orientation.config;

import com.e_esj.poc.Accueil_Orientation.entity.ProfessionnelSante;
import com.e_esj.poc.Accueil_Orientation.repository.ProfessionnelSanteRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfessionnelSanteDetailsService implements UserDetailsService {


    @Autowired
    ProfessionnelSanteRepository professionnelSanteRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ProfessionnelSante> professionnelSanteOpt = professionnelSanteRepository.findByCinOrEmail(username);

        System.out.print(professionnelSanteOpt);

        if(professionnelSanteOpt.isPresent()){
            ProfessionnelSante professionnelSante = professionnelSanteOpt.get();

            return User
                    .withUsername(username)
                    .password(professionnelSante.getUser().getPassword())
                    .roles(professionnelSante.getROLE()).build();
        }else {
            throw new UsernameNotFoundException("user not found");
        }


    }
}

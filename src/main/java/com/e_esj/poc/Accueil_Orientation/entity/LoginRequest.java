package com.e_esj.poc.Accueil_Orientation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String email;
    private String password;



}


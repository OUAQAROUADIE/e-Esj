package com.e_esj.poc.Accueil_Orientation.Dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Data
public class InfoUserDto {

    @NotEmpty
    private String nom;

    @NotEmpty
    private String prenom;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6, message = "Le mot de passe doit comporter au moins 6 caractères")
    private String password;

    @NotEmpty
    private String telephone;

    // Getters et Setters

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}

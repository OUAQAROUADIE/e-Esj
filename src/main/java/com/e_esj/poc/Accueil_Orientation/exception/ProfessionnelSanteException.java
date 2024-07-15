package com.e_esj.poc.Accueil_Orientation.exception;

public class ProfessionnelSanteException extends  Exception{
    public ProfessionnelSanteException(String message){
        super(message);
    }

    public ProfessionnelSanteException(String message, Throwable throwable){
        super(message, throwable);
    }
}

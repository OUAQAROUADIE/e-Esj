package com.e_esj.poc.Accueil_Orientation.exception;

public class MedecinException  extends Exception{
    public MedecinException(String message){
        super(message);
    }

    public  MedecinException(String message, Throwable throwable){
        super(message, throwable);
    }
}

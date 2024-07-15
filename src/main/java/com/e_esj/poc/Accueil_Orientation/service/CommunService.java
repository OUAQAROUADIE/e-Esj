package com.e_esj.poc.Accueil_Orientation.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommunService {

    public  static boolean isValidEmail(String email){
        String emailR = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailR);
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidMoroccanPhoneNumber(String phoneNumber) {
        String phoneR = "^(\\+212|0)([5-7])\\d{8}$";
        Pattern pattern = Pattern.compile(phoneR);
        if (phoneNumber == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    public static boolean isValidCIN(String cin) {
        String cinR = "^[A-Z]{1,2}[0-9]{3,7}$"; // Adjust the regex based on the exact format requirements
        Pattern pattern = Pattern.compile(cinR);
        if (cin == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(cin);
        return matcher.matches();
    }


}

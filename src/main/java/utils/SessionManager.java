package utils;

import java.util.Date;

public class SessionManager {
    // Attributes to store user information
    private static String prenom;
    private static String nom;
    private static String email;
    private static String identifiant;
    private static String role;
    private static boolean isAdmin;

    // Method to set all user information at login
    public static void setUserInfo(String prenom, String nom,
                                   String email,
                                   String identifiant,
                                   String role, boolean isAdmin) {
        SessionManager.prenom = prenom;
        SessionManager.nom = nom;
        SessionManager.email = email;
        SessionManager.identifiant = identifiant;
        SessionManager.role = role;
        SessionManager.isAdmin = isAdmin;
    }

    // Methods to get user information (getter methods)
    public static String getPrenom() {
        return prenom;
    }

    public static String getNom() {
        return nom;
    }

    public static String getEmail() {
        return email;
    }

    public static String getIdentifiant() {
        return identifiant;
    }

    public static String getStatus() {
        return role;
    }

    public static boolean isAdmin() { return isAdmin; }

    // Method to clear session (log out)
    public static void clearSession() {
        prenom = null;
        nom = null;
        email = null;
        identifiant = null;
        role = null;
        isAdmin = false;
    }

    // Method to check if the user is logged in
    public static boolean isUtilisateurConnecte() {
        return prenom != null && nom != null && identifiant != null;
    }

    public static String setUserInfo(String identifiant) {
        SessionManager.identifiant = identifiant;
        return identifiant;
    }
}

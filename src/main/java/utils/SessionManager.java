package utils;

import java.util.Date;

public class SessionManager {
    // Attributes to store user information
    private static String userId;
    private static String prenom;
    private static String nom;
    private static String dateNaissance;
    private static String telephone;
    private static String email;
    private static String adresse;
    private static String identifiant;
    private static String status;
    private static boolean estSuperAdmin;

    // Method to set all user information at login
    public static void setUserInfo(String prenom, String nom,
                                   String telephone, String email,
                                   String identifiant,
                                   String status, boolean estSuperAdmin) {
        SessionManager.prenom = prenom;
        SessionManager.nom = nom;
        SessionManager.telephone = telephone;
        SessionManager.email = email;
        SessionManager.identifiant = identifiant;
        SessionManager.status = status;
        SessionManager.estSuperAdmin = estSuperAdmin;
    }

    // Methods to get user information (getter methods)
    public static String getPrenom() {
        return prenom;
    }

    public static String getNom() {
        return nom;
    }

    public static String getTelephone() {
        return telephone;
    }

    public static String getEmail() {
        return email;
    }

    public static String getIdentifiant() {
        return identifiant;
    }

    public static String getStatus() {
        return status;
    }

    public static boolean isEstSuperAdmin() {
        return estSuperAdmin;
    }

    // Method to clear session (log out)
    public static void clearSession() {
        userId = null;
        prenom = null;
        nom = null;
        telephone = null;
        email = null;
        identifiant = null;
        status = null;
        estSuperAdmin = false;
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

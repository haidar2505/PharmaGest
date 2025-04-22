package models;

import java.util.Date;

public class Utilisateur {
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String identifiant;
    private String motDePasse;
    private String role;
    private boolean isAdmin;
    private Date lastLogin;

    // Colonnes
    public Utilisateur(String nom, String prenom, String email, String identifiant, String role, boolean isAdmin) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.identifiant = identifiant;
        this.role = role;
        this.isAdmin = isAdmin;
    }

    // Ajouter
    public Utilisateur(String nom, String prenom, String email, String identifiant, String motDePasse, String role, boolean isAdmin, Date lastLogin) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.role = role;
        this.isAdmin = isAdmin;
        this.lastLogin = lastLogin;
    }

    // Modifier
    public Utilisateur(String nom, String prenom, String email, String identifiant, String motDePasse, String role, boolean isAdmin) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.role = role;
        this.isAdmin = isAdmin;
    }

    // Getters and Setters
    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIdentifiant() { return identifiant; }
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean getIsAdmin() {
        return isAdmin;
    }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    public Date getlastLogin() {
        return lastLogin;
    }
    public void setlastLogin(Date dateCommande) { this.lastLogin = lastLogin; }
}
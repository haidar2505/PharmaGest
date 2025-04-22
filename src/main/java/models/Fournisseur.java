package models;

public class Fournisseur {
    private Integer idFournisseur;
    private String nom;
    private String adresse;
    private String email;
    private String pays;

    // Constructeur for create fournisseur
    public Fournisseur(String nom, String email, String adresse, String pays) {
        this.nom = nom;
        this.pays = pays;
        this.email = email;
        this.adresse = adresse;
    }

    // Constructeur for update, retrieve all founisseurs or delete fournisseur
    public Fournisseur(Integer idFournisseur, String nom, String email, String adresse, String pays) {
        this.idFournisseur = idFournisseur;
        this.nom = nom;
        this.email = email;
        this.adresse = adresse;
        this.pays = pays;
    }

    public Fournisseur(int getidFournisseur) {
        this.idFournisseur = getidFournisseur;
    }

    public Fournisseur(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Integer getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(Integer idFournisseur) {
        this.idFournisseur = idFournisseur;
    }



}

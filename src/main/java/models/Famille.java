package models;

public class Famille {
    private int idFamille;
    private String nom;

    // Constructors
    public Famille() {
    }

    public Famille(int idFamille, String nom) {
        this.idFamille = idFamille;
        this.nom = nom;
    }

    public Famille(int idFamille) {
        this.idFamille = idFamille;
    }

    public Famille(String nomFamille) {
        this.nom = nomFamille;
    }

    // Getters and Setters
    public int getIdFamille() {
        return idFamille;
    }

    public void setIdFamille(int idFamille) {
        this.idFamille = idFamille;
    }

    public String getNomFamille() {
        return nom;
    }

    public void setNomFamille(String nom) {
        this.nom = nom;
    }
}

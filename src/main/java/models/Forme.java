package models;

public class Forme {
    private int idForme;
    private String nom;

    // Constructors
    public Forme() {
    }

    public Forme(int idForme, String nom) {
        this.idForme = idForme;
        this.nom = nom;
    }

    public Forme(int getidForme) {
        this.idForme = getidForme;
    }

    public Forme(String nomForme) {
        this.nom = nomForme;
    }

    // Getters and Setters
    public int getIdForme() {
        return idForme;
    }

    public void setIdForme(int idForme) {
        this.idForme = idForme;
    }

    public String getNomForme() {
        return nom;
    }

    public void setNomForme(String nom) {
        this.nom = nom;
    }

}


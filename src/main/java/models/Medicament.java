package models;

public class Medicament {
    private int idMedicament;
    private String dci;
    private String dosage;
    private double puAchat;
    private double puVente;
    private int stock;
    private int stockMin;
    private int stockMax;
    private boolean ordonnance;

    // Object references
    private Forme forme;
    private Famille famille;
    private Fournisseur fournisseur;

    // ID fields (kept for backward compatibility)
    private int idForme;
    private int idFamille;
    private int idFournisseur;

    // Constructors
    public Medicament() {
    }

    // Constructor with IDs
    public Medicament(int idMedicament, String dci, String dosage, double puAchat, double puVente,
                      int stock, int stockMin, int stockMax, boolean ordonnance,
                      Fournisseur fournisseur, Famille famille, Forme forme) {
        this.idMedicament = idMedicament;
        this.dci = dci;
        this.dosage = dosage;
        this.puAchat = puAchat;
        this.puVente = puVente;
        this.stock = stock;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        this.ordonnance = ordonnance;
        this.fournisseur = fournisseur;
        this.famille = famille;
        this.forme = forme;
    }

    public Medicament(String dci, String dosage, double puAchat, double puVente,
                      int stock, int stockMin, int stockMax, boolean ordonnance,
                      int idForme, int idFamille, int idFournisseur) {
        this.dci = dci;
        this.dosage = dosage;
        this.puAchat = puAchat;
        this.puVente = puVente;
        this.stock = stock;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        this.ordonnance = ordonnance;
        this.idForme = idForme;
        this.idFamille = idFamille;
        this.idFournisseur = idFournisseur;
    }

    // Constructor with objects
    public Medicament(int idMedicament, String dci, double puAchat, double puVente,
                      int stock, int stockMin, int stockMax, boolean ordonnance,
                      Fournisseur fournisseur, Famille famille, Forme forme) {
        this.idMedicament = idMedicament;
        this.dci = dci;
        this.puAchat = puAchat;
        this.puVente = puVente;
        this.stock = stock;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        this.ordonnance = ordonnance;
        this.fournisseur = fournisseur;
        this.famille = famille;
        this.forme = forme;
        this.idFournisseur = fournisseur != null ? fournisseur.getIdFournisseur() : 0;
        this.idFamille = famille != null ? famille.getIdFamille() : 0;
        this.idForme = forme != null ? forme.getIdForme() : 0;
    }

    public Medicament(int idMedicament, String dci, double prixunitAchat, double prixunitVente) {
        this.idMedicament = idMedicament;
        this.dci = dci;
        this.puAchat = prixunitAchat;
        this.puVente = prixunitVente;
    }

    // Getters and Setters for all fields
    public int getIdMedicament() {
        return idMedicament;
    }

    public void setIdMedicament(int idMedicament) {
        this.idMedicament = idMedicament;
    }

    public String getDci() {
        return dci;
    }

    public void setDci(String dci) {
        this.dci = dci;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public double getPuAchat() {
        return puAchat;
    }

    public void setPuAchat(double puAchat) {
        this.puAchat = puAchat;
    }

    public double getPuVente() {
        return puVente;
    }

    public void setPuVente(double puVente) {
        this.puVente = puVente;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockMin() {
        return stockMin;
    }

    public void setStockMin(int stockMin) {
        this.stockMin = stockMin;
    }

    public int getStockMax() {
        return stockMax;
    }

    public void setStockMax(int stockMax) {
        this.stockMax = stockMax;
    }

    public boolean isOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(boolean ordonnance) {
        this.ordonnance = ordonnance;
    }

    // Object reference getters and setters
    public Forme getForme() {
        return forme;
    }

    public void setForme(Forme forme) {
        this.forme = forme;
        this.idForme = forme != null ? forme.getIdForme() : 0;
    }

    public Famille getFamille() {
        return famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
        this.idFamille = famille != null ? famille.getIdFamille() : 0;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
        this.idFournisseur = fournisseur != null ? fournisseur.getIdFournisseur() : 0;
    }

    // ID field getters and setters (maintained for compatibility)
    public int getIdFournisseur() {
        return fournisseur != null ? fournisseur.getIdFournisseur() : idFournisseur;
    }

    public void setIdFournisseur(int idFournisseur) {
        this.idFournisseur = idFournisseur;
        if (this.fournisseur != null) {
            this.fournisseur.setIdFournisseur(idFournisseur);
        }
    }

    public int getIdFamille() {
        return famille != null ? famille.getIdFamille() : idFamille;
    }

    public void setIdFamille(int idFamille) {
        this.idFamille = idFamille;
        if (this.famille != null) {
            this.famille.setIdFamille(idFamille);
        }
    }

    public int getIdForme() {
        return forme != null ? forme.getIdForme() : idForme;
    }

    public void setIdForme(int idForme) {
        this.idForme = idForme;
        if (this.forme != null) {
            this.forme.setIdForme(idForme);
        }
    }
}
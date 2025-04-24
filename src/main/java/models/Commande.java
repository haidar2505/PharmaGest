package models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Commande {
    private String numCommande;
    private int qteCommande;
    private BigDecimal montant;
    private LocalDate dateCommande;
    private LocalDate dateLivre;
    private boolean statutCommande;
    private int qteRecu;
    private int idMedicament;

    private String dci;
    private int stock;
    private int stockMin;
    private int stockMax;
    private double puVente;
    private String nom;


    // Constructor
    public Commande() {
    }

    public Commande(String numCommande, int qteCommande, BigDecimal montant, LocalDate dateCommande,
                    LocalDate dateLivre, boolean statutCommande, int qteRecu, int idMedicament) {
        this.numCommande = numCommande;
        this.qteCommande = qteCommande;
        this.montant = montant;
        this.dateCommande = dateCommande;
        this.dateLivre = dateLivre;
        this.statutCommande = statutCommande;
        this.qteRecu = qteRecu;
        this.idMedicament = idMedicament;
    }

    public Commande(String numCommande, int qteCommande, BigDecimal montant, String dci, int stock, int stockMin, int stockMax, double puVente, String nom) {
        this.numCommande = numCommande;
        this.qteCommande = qteCommande;
        this.montant = montant;
        this.dci = dci;
        this.stock = stock;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        this.puVente = puVente;
        this.nom = nom;
    }

    public Commande(String numCommande, int qteCommande, BigDecimal montant, int idMedicament, String dci, int stock, int stockMin, int stockMax, double puVente, String nom) {
        this.numCommande = numCommande;
        this.qteCommande = qteCommande;
        this.montant = montant;
        this.idMedicament = idMedicament;
        this.dci = dci;
        this.stock = stock;
        this.stockMin = stockMin;
        this.stockMax = stockMax;
        this.puVente = puVente;
        this.nom = nom;
    }

    // Getters and Setters
    public String getNumCommande() {
        return numCommande;
    }

    public void setNumCommande(String numCommande) {
        this.numCommande = numCommande;
    }

    public int getQteCommande() {
        return qteCommande;
    }

    public void setQteCommande(int qteCommande) {
        this.qteCommande = qteCommande;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDate getDateLivre() {
        return dateLivre;
    }

    public void setDateLivre(LocalDate dateLivre) {
        this.dateLivre = dateLivre;
    }

    public boolean isStatutCommande() {
        return statutCommande;
    }

    public void setStatutCommande(boolean statutCommande) {
        this.statutCommande = statutCommande;
    }

    public int getQteRecu() {
        return qteRecu;
    }

    public void setQteRecu(int qteRecu) {
        this.qteRecu = qteRecu;
    }

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

    public double getPuVente() {
        return puVente;
    }

    public void setPuVente(double puVente) {
        this.puVente = puVente;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
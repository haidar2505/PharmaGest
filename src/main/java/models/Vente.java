package models;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Vente {
    private String dci;
    private double puVente;
    private String numVente;
    private int qteDemande;
    private BigDecimal montant;
    private LocalDate dateVente;
    private boolean statutPaiement;
    private int idMedicament;
    private String numPrescription;
    private String numFacture;

    // Constructors
    public Vente() {
    }

    public Vente(String numVente, int qteDemande, BigDecimal montant, LocalDate dateVente,
                 boolean statutPaiement, int idMedicament, String numPrescription, String numFacture) {
        this.numVente = numVente;
        this.qteDemande = qteDemande;
        this.montant = montant;
        this.dateVente = dateVente;
        this.statutPaiement = statutPaiement;
        this.idMedicament = idMedicament;
        this.numPrescription = numPrescription;
        this.numFacture = numFacture;
    }

    public Vente(int qteDemande, BigDecimal montant, boolean statutPaiement, int idMedicament) {
        this.qteDemande = qteDemande;
        this.montant = montant;
        this.statutPaiement = statutPaiement;
        this.idMedicament = idMedicament;
    }

    public Vente(int idMedicament, String dci, double puVente, int qteDemande, BigDecimal montant) {
        this.idMedicament = idMedicament;
        this.dci = dci;
        this.puVente = puVente;
        this.qteDemande = qteDemande;
        this.montant = montant;
    }

    // Getters and Setters
    public String getDci() {
        return dci;
    }

    public void setDci(String dci) {
        this.dci = dci;
    }

    public double getPuVente() {
        return puVente;
    }

    public void setPuVente(String puVente) {
        this.puVente = Double.parseDouble(puVente);
    }

    public String getNumVente() {
        return numVente;
    }

    public void setNumVente(String numVente) {
        this.numVente = numVente;
    }

    public int getQteDemande() {
        return qteDemande;
    }

    public void setQteDemande(int qteDemande) {
        this.qteDemande = qteDemande;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDate dateVente) {
        this.dateVente = dateVente;
    }

    public boolean isStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(boolean statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public int getIdMedicament() {
        return idMedicament;
    }

    public void setIdMedicament(int idMedicament) {
        this.idMedicament = idMedicament;
    }

    public String getNumPrescription() {
        return numPrescription;
    }

    public void setNumPrescription(String numPrescription) {
        this.numPrescription = numPrescription;
    }

    public String getNumFacture() {
        return numFacture;
    }

    public void setNumFacture(String numFacture) {
        this.numFacture = numFacture;
    }
}
package models;

import java.time.LocalDate;

public class Prescription {
    private String numPrescription;
    private String nomMedecin;
    private String prenomMedecin;
    private String nomPatient;
    private String prenomPatient;
    private LocalDate datePrescription;

    // Constructors
    public Prescription() {
    }

    public Prescription(String numPrescription, String nomMedecin, String prenomMedecin,
                        String nomPatient, String prenomPatient, LocalDate datePrescription) {
        this.numPrescription = numPrescription;
        this.nomMedecin = nomMedecin;
        this.prenomMedecin = prenomMedecin;
        this.nomPatient = nomPatient;
        this.prenomPatient = prenomPatient;
        this.datePrescription = datePrescription;
    }

    public Prescription(String nomPatient, String prenomPatient, String nomMedecin, String prenomMedecin) {
        this.nomMedecin = nomMedecin;
        this.prenomMedecin = prenomMedecin;
        this.nomPatient = nomPatient;
        this.prenomPatient = prenomPatient;
    }

    // Getters and Setters
    public String getNumPrescription() {
        return numPrescription;
    }

    public void setNumPrescription(String numPrescription) {
        this.numPrescription = numPrescription;
    }

    public String getNomMedecin() {
        return nomMedecin;
    }

    public void setNomMedecin(String nomMedecin) {
        this.nomMedecin = nomMedecin;
    }

    public String getPrenomMedecin() {
        return prenomMedecin;
    }

    public void setPrenomMedecin(String prenomMedecin) {
        this.prenomMedecin = prenomMedecin;
    }

    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    public String getPrenomPatient() {
        return prenomPatient;
    }

    public void setPrenomPatient(String prenomPatient) {
        this.prenomPatient = prenomPatient;
    }

    public LocalDate getDatePrescription() {
        return datePrescription;
    }

    public void setDatePrescription(LocalDate datePrescription) {
        this.datePrescription = datePrescription;
    }

    // Utility Methods
    public String getMedecinComplet() {
        return prenomMedecin + " " + nomMedecin;
    }

    public String getPatientComplet() {
        return prenomPatient + " " + nomPatient;
    }
}
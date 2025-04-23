package models;

public class Facture {
    private String numFacture;
    private String pdfFile;
    private String numVente;

    // Constructors
    public Facture() {
    }

    public Facture(String numFacture, String pdfFile, String numVente) {
        this.numFacture = numFacture;
        this.pdfFile = pdfFile;
        this.numVente = numVente;
    }

    public Facture(String facturePDF) {
        this.pdfFile = facturePDF;
    }

    // Getters and Setters
    public String getNumFacture() {
        return numFacture;
    }

    public void setNumFacture(String numFacture) {
        this.numFacture = numFacture;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getNumVente() {
        return numVente;
    }

    public void setNumVente(String numVente) {
        this.numVente = numVente;
    }

}

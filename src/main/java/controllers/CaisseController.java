package controllers;

import DAO.VenteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Facture;
import models.Vente;
import utils.Utils;
import utils.ValidationUtils;

import java.io.IOException;
import java.sql.SQLException;

public class CaisseController {
    Utils sceneLoader = new Utils();

    @FXML private Button dashboardButton;
    public void dashboardButtonOnAction(ActionEvent e) throws IOException {
        sceneLoader.loadScene("Dashboard.fxml", "Dashboard", dashboardButton);
    }

    @FXML private TextField montantDonneField;

    @FXML private Label clientLabel;
    @FXML private Label monnaieLabel;
    @FXML private Label totalLabel;
    @FXML private Label montantFieldError;

    @FXML private TextField numVenteField;
    @FXML private TextField medicamentSearchField;
    @FXML private TextField quantiteField;

    @FXML private TableView<Vente> caisseTable;
    @FXML private TableColumn<Vente, String> numVenteColumn;
    @FXML private TableColumn<Vente, String> medicamentColumn;
    @FXML private TableColumn<Vente, String> quantiteColumn;
    @FXML private TableColumn<Vente, String> puVenteColumn;
    @FXML private TableColumn<Vente, String> totalColumn;

    public void initialize() throws SQLException {
        if(caisseTable != null){

            numVenteColumn.setCellValueFactory(new PropertyValueFactory<>("numVente"));
            medicamentColumn.setCellValueFactory(new PropertyValueFactory<>("dci"));
            quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("qteDemande"));
            puVenteColumn.setCellValueFactory(new PropertyValueFactory<>("puVente"));
            totalColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));

            caisseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    numVenteField.setText(String.valueOf(newSelection.getIdMedicament()));
                    totalLabel.setText(String.valueOf(newSelection.getMontant()));
                }
            });

            loadCaisseTable();

            montantDonneField.textProperty().addListener((obs, oldVal, newVal) -> {
                updateMonnaieRendu();
            });
            updateMonnaieRendu();
        }
    }

    public boolean isRowSelected() {
        return caisseTable.getSelectionModel().getSelectedItem() != null;
    }
    
    private ObservableList<Vente> nonVenduList = FXCollections.observableArrayList();

    private void loadCaisseTable() throws SQLException{
        VenteDAO venteDAO = new VenteDAO();
        nonVenduList = venteDAO.getAllNonVendu();
        caisseTable.setItems(nonVenduList);
    }

    public void validerButtonOnAction(ActionEvent e) throws SQLException{
        if(isRowSelected()){
            boolean isInvalid = false;
            double montant = Double.parseDouble(totalLabel.getText());

            if (ValidationUtils.validatePrice(montantDonneField, montantFieldError)){
                isInvalid = true;
            }else{
                double monnaieDonne = Double.parseDouble(montantDonneField.getText());
                if(montant > monnaieDonne) {
                    montantFieldError.setText("Invalide");
                    isInvalid = true;
                }
            }

            if(!isInvalid){
                VenteDAO venteDAO = new VenteDAO();

                String numVente = numVenteField.getText();

                String facturePDF = "facture_pdf";
                Facture ajouterFacture = new Facture(facturePDF, numVente);
                if(venteDAO.validerVente(numVente) && venteDAO.addFacture(ajouterFacture)){
                    loadCaisseTable();
                    clearForm();
                }
            }
        }else{
            montantFieldError.setText("Entrez la monnaie donnée par le client");
        }
    }

    private void updateMonnaieRendu() {
        if(isRowSelected()){
            double montant = Double.parseDouble(totalLabel.getText());

            if (montantDonneField.getText().isEmpty()){
                monnaieLabel.setText("");
            }else{
                double monnaieDonne = Double.parseDouble(montantDonneField.getText());
                if(montant > monnaieDonne) {
                    monnaieLabel.setText("");
                }
                double monnaieRendu = monnaieDonne - montant ;
                monnaieLabel.setText(String.format("%,.2f €", monnaieRendu));
            }
        }else{
            monnaieLabel.setText("");
        }
    }

    private void clearForm(){
        montantDonneField.clear();

        monnaieLabel.setText("");
        montantFieldError.setText("");
        monnaieLabel.setText("");
    }
}

package controllers;

import DAO.MedicamentDAO;
import DAO.VenteDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import models.*;
import models.Vente;
import models.Medicament;
import utils.Utils;
import utils.ValidationUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Objects;

public class VenteController {
    Utils sceneLoader = new Utils();

    @FXML private Button dashboardButton;
    public void dashboardButtonOnAction(ActionEvent e) throws IOException {
        sceneLoader.loadScene("Dashboard.fxml", "Dashboard", dashboardButton);
    }

    public void annulerVenteButtonOnAction(ActionEvent e) throws IOException {
        clearForm();
    }

    @FXML private TextField nomPatientField;
    @FXML private TextField prenomPatientField;
    @FXML private TextField nomMedecinField;
    @FXML private TextField prenomMedecinField;

    @FXML private Label nomPatientError;
    @FXML private Label prenomPatientError;
    @FXML private Label nomMedecinError;
    @FXML private Label prenomMedecinError;


    @FXML private TextField idMedicamentField;
    @FXML private TextField medicamentSearchField;
    @FXML private TextField quantiteField;

    @FXML private TextField idMedicamentPanierField;

    @FXML private Label searchError;
    @FXML private Label typeVenteError;
    @FXML private Label quantiteError;

    @FXML private Label totalLabel;

    @FXML private GridPane infoGridPane;

    @FXML private TableView<Medicament> selectionTable;
    @FXML private TableColumn<Medicament, String> idColumn;
    @FXML private TableColumn<Medicament, String> medicamentColumn;
    @FXML private TableColumn<Medicament, String> formeColumn;
    @FXML private TableColumn<Medicament, String> familleColumn;
    @FXML private TableColumn<Medicament, String> dosageColumn;
    @FXML private TableColumn<Medicament, String> qteStockColumn;
    @FXML private TableColumn<Medicament, String> puVenteColumn;

    @FXML private TableView<Vente> validationTable;
    @FXML private TableColumn<Vente, String> idmedicamentPanierColumn;
    @FXML private TableColumn<Vente, String> medicamentPanierColumn;
    @FXML private TableColumn<Vente, String> qtePanierColumn;
    @FXML private TableColumn<Vente, String> prixPanierColumn;
    @FXML private TableColumn<Vente, String> totalPanierColumn;

    @FXML private ComboBox<String> typeVenteComboBox;

    private final ObservableList<String> vente = FXCollections.observableArrayList("Libre", "Prescription");

    public void initialize() throws SQLException{
        if(selectionTable != null && validationTable != null){
            typeVenteComboBox.setItems(vente);
            typeVenteComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null || newVal.trim().equals("Libre")) {
                    infoGridPane.setVisible(false);
                    infoGridPane.setManaged(false);
                } else {
                    infoGridPane.setVisible(true);
                    infoGridPane.setManaged(true);
                }
            });

            idColumn.setCellValueFactory(new PropertyValueFactory<>("idMedicament"));
            medicamentColumn.setCellValueFactory(new PropertyValueFactory<>("dci"));
            formeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getForme().getNomForme()));
            familleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFamille().getNomFamille()));
            dosageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDosageComplet()));
            qteStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            puVenteColumn.setCellValueFactory(new PropertyValueFactory<>("puVente"));

            selectionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    idMedicamentField.setText(String.valueOf(newSelection.getIdMedicament()));
                    medicamentSearchField.setText(String.valueOf(newSelection.getDci()));
                }
            });

            validationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    idMedicamentPanierField.setText(String.valueOf(newSelection.getIdMedicament()));
                    totalLabel.setText(String.valueOf(newSelection.getMontant()));
                }
            });

            idmedicamentPanierColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            medicamentPanierColumn.setCellValueFactory(new PropertyValueFactory<>("dci"));
            qtePanierColumn.setCellValueFactory(new PropertyValueFactory<>("qteDemande"));
            prixPanierColumn.setCellValueFactory(new PropertyValueFactory<>("puVente"));
            totalPanierColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));

            loadSelectionTable();
            loadPanierTable();
        }
    }

    public boolean isRowSelected() {
        return selectionTable.getSelectionModel().getSelectedItem() != null;
    }

    public boolean isRowSelectedPanier() {
        return validationTable.getSelectionModel().getSelectedItem() != null;
    }

    private ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();

    private void loadSelectionTable() throws SQLException{
        MedicamentDAO medicamentDAO = new MedicamentDAO();
        medicamentList = medicamentDAO.getAllMedicament();
        selectionTable.setItems(medicamentList);
    }

    private ObservableList<Vente> nonVenduList = FXCollections.observableArrayList();

    private void loadPanierTable() throws SQLException{
        VenteDAO venteDAO = new VenteDAO();
        nonVenduList = venteDAO.getAllPanier();
        validationTable.setItems(nonVenduList);
    }

    public void searchButtonOnAction(ActionEvent e) throws SQLException {
        ObservableList<Medicament> filteredList = FXCollections.observableArrayList();
        MedicamentDAO medicamentDAO = new MedicamentDAO();
        medicamentList = medicamentDAO.getAllMedicament();

        String search = medicamentSearchField.getText().trim();
        String lowerCaseFilter = search.toLowerCase();

        for (Medicament medicament : medicamentList) {
            if(medicament.getDci().toLowerCase().contains(lowerCaseFilter)){
                filteredList.add(medicament);
            }
            selectionTable.setItems(filteredList);
        }
    }

    public void ajouterVenteButtonOnAction(ActionEvent e) throws SQLException {
        if(isRowSelected()){
            MedicamentDAO medicamentDAO = new MedicamentDAO();

            boolean isInvalid = false;

            if(typeVenteComboBox.getValue() == null){
                typeVenteError.setText("Définir le type de vente");
                isInvalid = true;
            }else if(Objects.equals(typeVenteComboBox.getValue(), "Prescription")){
                if (ValidationUtils.validateName(nomPatientField, nomPatientError)) isInvalid = true;
                if (ValidationUtils.validateName(prenomPatientField, prenomPatientError)) isInvalid = true;
                if (ValidationUtils.validateName(nomMedecinField, nomMedecinError)) isInvalid = true;
                if (ValidationUtils.validateName(prenomMedecinField, prenomMedecinError)) isInvalid = true;
            }

            if(quantiteField.getText().isEmpty()){
                quantiteError.setText("Donner une quantité");
                isInvalid = true;
            }else if (ValidationUtils.validateQteStock(quantiteField, quantiteError)){
                 isInvalid = true;
            }else{
                if(idMedicamentField.getText().isEmpty()){
                    searchError.setText("Error");
                    isInvalid = true;
                }else{
                    int idMedStock = Integer.parseInt(idMedicamentField.getText().trim());
                    ObservableList<Medicament> stockList = medicamentDAO.getMedicamentStock(idMedStock);
                    Medicament stockData = stockList.get(0);

                    int qteStock = Integer.parseInt(quantiteField.getText());
                    int currentStock = stockData.getStock();
                    int stockMin = stockData.getStockMin();
                    int stockMax = stockData.getStockMax();

                    int suffiecientStock = currentStock - qteStock;

                    if (qteStock >= currentStock || stockMin >= suffiecientStock) {
                        quantiteError.setText("Stock insufficient");
                        isInvalid = true;
                    }
                }
            }

            if(!isInvalid){
                VenteDAO venteDAO = new VenteDAO();

                int idMedicament = Integer.parseInt(idMedicamentField.getText());
                int qteDemande = Integer.parseInt(quantiteField.getText());
                String typeVente = typeVenteComboBox.getValue().trim();
                double puVente = medicamentDAO.getMedicamentPUvente(idMedicament);
                BigDecimal montant = BigDecimal.valueOf(puVente * qteDemande);
                String facturePDF = "facture_pdf";

                ObservableList<Medicament> stockList = medicamentDAO.getMedicamentStock(idMedicament);
                Medicament stockData = stockList.get(0);
                int currentStock = stockData.getStock();

                int newStock = currentStock - qteDemande;

                if(typeVente.equals("Prescription")){
                    String nomPatient = nomPatientField.getText().trim();
                    String prenomPatient = prenomPatientField.getText().trim();
                    String nomMedecin = nomMedecinField.getText().trim();
                    String prenomMedecin = prenomMedecinField.getText().trim();
                    Prescription ajouterPrescription = new Prescription(nomPatient, prenomPatient, nomMedecin, prenomMedecin);
                    Vente ajouterVente = new Vente(qteDemande, montant, false, idMedicament);
                    Facture ajouterFacture = new Facture(facturePDF);
                    if (venteDAO.addPrescription(ajouterPrescription) && venteDAO.updateNewStock(idMedicament, newStock) && venteDAO.addVente(ajouterVente)) {
                        loadSelectionTable();
                    }else{
                        searchError.setText("Failed");
                    }
                }else if(typeVente.equals("Libre")){
                    Vente ajouterVente = new Vente(qteDemande, montant, false, idMedicament);
                    Facture ajouterFacture = new Facture(facturePDF);
                    if (venteDAO.addVente(ajouterVente) && venteDAO.updateNewStock(idMedicament, newStock)) {
                        loadSelectionTable();
                        loadPanierTable();
                    }
                }
            }
        }else{
            searchError.setText("Choisir un médicament");
        }
    }

    public void validerVenteButtonOnAction(ActionEvent e) throws SQLException {
        if(isRowSelectedPanier()) {
            int idMedicamentPanier = Integer.parseInt(idMedicamentPanierField.getText());

            VenteDAO venteDAO = new VenteDAO();

            if (venteDAO.validerVentePanier(idMedicamentPanier)) {
                loadSelectionTable();
                loadPanierTable();
                clearForm();
            } else {
                searchError.setText("Failed");
            }
        }else{
            totalLabel.setText("Choisir un médicament");
        }
    }

    private void clearForm(){
        nomPatientField.clear();
        prenomPatientField.clear();
        nomMedecinField.clear();
        prenomMedecinField.clear();
        idMedicamentField.clear();
        medicamentSearchField.clear();
        quantiteField.clear();
        idMedicamentPanierField.clear();

        nomPatientError.setText("");
        prenomPatientError.setText("");
        nomMedecinError.setText("");
        prenomMedecinError.setText("");
        searchError.setText("");
        typeVenteError.setText("");
        quantiteError.setText("");
        totalLabel.setText("");
    }




}


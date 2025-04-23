package controllers;

import DAO.MedicamentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Forme;
import models.Medicament;
import models.Utilisateur;
import utils.Utils;
import utils.ValidationUtils;
import java.io.IOException;
import java.sql.SQLException;

public class ListePrixController {
    Utils sceneLoader = new Utils();

    @FXML public Button maintenanceButton;
    public void maintenanceButtonOnAction(ActionEvent e) throws IOException {
        sceneLoader.loadScene("Maintenance.fxml", "Maintenance", maintenanceButton);
    }

    public void searchAnnulerButtonOnAction(ActionEvent e) throws SQLException {
        searchField.clear();
    }

    @FXML private TableView<Medicament> listPrixTable;
    @FXML private TableColumn<Utilisateur, String> idColumn;
    @FXML private TableColumn<Utilisateur, String> medicamentColumn;
    @FXML private TableColumn<Utilisateur, Double> prixachatColumn;
    @FXML private TableColumn<Utilisateur, Double> prixventeColumn;

    @FXML private TextField idField;
    @FXML private TextField medicamentSearchField;
    @FXML private TextField PUachatField;
    @FXML private TextField PUventeField;

    @FXML
    public void initialize() throws SQLException {
        if (listPrixTable != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("idMedicament"));
            medicamentColumn.setCellValueFactory(new PropertyValueFactory<>("dci"));
            prixachatColumn.setCellValueFactory(new PropertyValueFactory<>("puAchat"));
            prixventeColumn.setCellValueFactory(new PropertyValueFactory<>("puVente"));

            listPrixTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    idField.setText(String.valueOf(newSelection.getIdMedicament()));
                    medicamentSearchField.setText(newSelection.getDci());
                    PUachatField.setText(String.valueOf(newSelection.getPuAchat()));
                    PUventeField.setText(String.valueOf(newSelection.getPuVente()));
                }
            });

            loadMedicamentData();
        }
    }

    private ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();

    private void loadMedicamentData() throws SQLException {
        MedicamentDAO medicamentDAO = new MedicamentDAO();
        medicamentList = medicamentDAO.getPrixMedicament();
        listPrixTable.setItems(medicamentList);
    }

    @FXML private TextField searchField;

    public void searchButtonOnAction(ActionEvent e) throws SQLException {
        ObservableList<Medicament> filteredList = FXCollections.observableArrayList();
        MedicamentDAO medicamentDAO = new MedicamentDAO();
        medicamentList = medicamentDAO.getAllMedicament();

        String search = searchField.getText().trim();
        String lowerCaseFilter = search.toLowerCase();

        for (Medicament medicament : medicamentList) {
            if(medicament.getDci().toLowerCase().contains(lowerCaseFilter)){
                filteredList.add(medicament);
            }
            listPrixTable.setItems(filteredList);
        }
    }

    @FXML private Label medicamentError;
    @FXML private Label prixachatError;
    @FXML private Label prixventeError;

    public void MAJprixButtonOnAction(ActionEvent e) throws SQLException {
        MedicamentDAO medicamentDAO = new MedicamentDAO();
        boolean isInvalid = false;

        if(medicamentSearchField.getText().isEmpty()){
            medicamentError.setText("Chosissez un médicament");
            isInvalid = true;
        }else{
            medicamentError.setText("");
        }

        if (ValidationUtils.validatePrice(PUachatField, prixachatError)) isInvalid = true;
        if (ValidationUtils.validatePrice(PUventeField, prixventeError)) isInvalid = true;

        if(!isInvalid){
            int idDci = Integer.parseInt(idField.getText().trim());
            String dci = medicamentSearchField.getText().trim();
            double prixachat = Double.parseDouble(PUachatField.getText().trim());
            double prixvente = Double.parseDouble(PUventeField.getText().trim());

            Medicament medicament = new Medicament(dci, prixachat, prixvente);
            if(medicamentDAO.MAJprix(idDci, medicament)){
                clearMAJ();
                loadMedicamentData();
            }
        }
    }

    public void annulerButtonOnAction(ActionEvent actionEvent) throws SQLException {
        loadMedicamentData();
        clearMAJ();
    }

    private void clearMAJ(){
        medicamentSearchField.clear();
        PUachatField.clear();
        PUventeField.clear();
    }
}

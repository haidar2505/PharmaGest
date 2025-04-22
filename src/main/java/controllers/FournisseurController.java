package controllers;

import DAO.UtilisateurDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import models.Utilisateur;
import utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import models.Fournisseur;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.Utils;
import DAO.FournisseurDAO;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.ValidationUtils;

public class FournisseurController {
    Utils sceneLoader = new Utils();

    @FXML public Button maintenanceButton;
    public void maintenanceButtonOnAction(ActionEvent e) throws IOException {
        sceneLoader.loadScene("Maintenance.fxml", "Maintenance", maintenanceButton);
    }

    public void fournisseurAnnulerButtonOnAction(ActionEvent e) throws IOException {
        clearForm();
    }

    @FXML public TableView<Fournisseur> fournisseurTable;
    @FXML private TableColumn<Fournisseur, Integer> nomColumn;
    @FXML private TableColumn<Fournisseur, String> emailColumn;
    @FXML private TableColumn<Fournisseur, String> adresseColumn;
    @FXML private TableColumn<Fournisseur, String> paysColumn;

    @FXML private TextField nomField;
    @FXML private TextField emailField;
    @FXML private TextField adresseField;
    @FXML private TextField paysField;

    @FXML private Label nomFieldError;
    @FXML private Label emailFieldError;
    @FXML private Label adresseFieldError;
    @FXML private Label paysFieldError;

    @FXML private ComboBox<String> searchComboBox;

    private final ObservableList<String> columns = FXCollections.observableArrayList("Nom", "Email", "Adresse", "Pays");

    // Declaration of ObservableList for Fournisseurs
    private final ObservableList<Fournisseur> fournisseurObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {
        if (fournisseurTable != null && searchComboBox != null) {

            //Set up ComboBox
            searchComboBox.setItems(columns);

            //Table columns
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            paysColumn.setCellValueFactory(new PropertyValueFactory<>("pays"));

            fournisseurTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {

                    nomField.setText(newSelection.getNom());
                    emailField.setText(newSelection.getEmail());
                    adresseField.setText(newSelection.getAdresse());
                    paysField.setText(newSelection.getPays());
                }
            });
            loadFournisseurData();
        }
    }

    public boolean isRowSelected() {
        return fournisseurTable.getSelectionModel().getSelectedItem() != null;
    }

    private ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();

    //Load the table
    private void loadFournisseurData() throws SQLException {
        FournisseurDAO fournisseurDAO = new FournisseurDAO();
        fournisseurList = fournisseurDAO.getAllFounisseur();
        fournisseurTable.setItems(fournisseurList);
    }

    @FXML private TextField searchField;
    @FXML private Label searchError;

    public void searchButtonOnAction(ActionEvent e) throws SQLException {

        ObservableList<Fournisseur> filteredList = FXCollections.observableArrayList();
        FournisseurDAO fournisseurDAO = new FournisseurDAO();
        fournisseurList = fournisseurDAO.getAllFounisseur();

        String search = searchField.getText().trim();
        String lowerCaseFilter = search.toLowerCase();

        if(search.isEmpty() || searchComboBox.getValue() == null){
            loadFournisseurData();
        } else {
            String selectedComboBox = searchComboBox.getValue().trim();
            if(selectedComboBox.isEmpty()){
                searchError.setText("Choisir une colonne");
            }else{
                for (Fournisseur fournisseur : fournisseurList) {
                    boolean matches = false;

                    switch (selectedComboBox) {
                        case "Nom":
                            matches = fournisseur.getNom().toLowerCase().contains(lowerCaseFilter);
                            break;
                        case "Email":
                            matches = fournisseur.getEmail().toLowerCase().contains(lowerCaseFilter);
                            break;
                        case "Adresse":
                            matches = fournisseur.getAdresse().toLowerCase().contains(lowerCaseFilter);
                            break;
                        case "Pays":
                            matches = fournisseur.getPays().toLowerCase().contains(lowerCaseFilter);
                            break;
                    }
                    if (matches) {
                        filteredList.add(fournisseur);
                    }
                }
            }
            fournisseurTable.setItems(filteredList);
        }
    }

    public void searchButtonAnnulerOnAction(ActionEvent e) throws SQLException{
        searchComboBox.getSelectionModel().clearSelection();
        searchField.clear();
        loadFournisseurData();
    }

    public void fournisseurAddButtonOnAction(ActionEvent e) throws SQLException {
        FournisseurDAO fournisseurDAO = new FournisseurDAO();
        boolean isInvalid = false;

        if(fournisseurDAO.verifyFournisseur(nomField.getText().trim().toLowerCase())){
            nomFieldError.setText("Fournisseur existant");
            isInvalid = true;
        }else if(ValidationUtils.validateName(nomField, nomFieldError)){
            isInvalid = true;
        }
        if (ValidationUtils.validateEmail(emailField, emailFieldError)) isInvalid = true;
        if (ValidationUtils.validateText(paysField, paysFieldError)) isInvalid = true;

        if (adresseField.getText().isEmpty()) {
            adresseFieldError.setText("Entrez l'adresse");
            isInvalid = true;
        }else{
            adresseFieldError.setText("");
        }

        if(!isInvalid){
            String nom = nomField.getText().trim();
            String email = emailField.getText().trim();
            String adresse = adresseField.getText().trim();
            String pays = paysField.getText().trim();

            Fournisseur ajouterFournisseur = new Fournisseur(nom, email, adresse, pays);
            if (fournisseurDAO.addFournisseur(ajouterFournisseur)) {
                loadFournisseurData();
                clearForm();
            }
        }
    }

    public void fournisseurModifierButtonOnAction(ActionEvent e) throws SQLException {
        if(isRowSelected()) {
            FournisseurDAO fournisseurDAO = new FournisseurDAO();
            boolean isInvalid = false;

            if(fournisseurDAO.verifyFournisseur(nomField.getText().trim().toLowerCase())){
                nomFieldError.setText("Fournisseur existant");
                isInvalid = true;
            }else if(ValidationUtils.validateName(nomField, nomFieldError)){
                isInvalid = true;
            }
            if (ValidationUtils.validateEmail(emailField, emailFieldError)) isInvalid = true;
            if (ValidationUtils.validateText(paysField, paysFieldError)) isInvalid = true;

            if (adresseField.getText().isEmpty()) {
                adresseFieldError.setText("Entrez l'adresse");
                isInvalid = true;
            } else {
                adresseFieldError.setText("");
            }

            if (!isInvalid) {
                String nom = nomField.getText().trim();
                String email = emailField.getText().trim();
                String adresse = adresseField.getText().trim();
                String pays = paysField.getText().trim();

                Fournisseur modifierFournisseur = new Fournisseur(nom, email, adresse, pays);
                int id = fournisseurDAO.getFournisseurId(nom);
                if (id > 0) {
                    if (fournisseurDAO.modifierForunisseur(id, modifierFournisseur)) {
                        loadFournisseurData();
                        clearForm();
                    }
                }
            }
        }else{
            nomFieldError.setText("Choisir un fournisseur");
        }
    }

    //Delete fournisseur
    public void supprimerFournisseurButtonOnAction(ActionEvent e) throws SQLException{
        if(isRowSelected()) {
            String nom = nomField.getText().trim();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supprimer fournisseur");
            alert.setHeaderText("Vous voulez supprimer le fournisseur : " + nom);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                FournisseurDAO fournisseurDAO = new FournisseurDAO();
                int id = fournisseurDAO.getFournisseurId(nom);
                if (id > 0) {
                    fournisseurDAO.deleteFournisseur(id);
                    loadFournisseurData();
                    clearForm();
                }
            }else{
                loadFournisseurData();
                clearForm();
            }
        }else{
            searchError.setText("Choisir un fournisseur");
        }
    }

    private void clearForm(){
        fournisseurTable.getSelectionModel().clearSelection();

        // Clear text fields
        nomField.clear();
        emailField.clear();
        adresseField.clear();
        paysField.clear();

        // Clear error labels
        nomFieldError.setText("");
        emailFieldError.setText("");
        adresseFieldError.setText("");
        paysFieldError.setText("");
    }
}

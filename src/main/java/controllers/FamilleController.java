package controllers;

import DAO.FamilleDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Famille;
import utils.Utils;
import utils.ValidationUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class FamilleController {
    Utils sceneLoader = new Utils();

    @FXML
    public Button maintenanceButton;

    public void maintenanceButtonOnAction(ActionEvent e) throws IOException, SQLException {
        sceneLoader.loadScene("Maintenance.fxml", "Maintenance", maintenanceButton);
    }

    @FXML private TableView<Famille> familleTable;
    @FXML private TableColumn<Famille, String> idColumn;
    @FXML private TableColumn<Famille, String> familleColumn;

    @FXML private TextField idField;
    @FXML private TextField familleSearchField;
    @FXML private Label familleError;


    public void searchButtonAnnulerOnAction(ActionEvent e) throws SQLException {
        familleSearchField.clear();
    }

    public void initialize() throws SQLException{
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idFamille"));
        familleColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
//        familleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomFamille()));

        familleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(String.valueOf(newSelection.getIdFamille()));
                familleSearchField.setText(newSelection.getNomFamille());
            }
        });

        loadFamilleData();
    }

    private ObservableList<Famille> familleList = FXCollections.observableArrayList();

    private void loadFamilleData() throws SQLException{
        FamilleDAO familleDAO = new FamilleDAO();
        familleList = familleDAO.getAllFamille();
        familleTable.setItems(familleList);
    }

    public void searchButtonOnAction(ActionEvent e) throws SQLException {
        ObservableList<Famille> filteredList = FXCollections.observableArrayList();
        FamilleDAO familleDAO = new FamilleDAO();
        familleList = familleDAO.getAllFamille();

        String search = familleSearchField.getText().trim();
        String lowerCaseFilter = search.toLowerCase();

        for (Famille famille : familleList) {
            if(famille.getNomFamille().toLowerCase().contains(lowerCaseFilter)){
                filteredList.add(famille);
            }
            familleTable.setItems(filteredList);
        }
    }

    public void addFamilleButtonOnAction(ActionEvent e) throws SQLException {
        FamilleDAO familleDAO = new FamilleDAO();
        String search = familleSearchField.getText().trim();

        if(familleDAO.verifyFamille(search)){
            familleError.setText("Famille existant");
        }else{
            boolean isInvalid = false;

            if (ValidationUtils.validateFamille(familleSearchField, familleError)) isInvalid = true;

            if(!isInvalid){
                String famille = familleSearchField.getText();

                Famille newfamille = new Famille(famille);
                if(familleDAO.addFamille(newfamille)){
                    loadFamilleData();
                    clearFamille();
                }
            }
        }
    }

    public void supprimerFamilleButtonOnAction(ActionEvent e) throws SQLException {
        int id = Integer.parseInt(idField.getText());
        String famille = familleSearchField.getText();
        if(famille.isEmpty()){
            familleError.setText("Choisir une famille");
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supprimer forme");
            alert.setHeaderText("Vous voulez supprimer la forme : " + famille);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                FamilleDAO familleDAO = new FamilleDAO();
                if(familleDAO.verifyMedicamentFamille(id)){
                    familleError.setText("Suppression invalide !");
                }else {
                    familleDAO.deleteFamille(famille);
                    loadFamilleData();
                    clearFamille();
                }
            }
        }
    }

    private void clearFamille(){
        familleSearchField.clear();

        familleError.setText("");
    }
}


package controllers;

import DAO.FormeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Forme;
import utils.Utils;
import utils.ValidationUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class FormeController {
    Utils sceneLoader = new Utils();

    @FXML public Button maintenanceButton;

    public void maintenanceButtonOnAction(ActionEvent e) throws IOException, SQLException {
        sceneLoader.loadScene("Maintenance.fxml", "Maintenance", maintenanceButton);
    }

    public void searchButtonAnnulerOnAction(ActionEvent e) throws SQLException {
        loadFormeData();
        clearSearch();
    }

    @FXML private TableView<Forme> formeTable;
    @FXML private TableColumn<Forme, String> idColumn;
    @FXML private TableColumn<Forme, String> formeColumn;

    @FXML private TextField idField;
    @FXML private TextField formeSearchField;

    @FXML private Label formeError;


    public void initialize() throws SQLException{
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idForme"));
//        formeColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        formeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomForme()));

        formeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(String.valueOf(newSelection.getIdForme()));
                formeSearchField.setText(newSelection.getNomForme());
            }
        });
        loadFormeData();
    }

    public boolean isRowSelected() {
        return formeTable.getSelectionModel().getSelectedItem() != null;
    }

    private ObservableList<Forme> formeList = FXCollections.observableArrayList();

    private void loadFormeData() throws SQLException{
        FormeDAO formeDAO = new FormeDAO();
        formeList = formeDAO.getAllForme();
        formeTable.setItems(formeList);
    }

    public void searchButtonOnAction(ActionEvent e) throws SQLException {
        ObservableList<Forme> filteredList = FXCollections.observableArrayList();
        FormeDAO formeDAO = new FormeDAO();
        formeList = formeDAO.getAllForme();

        String search = formeSearchField.getText().trim();
        String lowerCaseFilter = search.toLowerCase();

        for (Forme forme : formeList) {
            if(forme.getNomForme().toLowerCase().contains(lowerCaseFilter)){
                filteredList.add(forme);
            }
            formeTable.setItems(filteredList);
        }
    }

    public void addFormeButtonOnAction(ActionEvent e) throws SQLException {
        FormeDAO formeDAO = new FormeDAO();
        String search = formeSearchField.getText().trim();

        if(formeDAO.verifyForme(search)){
            formeError.setText("Forme existant");
        }else{
            boolean isInvalid = false;

            if (ValidationUtils.validateForme(formeSearchField, formeError)) isInvalid = true;

            if(!isInvalid){
                String forme = formeSearchField.getText();

                Forme newforme = new Forme(forme);
                if(formeDAO.addForme(newforme)){
                    loadFormeData();
                    clearSearch();
                }
            }
        }
    }

    public void supprimerFormeButtonOnAction(ActionEvent e) throws SQLException {
        if(isRowSelected()) {
            int id = Integer.parseInt(idField.getText());
            String forme = formeSearchField.getText();
            if (forme.isEmpty()) {
                formeError.setText("Choisir une forme");
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Supprimer forme");
                alert.setHeaderText("Vous voulez supprimer la forme : " + forme);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    FormeDAO formeDAO = new FormeDAO();
                    if (formeDAO.verifyMedicamentForme(id)) {
                        formeError.setText("Suppression invalide !");
                    } else {
                        formeDAO.deleteForme(id);
                        loadFormeData();
                        clearSearch();
                    }
                }
            }
        }else{
            formeError.setText("Choisir une forme");
        }
    }

    private void clearSearch(){
        formeSearchField.clear();

        formeError.setText("");
    }
}


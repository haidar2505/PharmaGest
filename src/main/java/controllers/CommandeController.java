package controllers;

import DAO.CommandeDAO;
import DAO.VenteDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Commande;
import models.Medicament;
import models.MedicamentCommande;
import utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class CommandeController {
    Utils sceneLoader = new Utils();

    @FXML public Button dashboardButton;
    public void dashboardButtonOnAction(ActionEvent e) throws IOException {
        sceneLoader.loadScene("Dashboard.fxml", "Dashboard", dashboardButton);
    }

    @FXML private TextField totalField;
    @FXML private TextField numCommandeField;

    @FXML private Label commandeError;

    @FXML private TableView<Commande> commandeTable;
    @FXML private TableColumn<Commande, String> numCommandeColumn;
    @FXML private TableColumn<Commande, String> medicamentColumn;
    @FXML private TableColumn<Commande, String> stockColumn;
    @FXML private TableColumn<Commande, String> stockMinColumn;
    @FXML private TableColumn<Commande, String> stockMaxColumn;
    @FXML private TableColumn<Commande, String> stockCommandeColumn;
    @FXML private TableColumn<Commande, String> puVenteColumn;
    @FXML private TableColumn<Commande, String> totalColumn;
    @FXML private TableColumn<Commande, String> fournisseurColumn;

    public void initialize() throws SQLException{
        if(commandeTable != null){
            numCommandeColumn.setCellValueFactory(new PropertyValueFactory<>("numCommande"));
            medicamentColumn.setCellValueFactory(new PropertyValueFactory<>("dci"));
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            stockMinColumn.setCellValueFactory(new PropertyValueFactory<>("stockMin"));
            stockMaxColumn.setCellValueFactory(new PropertyValueFactory<>("stockMax"));
            stockCommandeColumn.setCellValueFactory(new PropertyValueFactory<>("qteCommande"));
            puVenteColumn.setCellValueFactory(new PropertyValueFactory<>("puVente"));
            totalColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            fournisseurColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));

            commandeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    totalField.setText(String.valueOf(newSelection.getMontant()));
                    numCommandeField.setText(String.valueOf(newSelection.getNumCommande()));
                }
            });

            loadCommandeData();
        }
    }

    public boolean isRowSelectedPanier() {
        return commandeTable.getSelectionModel().getSelectedItem() != null;
    }

    private ObservableList<Commande> commandeList = FXCollections.observableArrayList();

    private void loadCommandeData() throws SQLException{
        CommandeDAO commandeDAO = new CommandeDAO();
        commandeList = commandeDAO.getAllCommande();
        commandeTable.setItems(commandeList);
    }


    public void validerButtonOnAction(ActionEvent e) throws SQLException{
        if(isRowSelectedPanier()) {
            String numCommande = numCommandeField.getText();

            CommandeDAO commandeDAO = new CommandeDAO();

            if (commandeDAO.validerCommandePanier(numCommande)) {
                loadCommandeData();
                clearForm();
            } else {
                commandeError.setText("Failed");
            }
        }else{
            commandeError.setText("Choisir un médicament");
        }
    }

    private void clearForm(){
        totalField.clear();
        numCommandeField.clear();

        commandeError.setText("");
    }
}

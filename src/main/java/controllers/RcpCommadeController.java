package controllers;

import DAO.CommandeDAO;
import DAO.MedicamentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Commande;
import models.Medicament;
import utils.Utils;
import utils.ValidationUtils;

import java.io.IOException;
import java.sql.SQLException;

public class RcpCommadeController {
    Utils sceneLoader = new Utils();

    @FXML public Button dashboardButton;
    public void dashboardButtonOnAction(ActionEvent e) throws IOException {
        sceneLoader.loadScene("Dashboard.fxml", "Dashboard", dashboardButton);
    }

    @FXML private TextField numCommandeField;
    @FXML private TextField qteLivreeField;
    @FXML private TextField idMedicamentField;
    @FXML private TextField stockActuelField;
    @FXML private TextField stockCommanderField;

    @FXML private Label qteLivreeError;

    @FXML private TableView<Commande> rcpCommandeTable;
    @FXML private TableColumn<Commande, String> numCommandeColumn;
    @FXML private TableColumn<Commande, String> idMedicamentColumn;
    @FXML private TableColumn<Commande, String> medicamentColumn;
    @FXML private TableColumn<Commande, String> stockColumn;
    @FXML private TableColumn<Commande, String> stockMinColumn;
    @FXML private TableColumn<Commande, String> stockMaxColumn;
    @FXML private TableColumn<Commande, String> stockCommandeColumn;
    @FXML private TableColumn<Commande, String> puVenteColumn;
    @FXML private TableColumn<Commande, String> totalColumn;
    @FXML private TableColumn<Commande, String> fournisseurColumn;

    public void initialize() throws SQLException {
        if(rcpCommandeTable != null){
            numCommandeColumn.setCellValueFactory(new PropertyValueFactory<>("numCommande"));
            idMedicamentColumn.setCellValueFactory(new PropertyValueFactory<>("idMedicament"));
            medicamentColumn.setCellValueFactory(new PropertyValueFactory<>("dci"));
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
            stockMinColumn.setCellValueFactory(new PropertyValueFactory<>("stockMin"));
            stockMaxColumn.setCellValueFactory(new PropertyValueFactory<>("stockMax"));
            stockCommandeColumn.setCellValueFactory(new PropertyValueFactory<>("qteCommande"));
            puVenteColumn.setCellValueFactory(new PropertyValueFactory<>("puVente"));
            totalColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            fournisseurColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));

            rcpCommandeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    numCommandeField.setText(String.valueOf(newSelection.getNumCommande()));
                    idMedicamentField.setText(String.valueOf(newSelection.getIdMedicament()));
                    stockActuelField.setText(String.valueOf(newSelection.getStock()));
                    stockCommanderField.setText(String.valueOf(newSelection.getQteCommande()));
                }
            });

            loadRcpCommandeData();
        }
    }

    public boolean isRowSelected() {
        return rcpCommandeTable.getSelectionModel().getSelectedItem() != null;
    }

    private ObservableList<Commande> rcpCommandeList = FXCollections.observableArrayList();

    private void loadRcpCommandeData() throws SQLException {
        CommandeDAO commandeDAO = new CommandeDAO();
        rcpCommandeList = commandeDAO.getAllRcpCommande();
        rcpCommandeTable.setItems(rcpCommandeList);
    }


    public void validerButtonOnAction(ActionEvent e) throws SQLException{
        if(isRowSelected()) {
            MedicamentDAO medicamentDAO = new MedicamentDAO();

            boolean isInvalid = false;

            String numCommande = numCommandeField.getText();

            if (qteLivreeField.getText().isEmpty()) {
                qteLivreeError.setText("Donner une quantité");
                isInvalid = true;
            } else if (ValidationUtils.validateQteStock(qteLivreeField, qteLivreeError)) {
                isInvalid = true;
            } else {
                if (numCommandeField.getText().isEmpty()) {
                    qteLivreeError.setText("Error");
                    isInvalid = true;
                } else {
                    int qteStock = Integer.parseInt(qteLivreeField.getText());
                    int stockCommander = Integer.parseInt(stockCommanderField.getText());

                    if (qteStock > stockCommander) {
                        qteLivreeError.setText("Stock invalide");
                        isInvalid = true;
                    }
                }
            }

            if(!isInvalid){
                CommandeDAO commandeDAO = new CommandeDAO();

                String pdfFile = "rcpCommande_pdf";
                int idMedicament = Integer.parseInt(idMedicamentField.getText());
                int stockActuel = Integer.parseInt(stockActuelField.getText());
                int stockLivree = Integer.parseInt(qteLivreeField.getText());

                int newStock = stockActuel + stockLivree;

                if (commandeDAO.validerCommande(numCommande) && commandeDAO.updateStockMedicament(idMedicament, newStock) && commandeDAO.addRecipient(pdfFile, numCommande)) {
                    loadRcpCommandeData();
                    clearForm();
                } else {
                    qteLivreeError.setText("Failed");
                }
            }
        }
    }

    private void clearForm() {
        qteLivreeField.clear();
        numCommandeField.clear();
        idMedicamentField.clear();

        qteLivreeError.setText("");
    }
}

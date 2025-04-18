package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Medicament;
import models.Vente;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class VenteController {
    Utils sceneLoader = new Utils();

    @FXML private Button dashboardButton;

    public void dashboardButtonOnAction(ActionEvent e) throws IOException, SQLException {
        sceneLoader.loadScene("Dashboard.fxml", "Dashboard", dashboardButton);
    }

    @FXML private TableView<Vente> selectionTable;
    @FXML private TableColumn<Vente, String> medicamentColumn;
    @FXML private TableColumn<Vente, Integer> quantiteStockColumn;
    @FXML private TableColumn<Vente, Double> prixVenteColumn;

    @FXML private TableColumn<Vente, String> medicamentPanierColumn;
    @FXML private TableColumn<Vente, Double> quantitePanierColumn;
    @FXML private TableColumn<Vente, Double> prixPanierColumn;
    @FXML private TableColumn<Vente, String> totalPanierColumn;

    //ComboBox
    @FXML private ComboBox<String> searchComboBox;
    @FXML private ComboBox<String> formeComboBox;
    @FXML private ComboBox<String> familleComboBox;
}


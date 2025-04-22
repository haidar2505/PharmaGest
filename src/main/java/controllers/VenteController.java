package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Medicament;
import models.ReceptionCmd;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class VenteController {
    Utils sceneLoader = new Utils();

    @FXML private Button dashboardButton;
    public void dashboardButtonOnAction(ActionEvent e) throws IOException {
        sceneLoader.loadScene("Dashboard.fxml", "Dashboard", dashboardButton);
    }


}


package controllers;
import DAO.LoginDAO;
import dataBase.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.*;

import javafx.scene.Parent;
import javafx.stage.StageStyle;
import utils.SessionManager;
import utils.Utils;

public class LoginController {

    @FXML public PasswordField passwordField;
    @FXML private TextField usernameTextField;

    @FXML private Label loginError;

    @FXML private Button loginButton;

    public void loginButtonOnAction(ActionEvent e) throws IOException, SQLException {
        LoginDAO logindao = new LoginDAO();

        String username = usernameTextField.getText().trim();
        String password = passwordField.getText().trim();
        try {
            if (username.isEmpty() || password.isEmpty() ) {
                loginError.setText("Identifiant ou mot de passe vide !");
                loginError.setVisible(true);
                loginError.setManaged(true);
                return;
            } else {
                Utils hasher = new Utils();
                String hashedPassword = Utils.hashWithSHA256(password);
                if (logindao.verifyUserLogin(username, hashedPassword) && logindao.updateIdentifiant(username)) {
                    Utils sceneLoader = new Utils();
                    sceneLoader.loadScene("Dashboard.fxml", username, loginButton);
                }else{
                    loginError.setText("Identifiant ou mot de passe incorrect !");
                    loginError.setVisible(true);
                    loginError.setManaged(true);
                    return;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void cancelButtonOnAction(){ System.exit(0); }
}

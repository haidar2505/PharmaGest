package DAO;

import dataBase.DatabaseConnection;
import utils.SessionManager;

import java.sql.*;

public class LoginDAO {

    private final Connection conn;
    public LoginDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    //Verify identifiant
    public boolean verifyUserLogin(String username, String password) throws SQLException {
        String verifyUser = "SELECT * FROM utilisateur WHERE TRIM(LOWER(identifiant)) = TRIM(LOWER(?)) AND mot_de_passe = ?";
        try (PreparedStatement stmt = conn.prepareStatement(verifyUser)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String prenom = rs.getString("prenom");
                    String nom = rs.getString("nom");
                    String email = rs.getString("email");
                    String identifiant = rs.getString("identifiant");
                    String role = rs.getString("role");
                    boolean isAdmin = rs.getBoolean("is_admin");

                    SessionManager.setUserInfo(prenom, nom, email, identifiant, role, isAdmin);

                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public boolean updateIdentifiant(String username) throws SQLException {
        if(username.toLowerCase().trim().equals(SessionManager.getIdentifiant().toLowerCase().trim())) {
            String updateUser = "UPDATE utilisateur SET identifiant = ? WHERE identifiant = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateUser)) {
                stmt.setString(1, username);
                stmt.setString(2, SessionManager.getIdentifiant());
                int rowAffected = stmt.executeUpdate();

                return rowAffected > 0;
            }
        }else{
            return false;
        }
    }
}

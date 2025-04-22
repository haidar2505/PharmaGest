package DAO;

import dataBase.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Forme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FormeDAO {

    private final Connection conn;
    public FormeDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public ObservableList<Forme> getAllForme() throws SQLException{
        ObservableList<Forme> formList = FXCollections.observableArrayList();
        String allForme = "SELECT * FROM forme ORDER BY nom_forme";
        try (PreparedStatement stmt = conn.prepareStatement(allForme);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Forme forme = new Forme(
                        rs.getInt("id_forme"),
                        rs.getString("nom_forme")
                );
                formList.add(forme);
            }
        }
        return formList;
    }

    public int getFormeId(String nomForme) throws SQLException{
        String getId = "SELECT id_forme FROM forme WHERE nom_forme = ?";
        try(PreparedStatement stmt = conn.prepareStatement(getId)){
            stmt.setString(1, nomForme);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("id_forme");
                }
            }
        }
        return -1;
    }

    //Verify existing forme
    public boolean verifyForme(String nomForme) {
        String verifyIdentifiant = "SELECT COUNT(*) FROM forme WHERE TRIM(LOWER(nom_forme)) = TRIM(LOWER(?))";
        try (PreparedStatement stmt = conn.prepareStatement(verifyIdentifiant)) {
            stmt.setString(1, nomForme);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking forme: " + e.getMessage());
        }
        return false;
    }

    public boolean verifyMedicamentForme(int idForme) {
        String verifyForme = "SELECT COUNT(*) FROM medicament WHERE id_forme = ?";
        try (PreparedStatement stmt = conn.prepareStatement(verifyForme)) {
            stmt.setInt(1, idForme);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking forme: " + e.getMessage());
        }
        return false;
    }

    public boolean addForme (Forme forme) throws SQLException{
        String ajouterForme = "INSERT INTO forme (nom_forme) VALUES (?)";
        try(PreparedStatement stmt = conn.prepareStatement(ajouterForme)) {
            stmt.setString(1, forme.getNomForme());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteForme(int idForme) throws SQLException{
        String supprimerForme = "DELETE FROM forme WHERE id_forme = ?";
        try(PreparedStatement stmt = conn.prepareStatement(supprimerForme)) {
            stmt.setInt(1, idForme);
            return stmt.executeUpdate() > 0;
        }
    }
}

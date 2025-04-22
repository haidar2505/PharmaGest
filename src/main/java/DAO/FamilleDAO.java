package DAO;

import dataBase.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Famille;
import models.Forme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FamilleDAO {
    private final Connection conn;
    public FamilleDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public ObservableList<Famille> getAllFamille() throws SQLException{
        ObservableList<Famille> familleList = FXCollections.observableArrayList();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM famille ORDER BY nom_famille");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Famille famille = new Famille(
                        rs.getInt("id_famille"),
                        rs.getString("nom_famille")
                );
                familleList.add(famille);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching famille: " + e.getMessage());
        }
        return familleList;
    }

    //Get utilisateur id
    public int getFamilleId(String nomFamille) throws SQLException{
        String getId = "SELECT id_famille FROM famille WHERE nom_famille = ?";
        try(PreparedStatement stmt = conn.prepareStatement(getId)){
            stmt.setString(1, nomFamille);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("id_famille");
                }
            }
        }
        return -1;
    }

    //Verify existing famille
    public boolean verifyFamille(String nomFamille) {
        String verifyIdentifiant = "SELECT COUNT(*) FROM famille WHERE TRIM(LOWER(nom_famille)) = TRIM(LOWER(?))";
        try (PreparedStatement stmt = conn.prepareStatement(verifyIdentifiant)) {
            stmt.setString(1, nomFamille);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking famille: " + e.getMessage());
        }
        return false;
    }

    public boolean verifyMedicamentFamille(int idFamille) {
        String verifyFamille = "SELECT COUNT(*) FROM medicament WHERE id_famille = ?";
        try (PreparedStatement stmt = conn.prepareStatement(verifyFamille)) {
            stmt.setInt(1, idFamille);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking famille: " + e.getMessage());
        }
        return false;
    }

    public boolean addFamille (Famille famille) throws SQLException{
        String query = "INSERT INTO famille (nom_famille) VALUES (?)";
        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, famille.getNomFamille());
            return stmt.executeUpdate() > 0;
        }
    }

    public void deleteFamille(String famille) throws SQLException{
        String query = "DELETE FROM famille WHERE nom_famille = ?";
        try(PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, famille);
            stmt.executeUpdate();
        }
    }
}

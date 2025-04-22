package DAO;

import dataBase.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Fournisseur;
import models.Utilisateur;

import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurDAO {

    private final Connection conn;
    public FournisseurDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public ObservableList<Fournisseur> getAllFounisseur() throws SQLException{
        ObservableList<Fournisseur> fournisseurList = FXCollections.observableArrayList();
        String allFournisseur = "SELECT * FROM fournisseur";
        try(PreparedStatement stmt = conn.prepareStatement(allFournisseur);
            ResultSet rs = stmt.executeQuery()) {
            while(rs.next()) {
                Fournisseur fournisseur = new Fournisseur(
                    rs.getString("nom"),
                    rs.getString("email"),
                    rs.getString("adresse"),
                    rs.getString("pays")
                );
                fournisseurList.add(fournisseur);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return fournisseurList;
    }

    //Verify existing fournisseur
    public boolean verifyFournisseur(String nom) {
        String existFournisseur = "SELECT COUNT(*) FROM fournisseur WHERE TRIM(LOWER(nom)) = TRIM(LOWER(?))";
        try (PreparedStatement stmt = conn.prepareStatement(existFournisseur)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking nom: " + e.getMessage());
        }
        return false;
    }

    //Get fournisseur id
    public int getFournisseurId(String nom) throws SQLException{
        String getId = "SELECT id_fournisseur FROM fournisseur WHERE nom = ?";
        try(PreparedStatement stmt = conn.prepareStatement(getId)){
            stmt.setString(1, nom);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("id_fournisseur");
                }
            }
        }
        return -1;
    }

    //Ajouter forunisseur
    public boolean addFournisseur (Fournisseur fournisseur) throws SQLException{
        String ajouterUtilisateur = "INSERT INTO fournisseur (nom, email, adresse, pays) VALUES (?, ?, ?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(ajouterUtilisateur)) {
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getEmail());
            stmt.setString(3, fournisseur.getAdresse());
            stmt.setString(4, fournisseur.getPays());
            return stmt.executeUpdate() > 0;
        }
    }

    //Modify forunisseur
    public boolean modifierForunisseur(int fourID, Fournisseur forunisseur) throws SQLException{
        String modifyUtilisateurPassword = "UPDATE fournisseur SET nom = ?, email = ?, adresse = ?, pays = ? WHERE id_fournisseur = ?";
        try(PreparedStatement stmt = conn.prepareStatement(modifyUtilisateurPassword)) {
            stmt.setString(1, forunisseur.getNom());
            stmt.setString(2, forunisseur.getEmail());
            stmt.setString(3, forunisseur.getAdresse());
            stmt.setString(4, forunisseur.getPays());
            stmt.setInt(8, fourID);
            return stmt.executeUpdate() > 0;
        }
    }

    //Supprimer forunisseur
    public boolean deleteFournisseur(int fourID) throws SQLException{
        String supprimerUtilisateur = "DELETE FROM fournisseur WHERE id_fournisseur = ?";
        try(PreparedStatement stmt = conn.prepareStatement(supprimerUtilisateur)) {
            stmt.setInt(1, fourID);
            return stmt.executeUpdate() > 0;
        }
    }
}

package DAO;

import dataBase.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Famille;
import models.Forme;
import models.Fournisseur;
import models.Medicament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicamentDAO {

    //Connection to database
    private final Connection conn;
    public MedicamentDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public ObservableList<Medicament> getAllMedicament() throws SQLException {
        ObservableList<Medicament> allMedicamentList = FXCollections.observableArrayList();
        String allMedicament = "SELECT m.*, f.nom_forme, fam.nom_famille, four.nom FROM medicament m JOIN forme f ON m.id_forme = f.id_forme JOIN famille fam ON m.id_famille = fam.id_famille JOIN fournisseur four ON m.id_fournisseur = four.id_fournisseur ORDER BY m.dci";
        try (PreparedStatement stmt = conn.prepareStatement(allMedicament);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Forme forme = new Forme(rs.getString("nom_forme"));
                Famille famille = new Famille(rs.getString("nom_famille"));
                Fournisseur fournisseur = new Fournisseur(rs.getString("nom"));
                Medicament medicament = new Medicament(
                        rs.getInt("id_medicament"),
                        rs.getString("dci"),
                        rs.getString("dosage"),
                        rs.getDouble("pu_achat"),
                        rs.getDouble("pu_vente"),
                        rs.getInt("stock"),
                        rs.getInt("stock_min"),
                        rs.getInt("stock_max"),
                        rs.getBoolean("ordonnace"),
                        fournisseur,
                        famille,
                        forme
                );
                allMedicamentList.add(medicament);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching medicament: " + e.getMessage());
        }
        return allMedicamentList;
    }

    public boolean verifyExistingMedicament(String medicament, String dose, String unite) throws SQLException{
        String verifyMedicament = "SELECT COUNT(*) FROM medicament WHERE TRIM(LOWER(dci)) = TRIM(LOWER(?)) AND TRIM(LOWER(dosage)) = TRIM(LOWER(?)) AND TRIM(LOWER(id_forme)) = TRIM(LOWER(?))";
        try (PreparedStatement stmt = conn.prepareStatement(verifyMedicament)) {
            stmt.setString(1, medicament);
            stmt.setString(2, dose);
            stmt.setString(3, unite);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking medicament: " + e.getMessage());
        }
        return false;
    }

    public ObservableList<Medicament> getPrixMedicament() throws SQLException {
        ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();
        String prixMedicament = "SELECT id_medicament, dci, pu_achat, pu_vente FROM medicament ORDER BY dci";
        try (PreparedStatement stmt = conn.prepareStatement(prixMedicament);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Medicament medicament = new Medicament(
                        rs.getInt("id_medicament"),
                        rs.getString("dci"),
                        rs.getDouble("pu_achat"),
                        rs.getDouble("pu_vente")
                );
                medicamentList.add(medicament);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching medicament: " + e.getMessage());
        }
        return medicamentList;
    }

    public boolean MAJprix(int idDci, double prixVente, double prixAchat) throws SQLException{
        String majPrix = "UPDATE medicament SET pu_achat = ?, pu_vente = ? WHERE id_medicament = ?";
        try(PreparedStatement stmt = conn.prepareStatement(majPrix)) {
            stmt.setDouble(2, prixAchat);
            stmt.setDouble(1, prixVente);
            stmt.setInt(3, idDci);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean addMedicament(Medicament medicament) throws SQLException{
        String ajouterMedicament = "INSERT INTO medicament (dci, pu_achat, pu_vente, stock, stock_min, stock_max, ordonnance, id_fournisseur, id_famille, id_forme, dosage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(ajouterMedicament)) {
            stmt.setString(1, medicament.getDci());
            stmt.setDouble(2, medicament.getPuAchat());
            stmt.setDouble(3, medicament.getPuVente());
            stmt.setInt(4, medicament.getStock());
            stmt.setInt(5, medicament.getStockMin());
            stmt.setInt(6, medicament.getStockMax());
            stmt.setBoolean(7, medicament.isOrdonnance());
            stmt.setInt(8, medicament.getIdFournisseur());
            stmt.setInt(9, medicament.getIdFamille());
            stmt.setInt(10, medicament.getIdForme());
            stmt.setString(11, medicament.getDosage());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean modifierMedicament(int idDci, Medicament medicament) throws SQLException{
        String modifyMedicament = "UPDATE medicament SET dci = ?, pu_achat = ?, pu_vente = ?, stock = ?, stock_min = ?, stock_max = ?, ordonnance = ?, id_fournisseur = ?, id_famille = ?, id_forme = ?, dosage = ? WHERE id_medicament = ?";
        try(PreparedStatement stmt = conn.prepareStatement(modifyMedicament)) {
            stmt.setString(1, medicament.getDci());
            stmt.setDouble(2, medicament.getPuAchat());
            stmt.setDouble(3, medicament.getPuVente());
            stmt.setInt(4, medicament.getStock());
            stmt.setInt(5, medicament.getStockMin());
            stmt.setInt(6, medicament.getStockMax());
            stmt.setBoolean(7, medicament.isOrdonnance());
            stmt.setInt(8, medicament.getIdFournisseur());
            stmt.setInt(9, medicament.getIdFamille());
            stmt.setInt(10, medicament.getIdForme());
            stmt.setString(11, medicament.getDosage());
            stmt.setInt(12, medicament.getIdMedicament());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteMedicament(int idDci) throws SQLException{
        String supprimerMedicament = "DELETE FROM medicament WHERE id_medicament = ?";
        try(PreparedStatement stmt = conn.prepareStatement(supprimerMedicament)) {
            stmt.setInt(1, idDci);
            return stmt.executeUpdate() > 0;
        }
    }
}

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
                        rs.getInt("dosage"),
                        rs.getString("dosage_unite"),
                        rs.getDouble("pu_achat"),
                        rs.getDouble("pu_vente"),
                        rs.getInt("stock"),
                        rs.getInt("stock_min"),
                        rs.getInt("stock_max"),
                        rs.getBoolean("ordonnance"),
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

    public boolean verifyExistingMedicament(String medicament, int dose, int idUnite) throws SQLException{
        String verifyMedicament = "SELECT COUNT(*) FROM medicament WHERE TRIM(LOWER(dci)) = TRIM(LOWER(?)) AND dosage = ? AND id_forme = ?";
        try (PreparedStatement stmt = conn.prepareStatement(verifyMedicament)) {
            stmt.setString(1, medicament);
            stmt.setInt(2, dose);
            stmt.setInt(3, idUnite);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking medicament: " + e.getMessage());
        }
        return false;
    }

    public double getMedicamentPUvente(int idDci) throws SQLException{
        String getId = "SELECT pu_vente FROM medicament WHERE id_medicament = ?";
        try(PreparedStatement stmt = conn.prepareStatement(getId)){
            stmt.setInt(1, idDci);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt("pu_vente");
                }
            }
        }
        return -1;
    }

    public ObservableList<Medicament> getMedicamentStock(int idDci) throws SQLException{
        ObservableList<Medicament> stockList = FXCollections.observableArrayList();
        String getId = "SELECT stock, stock_min, stock_max FROM medicament WHERE id_medicament = ?";
        try(PreparedStatement stmt = conn.prepareStatement(getId)){
            stmt.setInt(1, idDci);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Medicament medicamentStock = new Medicament(
                        rs.getInt("stock"),
                        rs.getInt("stock_min"),
                        rs.getInt("stock_max")
                    );
                    stockList.add(medicamentStock);
                }
            }
        }
        return stockList;
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

    public boolean MAJprix(int idDci, Medicament medicament) throws SQLException{
        String majPrix = "UPDATE medicament SET pu_achat = ?, pu_vente = ? WHERE id_medicament = ?";
        try(PreparedStatement stmt = conn.prepareStatement(majPrix)) {
            stmt.setDouble(1, medicament.getPuAchat());
            stmt.setDouble(2, medicament.getPuVente());
            stmt.setInt(3, idDci);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean addMedicament(Medicament medicament) throws SQLException{
        String ajouterMedicament = "INSERT INTO medicament (dci, pu_achat, pu_vente, stock, stock_min, stock_max, ordonnance, id_fournisseur, id_famille, id_forme, dosage, dosage_unite) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            stmt.setInt(11, medicament.getDosage());
            stmt.setString(12, medicament.getDosageUnite());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean modifierMedicament(int idDci, Medicament medicament) throws SQLException{
        String modifyMedicament = "UPDATE medicament SET dci = ?, pu_achat = ?, pu_vente = ?, stock = ?, stock_min = ?, stock_max = ?, ordonnance = ?, id_fournisseur = ?, id_famille = ?, id_forme = ?, dosage = ?, dosage_unite = ? WHERE id_medicament = ?";
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
            stmt.setInt(11, medicament.getDosage());
            stmt.setString(12, medicament.getDosageUnite());
            stmt.setInt(13, idDci);
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

package DAO;

import dataBase.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandeDAO {

    private final Connection conn;
    public CommandeDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public ObservableList<Commande> getAllCommande() throws SQLException {
        ObservableList<Commande> allCommandeList = FXCollections.observableArrayList();
        String allCommande = "SELECT c.num_commande, c.qte_commande, c.montant, m.dci, m.stock, m.stock_min, m.stock_max, m.pu_vente, f.nom FROM rcpcommande c JOIN medicament m ON c.id_medicament = m.id_medicament JOIN fournisseur f ON m.id_fournisseur = f.id_fournisseur WHERE c.statut_panier = true AND statut_commande = false";
        try (PreparedStatement stmt = conn.prepareStatement(allCommande);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getString("num_commande"),
                        rs.getInt("qte_commande"),
                        rs.getBigDecimal("montant"),
                        rs.getString("dci"),
                        rs.getInt("stock"),
                        rs.getInt("stock_min"),
                        rs.getInt("stock_max"),
                        rs.getDouble("pu_vente"),
                        rs.getString("nom")
                );
                allCommandeList.add(commande);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching commande: " + e.getMessage());
        }
        return allCommandeList;
    }

    public ObservableList<Commande> getAllRcpCommande() throws SQLException {
        ObservableList<Commande> allRcpCommandeList = FXCollections.observableArrayList();
        String allRcpCommande = "SELECT c.num_commande, c.qte_commande, c.montant, m.id_medicament, m.dci, m.stock, m.stock_min, m.stock_max, m.pu_vente, f.nom FROM rcpcommande c JOIN medicament m ON c.id_medicament = m.id_medicament JOIN fournisseur f ON m.id_fournisseur = f.id_fournisseur WHERE c.statut_panier = false AND statut_commande = false";
        try (PreparedStatement stmt = conn.prepareStatement(allRcpCommande);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getString("num_commande"),
                        rs.getInt("qte_commande"),
                        rs.getBigDecimal("montant"),
                        rs.getInt("id_medicament"),
                        rs.getString("dci"),
                        rs.getInt("stock"),
                        rs.getInt("stock_min"),
                        rs.getInt("stock_max"),
                        rs.getDouble("pu_vente"),
                        rs.getString("nom")
                );
                allRcpCommandeList.add(commande);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching commande: " + e.getMessage());
        }
        return allRcpCommandeList;
    }

    public boolean validerCommandePanier(String numCommande) throws SQLException{
        String addCommande = "UPDATE rcpcommande SET statut_panier = ? WHERE num_commande = ?";
        try(PreparedStatement stmt = conn.prepareStatement(addCommande)) {
            stmt.setBoolean(1, false);
            stmt.setString(2, numCommande);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean validerCommande(String numCommande) throws SQLException{
        String validerCommande = "UPDATE rcpcommande SET statut_commande = ? WHERE num_commande = ?";
        try(PreparedStatement stmt = conn.prepareStatement(validerCommande)) {
            stmt.setBoolean(1, true);
            stmt.setString(2, numCommande);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateStockMedicament(int idDci, int addStock) throws SQLException{
        String updateStock = "UPDATE medicament SET stock = ? WHERE id_medicament = ?";
        try(PreparedStatement stmt = conn.prepareStatement(updateStock)) {
            stmt.setInt(1, addStock);
            stmt.setInt(2, idDci);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean addRecipient(String pdfFile, String numCommande) throws SQLException{
        String ajouterRecipient = "INSERT INTO recipient (pdf_file, num_commande) VALUES (?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(ajouterRecipient)) {
            stmt.setString(1, pdfFile);
            stmt.setString(2, numCommande);
            return stmt.executeUpdate() > 0;
        }
    }
}

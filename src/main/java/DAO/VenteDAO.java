package DAO;

import dataBase.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Facture;
import models.Medicament;
import models.Prescription;
import models.Vente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VenteDAO {

    private final Connection conn;
    public VenteDAO() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

//    public String getNumVente(int idDci) throws SQLException{
//        String numVente = "SELECT num_vente FROM vente WHERE id_medicament = ?";
//        try(PreparedStatement stmt = conn.prepareStatement(numVente)){
//            stmt.setInt(1, idDci);
//            try(ResultSet rs = stmt.executeQuery()){
//                if(rs.next()){
//                    return rs.getInt("pu_vente");
//                }
//            }
//        }
//        return String.valueOf(-1);
//    }

    public boolean addVente(Vente vente) throws SQLException{
        String ajouterVente = "INSERT INTO vente (qte_demande, montant, statut_paiement, id_medicament, statut_panier) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(ajouterVente)) {
            stmt.setInt(1, vente.getQteDemande());
            stmt.setBigDecimal(2, vente.getMontant());
            stmt.setBoolean(3, vente.isStatutPaiement());
            stmt.setInt(4, vente.getIdMedicament());
            stmt.setBoolean(5,true);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateNewStock(int idDci, int newStock) throws SQLException{
        String newCurrentStock = "UPDATE medicament SET stock = ? WHERE id_medicament = ?";
        try(PreparedStatement stmt = conn.prepareStatement(newCurrentStock)) {
            stmt.setDouble(1, newStock);
            stmt.setInt(2, idDci);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean addPrescription(Prescription prescription) throws SQLException{
        String ajouterVente = "INSERT INTO prescription (nom_medecin, nom_patient, prenom_medecin, prenom_patient) VALUES (?, ?, ?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(ajouterVente)) {
            stmt.setString(1, prescription.getNomMedecin());
            stmt.setString(2, prescription.getNomPatient());
            stmt.setString(3, prescription.getPrenomMedecin());
            stmt.setString(4, prescription.getPrenomPatient());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean addFacture(Facture facture) throws SQLException{
        String ajouterVente = "INSERT INTO facture (pdf_file, num_vente) VALUES (?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(ajouterVente)) {
            stmt.setString(1, facture.getPdfFile());
            stmt.setString(2, facture.getNumVente());
            return stmt.executeUpdate() > 0;
        }
    }

    public ObservableList<Vente> getAllPanier() throws SQLException{
        ObservableList<Vente> allNonVendu = FXCollections.observableArrayList();
        String getNonVendu = "SELECT v.num_vente, m.dci, m.pu_vente, v.qte_demande, v.montant FROM medicament m JOIN vente v ON m.id_medicament = v.id_medicament WHERE v.statut_panier = true";
        try(PreparedStatement stmt = conn.prepareStatement(getNonVendu)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Vente vente = new Vente(
                    rs.getString("num_vente"),
                    rs.getString("dci"),
                    rs.getDouble("pu_vente"),
                    rs.getInt("qte_demande"),
                    rs.getBigDecimal("montant")
                );
                allNonVendu.add(vente);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching medicament: " + e.getMessage());
        }
        return allNonVendu;
    }

    public boolean validerVentePanier(String numVente) throws SQLException{
        String newCurrentStock = "UPDATE vente SET statut_panier = ? WHERE num_vente = ?";
        try(PreparedStatement stmt = conn.prepareStatement(newCurrentStock)) {
            stmt.setBoolean(1, false);
            stmt.setString(2, numVente);
            return stmt.executeUpdate() > 0;
        }
    }

    public ObservableList<Vente> getAllNonVendu() throws SQLException{
        ObservableList<Vente> allNonVendu = FXCollections.observableArrayList();
        String getNonVendu = "SELECT v.num_vente, m.dci, m.pu_vente, v.qte_demande, v.montant FROM medicament m JOIN vente v ON m.id_medicament = v.id_medicament WHERE v.statut_panier = false AND statut_paiement = false";
        try(PreparedStatement stmt = conn.prepareStatement(getNonVendu)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Vente vente = new Vente(
                        rs.getString("num_vente"),
                        rs.getString("dci"),
                        rs.getDouble("pu_vente"),
                        rs.getInt("qte_demande"),
                        rs.getBigDecimal("montant")
                );
                allNonVendu.add(vente);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching medicament: " + e.getMessage());
        }
        return allNonVendu;
    }

    public boolean validerVente(String numVente) throws SQLException{
        String newCurrentStock = "UPDATE vente SET statut_paiement = ? WHERE num_vente = ?";
        try(PreparedStatement stmt = conn.prepareStatement(newCurrentStock)) {
            stmt.setBoolean(1, true);
            stmt.setString(2, numVente);
            return stmt.executeUpdate() > 0;
        }
    }
}

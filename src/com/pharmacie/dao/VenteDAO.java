package com.pharmacie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.pharmacie.model.Vente;
import com.pharmacie.util.DBConnection;

public class VenteDAO {
    private Connection connection;

    public VenteDAO() {
        this.connection = DBConnection.getConnection();
    }

    // Enregistrer une nouvelle vente
    public void enregistrerVente(Vente vente) {
        String sql = "INSERT INTO T_Vente (id_medicament, quantite, prix_total, date_vente) VALUES (?, ?, ?, ?)";
        String updateStockSql = "UPDATE T_Medicament SET stock = stock - ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            // Insérer la vente
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, vente.getIdMedicament());
                stmt.setInt(2, vente.getQuantite());
                stmt.setDouble(3, vente.getPrixTotal());
                if (vente.getDateVente() != null) {
                    stmt.setTimestamp(4, vente.getDateVente());
                } else {
                    stmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
                }
                stmt.executeUpdate();
            }

            // Mettre à jour le stock
            try (PreparedStatement stmt = connection.prepareStatement(updateStockSql)) {
                stmt.setInt(1, vente.getQuantite());
                stmt.setInt(2, vente.getIdMedicament());
                stmt.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    // Récupérer les statistiques de ventes par famille
    public Map<String, Double> getVentesParFamille() {
        Map<String, Double> stats = new HashMap<>();
        String sql = "SELECT m.famille, SUM(v.prix_total) as total " +
                "FROM T_Vente v " +
                "JOIN T_Medicament m ON v.id_medicament = m.id " +
                "GROUP BY m.famille";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                stats.put(rs.getString("famille"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}

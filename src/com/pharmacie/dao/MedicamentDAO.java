package com.pharmacie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pharmacie.model.Medicament;
import com.pharmacie.util.DBConnection;

public class MedicamentDAO {
    private Connection connection;

    public MedicamentDAO() {
        this.connection = DBConnection.getConnection();
    }

    // Fonction pour ajouter un médicament dans la base
    public void ajouter(Medicament med) {
        String sql = "INSERT INTO T_Medicament (nom, famille, prix, stock, id_fournisseur) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, med.getNom());
            stmt.setString(2, med.getFamille());
            stmt.setDouble(3, med.getPrix());
            stmt.setInt(4, med.getStock());
            if (med.getIdFournisseur() > 0)
                stmt.setInt(5, med.getIdFournisseur());
            else
                stmt.setNull(5, java.sql.Types.INTEGER);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier les infos d'un médicament existant
    public void modifier(Medicament med) {
        String sql = "UPDATE T_Medicament SET nom=?, famille=?, prix=?, stock=?, id_fournisseur=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, med.getNom());
            stmt.setString(2, med.getFamille());
            stmt.setDouble(3, med.getPrix());
            stmt.setInt(4, med.getStock());
            if (med.getIdFournisseur() > 0)
                stmt.setInt(5, med.getIdFournisseur());
            else
                stmt.setNull(5, java.sql.Types.INTEGER);
            stmt.setInt(6, med.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un médicament (attention ça supprime aussi ses ventes)
    public void supprimer(int id) {
        String sqlVente = "DELETE FROM T_Vente WHERE id_medicament=?";
        String sqlMed = "DELETE FROM T_Medicament WHERE id=?";

        try {
            // Il faut d'abord supprimer les ventes liées à ce médicament
            try (PreparedStatement stmt = connection.prepareStatement(sqlVente)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            // Ensuite on peut supprimer le médicament
            try (PreparedStatement stmt = connection.prepareStatement(sqlMed)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer tous les médicaments
    public List<Medicament> lister() {
        List<Medicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM T_Medicament";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Medicament(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("famille"),
                        rs.getDouble("prix"),
                        rs.getInt("stock"),
                        rs.getInt("id_fournisseur")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // Chercher des médicaments par nom ou famille
    public List<Medicament> rechercher(String motCle) {
        List<Medicament> liste = new ArrayList<>();
        String sql = "SELECT * FROM T_Medicament WHERE nom LIKE ? OR famille LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + motCle + "%");
            stmt.setString(2, "%" + motCle + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                liste.add(new Medicament(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("famille"),
                        rs.getDouble("prix"),
                        rs.getInt("stock"),
                        rs.getInt("id_fournisseur")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // Trouver un médicament par son ID
    public Medicament getById(int id) {
        String sql = "SELECT * FROM T_Medicament WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Medicament(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("famille"),
                        rs.getDouble("prix"),
                        rs.getInt("stock"),
                        rs.getInt("id_fournisseur"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

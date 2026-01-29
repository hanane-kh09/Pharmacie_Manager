package com.pharmacie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pharmacie.model.Fournisseur;
import com.pharmacie.util.DBConnection;

public class FournisseurDAO {
    private Connection connection;

    public FournisseurDAO() {
        this.connection = DBConnection.getConnection();
    }

    // Ajouter un nouveau fournisseur
    public void ajouter(Fournisseur fournisseur) {
        String sql = "INSERT INTO T_Fournisseur (nom, ville, contact) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getVille());
            stmt.setString(3, fournisseur.getContact());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lister tous les fournisseurs
    public List<Fournisseur> lister() {
        List<Fournisseur> liste = new ArrayList<>();
        String sql = "SELECT * FROM T_Fournisseur";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Fournisseur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("ville"),
                        rs.getString("contact")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    // Modifier un fournisseur
    public void modifier(Fournisseur f) {
        String sql = "UPDATE T_Fournisseur SET nom=?, ville=?, contact=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, f.getNom());
            stmt.setString(2, f.getVille());
            stmt.setString(3, f.getContact());
            stmt.setInt(4, f.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un fournisseur
    public void supprimer(int id) {
        String sql = "DELETE FROM T_Fournisseur WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

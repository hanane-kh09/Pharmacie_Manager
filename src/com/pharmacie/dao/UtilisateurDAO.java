package com.pharmacie.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.pharmacie.util.DBConnection;

public class UtilisateurDAO {
    private Connection connection;

    public UtilisateurDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean authentifier(String login, String password) {
        String sql = "SELECT * FROM T_Utilisateur WHERE login = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, password); // En production, il faut hasher le mot de passe !
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retourne vrai si une ligne correspond
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour créer la table si elle n'existe pas (sera appelée par
    // l'installer)
    public void creerTable() {
        String sql = "CREATE TABLE IF NOT EXISTS T_Utilisateur (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "login VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(100) NOT NULL)";
        try (java.sql.Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);

            // Ajouter admin par défaut si vide
            stmt.executeUpdate("INSERT IGNORE INTO T_Utilisateur (login, password) VALUES ('admin', 'admin')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

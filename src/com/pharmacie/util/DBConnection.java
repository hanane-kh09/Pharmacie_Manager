package com.pharmacie.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.File;

public class DBConnection {
    private static Connection connection = null;

    // Constructeur privé pour le pattern Singleton
    private DBConnection() {
        try {
            // Charger le driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Lire les paramètres de connexion depuis le fichier db.properties
            Properties props = new Properties();
            File CONFIG_FILE = new File("db.properties");

            String url = "jdbc:mysql://localhost:3306/db_pharmacie";
            String user = "root";
            String password = "";

            if (CONFIG_FILE.exists()) {
                try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                    props.load(fis);
                    url = props.getProperty("db.url", url);
                    user = props.getProperty("db.user", user);
                    password = props.getProperty("db.password", password);
                } catch (IOException e) {
                    System.err.println("Erreur de lecture de db.properties : " + e.getMessage());
                }
            } else {
                System.out.println("Fichier db.properties introuvable, utilisation des valeurs par défaut.");
            }

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion à la base de données réussie !");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur de connexion à la base de données.");
        }
    }

    // Récupérer la connexion (pattern Singleton)
    public static Connection getConnection() {
        if (connection == null) {
            new DBConnection();
        } else {
            try {
                if (connection.isClosed()) {
                    new DBConnection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}

-- Script pour créer la base de données de la pharmacie
CREATE DATABASE IF NOT EXISTS db_pharmacie;
USE db_pharmacie;

-- Table pour stocker les fournisseurs
CREATE TABLE IF NOT EXISTS T_Fournisseur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    ville VARCHAR(100),
    contact VARCHAR(100)
);

-- Table des médicaments
CREATE TABLE IF NOT EXISTS T_Medicament (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    famille VARCHAR(100),
    prix DOUBLE NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    id_fournisseur INT,
    CONSTRAINT fk_fournisseur FOREIGN KEY (id_fournisseur) REFERENCES T_Fournisseur(id) ON DELETE SET NULL
);

-- Table pour enregistrer les ventes
CREATE TABLE IF NOT EXISTS T_Vente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_medicament INT NOT NULL,
    date_vente DATETIME DEFAULT CURRENT_TIMESTAMP,
    quantite INT NOT NULL,
    prix_total DOUBLE NOT NULL,
    CONSTRAINT fk_medicament FOREIGN KEY (id_medicament) REFERENCES T_Medicament(id)
);

-- Quelques données de test pour commencer
INSERT INTO T_Fournisseur (nom, ville, contact) VALUES 
('PharmaDistribute', 'Paris', 'contact@pharmadistrib.fr'),
('MediGlobal', 'Lyon', 'service.client@mediglobal.com');

INSERT INTO T_Medicament (nom, famille, prix, stock, id_fournisseur) VALUES 
('Doliprane 1000', 'Antalgique', 2.50, 100, 1),
('Smecta', 'Digestif', 5.90, 50, 1),
('Amoxicilline', 'Antibiotique', 7.20, 30, 2);

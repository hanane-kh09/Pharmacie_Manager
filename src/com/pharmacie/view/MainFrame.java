package com.pharmacie.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // Couleurs du thème
    public static final Color PRIMARY_COLOR = Color.decode("#7cc248");
    public static final Color SECONDAY_COLOR = Color.decode("#1d3809");
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public MainFrame() {
        initComponents();
        initCustomDesign();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pharmacie Manager");
        setSize(1000, 700);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new java.awt.BorderLayout());

        // Création des onglets
        tabbedPane = new javax.swing.JTabbedPane();

        medicamentPanel = new MedicamentPanel();
        ventePanel = new VentePanel();
        statsPanel = new StatistiquesPanel();
        fournisseurPanel = new FournisseurPanel();

        tabbedPane.addTab("Médicaments", medicamentPanel);
        tabbedPane.addTab("Fournisseurs", fournisseurPanel);
        tabbedPane.addTab("Ventes", ventePanel);
        tabbedPane.addTab("Statistiques", statsPanel);

        // Vérifier le stock faible au démarrage
        SwingUtilities.invokeLater(this::checkLowStock);

        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

        // Quand on change d'onglet, rafraichir les données
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == ventePanel) {
                ventePanel.chargerMedicaments();
            } else if (tabbedPane.getSelectedComponent() == statsPanel) {
                statsPanel.rafraichir();
            }
        });

        pack();
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    private void initCustomDesign() {
        // Panneau du haut avec le logo
        javax.swing.JPanel headerPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        headerPanel.setBackground(SECONDAY_COLOR);
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Charger le logo
        ImageIcon logoIcon = new ImageIcon("resources/images/logo.png");
        Image scaledImg = logoIcon.getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(" Pharmacie Manager", new ImageIcon(scaledImg), SwingConstants.LEFT);
        lblLogo.setFont(HEADER_FONT);
        lblLogo.setForeground(TEXT_COLOR);

        headerPanel.add(lblLogo, java.awt.BorderLayout.WEST);
        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        // Icône de la fenêtre
        setIconImage(logoIcon.getImage());
    }

    // Variables
    private javax.swing.JTabbedPane tabbedPane;
    private com.pharmacie.view.MedicamentPanel medicamentPanel;
    private com.pharmacie.view.VentePanel ventePanel;
    private com.pharmacie.view.StatistiquesPanel statsPanel;
    private com.pharmacie.view.FournisseurPanel fournisseurPanel;

    // Vérifier s'il y a des médicaments avec stock faible
    private void checkLowStock() {
        com.pharmacie.dao.MedicamentDAO dao = new com.pharmacie.dao.MedicamentDAO();
        java.util.List<com.pharmacie.model.Medicament> list = dao.lister();
        StringBuilder sb = new StringBuilder();
        for (com.pharmacie.model.Medicament m : list) {
            if (m.getStock() < 10) {
                sb.append("- ").append(m.getNom()).append(" (Stock: ").append(m.getStock()).append(")\n");
            }
        }
        if (sb.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "ATTENTION : Stock critique pour les médicaments suivants :\n" + sb.toString(),
                    "Alerte Stock Faible",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Utiliser le thème Nimbus pour un meilleur rendu
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.put("nimbusBase", PRIMARY_COLOR);
                    UIManager.put("nimbusBlueGrey", PRIMARY_COLOR);
                    UIManager.put("control", new Color(245, 250, 245));
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus n'est pas dispo, utiliser le thème par défaut
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
            }
        }

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}

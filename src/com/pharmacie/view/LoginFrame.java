package com.pharmacie.view;

import javax.swing.*;
import java.awt.*;
import com.pharmacie.dao.UtilisateurDAO;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Connexion - Pharmacie Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Logo ou Titre
        JLabel lblTitle = new JLabel("Connexion");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(124, 194, 72));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        // Champs
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 1;
        panel.add(new JLabel("Utilisateur :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtUser = new JTextField(15);
        panel.add(txtUser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Mot de passe :"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField txtPass = new JPasswordField(15);
        panel.add(txtPass, gbc);

        // Boutons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnLogin = new JButton("Se connecter");
        btnLogin.setBackground(new Color(124, 194, 72));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panel.add(btnLogin, gbc);

        add(panel);

        // Action
        btnLogin.addActionListener(e -> {
            String login = txtUser.getText();
            String pass = new String(txtPass.getPassword());

            UtilisateurDAO dao = new UtilisateurDAO();
            if (dao.authentifier(login, pass)) {
                dispose();
                new MainFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Identifiants incorrects.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Appuyer sur EnTrée pour valider
        getRootPane().setDefaultButton(btnLogin);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        new LoginFrame().setVisible(true);
    }
}

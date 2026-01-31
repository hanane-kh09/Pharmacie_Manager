package com.pharmacie.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.pharmacie.dao.FournisseurDAO;
import com.pharmacie.model.Fournisseur;
import java.awt.*;
import java.util.List;

public class FournisseurPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private FournisseurDAO fournisseurDAO;

    private JTextField txtNom, txtVille, txtContact;

    public FournisseurPanel() {
        fournisseurDAO = new FournisseurDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Le tableau pour afficher tous les fournisseurs
        String[] colonnes = { "ID", "Nom", "Ville", "Contact" };
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Formulaire pour ajouter/modifier les infos d'un fournisseur
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Détails du Fournisseur"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelForm.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        txtNom = new JTextField(15);
        panelForm.add(txtNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelForm.add(new JLabel("Ville :"), gbc);
        gbc.gridx = 1;
        txtVille = new JTextField(15);
        panelForm.add(txtVille, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Contact :"), gbc);
        gbc.gridx = 1;
        txtContact = new JTextField(15);
        panelForm.add(txtContact, gbc);

        // Les boutons d'action
        JPanel panelBtn = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnAjouter = createStyledButton("Ajouter", MainFrame.PRIMARY_COLOR);
        JButton btnModifier = createStyledButton("Modifier", Color.ORANGE);
        JButton btnSupprimer = createStyledButton("Supprimer", Color.RED);
        JButton btnVider = createStyledButton("Vider", Color.GRAY);

        panelBtn.add(btnAjouter);
        panelBtn.add(btnModifier);
        panelBtn.add(btnSupprimer);
        panelBtn.add(btnVider);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelForm.add(panelBtn, gbc);

        add(panelForm, BorderLayout.EAST);

        // Connecter les boutons
        btnAjouter.addActionListener(e -> ajouter());
        btnModifier.addActionListener(e -> modifier());
        btnSupprimer.addActionListener(e -> supprimer());
        btnVider.addActionListener(e -> viderChamps());

        table.getSelectionModel().addListSelectionListener(e -> remplirFormulaire());

        chargerDonnees();
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return btn;
    }

    private void chargerDonnees() {
        tableModel.setRowCount(0);
        List<Fournisseur> list = fournisseurDAO.lister();
        for (Fournisseur f : list) {
            tableModel.addRow(new Object[] { f.getId(), f.getNom(), f.getVille(), f.getContact() });
        }
    }

    private void ajouter() {
        if (txtNom.getText().isEmpty())
            return;
        Fournisseur f = new Fournisseur(0, txtNom.getText(), txtVille.getText(), txtContact.getText());
        fournisseurDAO.ajouter(f);
        chargerDonnees();
        viderChamps();
    }

    private void modifier() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        int id = (int) tableModel.getValueAt(row, 0);
        Fournisseur f = new Fournisseur(id, txtNom.getText(), txtVille.getText(), txtContact.getText());
        fournisseurDAO.modifier(f);
        chargerDonnees();
        viderChamps();
    }

    private void supprimer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un fournisseur.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce fournisseur ?", "Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            fournisseurDAO.supprimer(id);
            chargerDonnees();
            viderChamps();
        }
    }

    private void remplirFormulaire() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtNom.setText(tableModel.getValueAt(row, 1).toString());
            txtVille.setText(tableModel.getValueAt(row, 2).toString());
            txtContact.setText(tableModel.getValueAt(row, 3).toString());
        }
    }

    private void viderChamps() {
        txtNom.setText("");
        txtVille.setText("");
        txtContact.setText("");
        table.clearSelection();
    }
}

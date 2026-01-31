package com.pharmacie.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.pharmacie.dao.FournisseurDAO;
import com.pharmacie.dao.MedicamentDAO;
import com.pharmacie.model.Fournisseur;
import com.pharmacie.model.Medicament;

import java.awt.*;
import java.util.List;

public class MedicamentPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private MedicamentDAO medicamentDAO;
    private FournisseurDAO fournisseurDAO;

    private JTextField txtNom, txtFamille, txtPrix, txtStock, txtRecherche;
    private JComboBox<Fournisseur> cbFournisseur;

    public MedicamentPanel() {
        medicamentDAO = new MedicamentDAO();
        fournisseurDAO = new FournisseurDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Partie recherche en haut
        JPanel panelNord = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNord.setBackground(new Color(245, 245, 245));
        txtRecherche = new JTextField(20);
        JButton btnRecherche = new JButton("Rechercher");
        btnRecherche.addActionListener(e -> chargerDonnees(txtRecherche.getText()));
        panelNord.add(new JLabel("Rechercher (Nom/Famille) : "));
        panelNord.add(txtRecherche);
        panelNord.add(btnRecherche);
        add(panelNord, BorderLayout.NORTH);

        // Le tableau avec tous les médicaments
        String[] colonnes = { "ID", "Nom", "Famille", "Prix", "Stock", "ID Fournisseur", "Alerte" };
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setDefaultRenderer(Object.class, new StocksAlertRenderer());

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Formulaire pour ajouter/modifier un médicament
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Détails du Médicament"),
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
        panelForm.add(new JLabel("Famille :"), gbc);
        gbc.gridx = 1;
        txtFamille = new JTextField(15);
        panelForm.add(txtFamille, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelForm.add(new JLabel("Prix (€) :"), gbc);
        gbc.gridx = 1;
        txtPrix = new JTextField(15);
        panelForm.add(txtPrix, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelForm.add(new JLabel("Stock :"), gbc);
        gbc.gridx = 1;
        txtStock = new JTextField(15);
        panelForm.add(txtStock, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panelForm.add(new JLabel("Fournisseur :"), gbc);
        gbc.gridx = 1;
        cbFournisseur = new JComboBox<>();
        panelForm.add(cbFournisseur, gbc);

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
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panelForm.add(panelBtn, gbc);

        add(panelForm, BorderLayout.EAST);

        // Connecter les boutons aux fonctions
        btnAjouter.addActionListener(e -> ajouterMedicament());
        btnModifier.addActionListener(e -> modifierMedicament());
        btnSupprimer.addActionListener(e -> supprimerMedicament());
        btnVider.addActionListener(e -> viderChamps());

        // Si on clique sur une ligne du tableau, ça remplit le formulaire automatiquement
        table.getSelectionModel().addListSelectionListener(e -> remplirFormulaire());

        chargerFournisseurs();
        chargerDonnees("");
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return btn;
    }

    private void chargerFournisseurs() {
        cbFournisseur.removeAllItems();
        List<Fournisseur> list = fournisseurDAO.lister();
        for (Fournisseur f : list) {
            cbFournisseur.addItem(f);
        }
    }

    private void chargerDonnees(String motCle) {
        tableModel.setRowCount(0);
        List<Medicament> list = motCle.isEmpty() ? medicamentDAO.lister() : medicamentDAO.rechercher(motCle);
        for (Medicament m : list) {
            String alerte = m.getStock() < 10 ? "STOCK FAIBLE" : "OK";
            tableModel.addRow(new Object[] {
                    m.getId(), m.getNom(), m.getFamille(), m.getPrix(), m.getStock(), m.getIdFournisseur(), alerte
            });
        }
    }

    private void ajouterMedicament() {
        try {
            Medicament m = new Medicament(
                    txtNom.getText(),
                    txtFamille.getText(),
                    Double.parseDouble(txtPrix.getText()),
                    Integer.parseInt(txtStock.getText()),
                    ((Fournisseur) cbFournisseur.getSelectedItem()).getId());
            medicamentDAO.ajouter(m);
            chargerDonnees("");
            viderChamps();
            JOptionPane.showMessageDialog(this, "Médicament ajouté !");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur de saisie : " + ex.getMessage());
        }
    }

    private void modifierMedicament() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            Medicament m = new Medicament(
                    id,
                    txtNom.getText(),
                    txtFamille.getText(),
                    Double.parseDouble(txtPrix.getText()),
                    Integer.parseInt(txtStock.getText()),
                    ((Fournisseur) cbFournisseur.getSelectedItem()).getId());
            medicamentDAO.modifier(m);
            chargerDonnees("");
            viderChamps();
            JOptionPane.showMessageDialog(this, "Médicament modifié !");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void supprimerMedicament() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer ce médicament ?\n\n" +
                        "⚠️ ATTENTION : Tout l'historique des ventes associé sera supprimé.\n" +
                        "Cela affectera les statistiques.",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            medicamentDAO.supprimer(id);
            chargerDonnees("");
            viderChamps();
        }
    }

    private void remplirFormulaire() {
        int row = table.getSelectedRow();
        if (row != -1) {
            txtNom.setText(tableModel.getValueAt(row, 1).toString());
            txtFamille.setText(tableModel.getValueAt(row, 2).toString());
            txtPrix.setText(tableModel.getValueAt(row, 3).toString());
            txtStock.setText(tableModel.getValueAt(row, 4).toString());
            
            // Sélectionner le bon fournisseur
            int idFournisseur = (int) tableModel.getValueAt(row, 5);
            for (int i = 0; i < cbFournisseur.getItemCount(); i++) {
                if (cbFournisseur.getItemAt(i).getId() == idFournisseur) {
                    cbFournisseur.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void viderChamps() {
        txtNom.setText("");
        txtFamille.setText("");
        txtPrix.setText("");
        txtStock.setText("");
        table.clearSelection();
    }

    // Cette classe permet de colorer en rouge les lignes où le stock est faible
    private class StocksAlertRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                try {
                    int stock = Integer.parseInt(table.getValueAt(row, 4).toString());
                    if (stock < 10) {
                        c.setBackground(new Color(255, 200, 200));
                        c.setForeground(Color.RED);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } catch (Exception e) {
                }
            }
            return c;
        }
    }
}

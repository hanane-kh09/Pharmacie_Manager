package com.pharmacie.view;

import javax.swing.*;

import com.pharmacie.dao.MedicamentDAO;
import com.pharmacie.dao.VenteDAO;
import com.pharmacie.model.Medicament;
import com.pharmacie.model.Vente;

import java.awt.*;
import java.util.List;

public class VentePanel extends JPanel {
    private JComboBox<Medicament> cbMedicament;
    private JTextField txtQuantite;
    private JSpinner dateSpinner;
    private JLabel lblPrixUnitaire, lblPrixTotal, lblStock;
    private JButton btnValider;

    private MedicamentDAO medicamentDAO;
    private VenteDAO venteDAO;

    public VentePanel() {
        medicamentDAO = new MedicamentDAO();
        venteDAO = new VenteDAO();
        setLayout(new GridBagLayout());

        JPanel panelForm = new JPanel(new GridLayout(7, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Nouvelle Vente"));

        cbMedicament = new JComboBox<>();
        txtQuantite = new JTextField("1");
        lblPrixUnitaire = new JLabel("0.0 €");
        lblPrixUnitaire.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrixTotal = new JLabel("0.0 €");
        lblPrixTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblPrixTotal.setForeground(MainFrame.PRIMARY_COLOR);

        lblStock = new JLabel("Stock : 0");

        btnValider = new JButton("Valider la Vente");
        btnValider.setBackground(MainFrame.PRIMARY_COLOR);
        btnValider.setForeground(Color.BLACK);
        btnValider.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelForm.add(new JLabel("Médicament :"));
        panelForm.add(cbMedicament);

        panelForm.add(new JLabel("Stock disponible :"));
        panelForm.add(lblStock);

        panelForm.add(new JLabel("Prix Unitaire :"));
        panelForm.add(lblPrixUnitaire);

        panelForm.add(new JLabel("Quantité :"));
        panelForm.add(txtQuantite);

        panelForm.add(new JLabel("Total à payer :"));
        panelForm.add(lblPrixTotal);

        // Sélecteur de date
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new java.util.Date());

        panelForm.add(new JLabel("Date :"));
        panelForm.add(dateSpinner);

        panelForm.add(new JLabel(""));
        panelForm.add(btnValider);

        add(panelForm);

        // Charger la liste des médicaments
        chargerMedicaments();

        // Quand on change de médicament, mettre à jour les infos
        cbMedicament.addActionListener(e -> updateInfoMedicament());

        btnValider.addActionListener(e -> validerVente());

        // Afficher les infos du premier médicament
        if (cbMedicament.getItemCount() > 0)
            updateInfoMedicament();
    }

    // Charger tous les médicaments dans la liste
    public void chargerMedicaments() {
        cbMedicament.removeAllItems();
        List<Medicament> list = medicamentDAO.lister();
        for (Medicament m : list) {
            cbMedicament.addItem(m);
        }
    }

    // Mettre à jour le prix et le stock quand on sélectionne un médicament
    private void updateInfoMedicament() {
        Medicament m = (Medicament) cbMedicament.getSelectedItem();
        if (m != null) {
            lblPrixUnitaire.setText(m.getPrix() + " €");
            lblStock.setText(String.valueOf(m.getStock()));
            calculerTotal();
        }
    }

    // Calculer le prix total
    private void calculerTotal() {
        try {
            Medicament m = (Medicament) cbMedicament.getSelectedItem();
            int qte = Integer.parseInt(txtQuantite.getText());
            if (m != null) {
                double total = m.getPrix() * qte;
                lblPrixTotal.setText(String.format("%.2f €", total));
            }
        } catch (NumberFormatException e) {
            lblPrixTotal.setText("Erreur");
        }
    }

    // Enregistrer la vente
    private void validerVente() {
        try {
            Medicament m = (Medicament) cbMedicament.getSelectedItem();
            int qte = Integer.parseInt(txtQuantite.getText());

            if (m == null) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un médicament.");
                return;
            }
            if (qte <= 0) {
                JOptionPane.showMessageDialog(this, "Quantité invalide.");
                return;
            }
            if (qte > m.getStock()) {
                JOptionPane.showMessageDialog(this, "Stock insuffisant !");
                return;
            }

            // Récupérer la date sélectionnée
            java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
            java.sql.Timestamp dateVente = new java.sql.Timestamp(selectedDate.getTime());

            double total = m.getPrix() * qte;
            Vente vente = new Vente(m.getId(), qte, total);
            vente.setDateVente(dateVente);

            venteDAO.enregistrerVente(vente);
            JOptionPane.showMessageDialog(this, "Vente enregistrée avec succès !");

            // Recharger la liste (le stock a changé)
            chargerMedicaments();
            txtQuantite.setText("1");
            dateSpinner.setValue(new java.util.Date());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une quantité valide.");
        }
    }
}

package com.pharmacie.model;

import java.sql.Timestamp;

public class Vente {
    private int id;
    private int idMedicament;
    private Timestamp dateVente;
    private int quantite;
    private double prixTotal;

    public Vente() {
    }

    public Vente(int id, int idMedicament, Timestamp dateVente, int quantite, double prixTotal) {
        this.id = id;
        this.idMedicament = idMedicament;
        this.dateVente = dateVente;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
    }

    // Constructeur sans ID (pour l'ajout)
    public Vente(int idMedicament, int quantite, double prixTotal) {
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdMedicament() { return idMedicament; }
    public void setIdMedicament(int idMedicament) { this.idMedicament = idMedicament; }

    public Timestamp getDateVente() { return dateVente; }
    public void setDateVente(Timestamp dateVente) { this.dateVente = dateVente; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double getPrixTotal() { return prixTotal; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }
}

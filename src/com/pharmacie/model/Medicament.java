package com.pharmacie.model;

public class Medicament {
    private int id;
    private String nom;
    private String famille;
    private double prix;
    private int stock;
    private int idFournisseur;

    public Medicament() {
    }

    public Medicament(int id, String nom, String famille, double prix, int stock, int idFournisseur) {
        this.id = id;
        this.nom = nom;
        this.famille = famille;
        this.prix = prix;
        this.stock = stock;
        this.idFournisseur = idFournisseur;
    }

    // Constructeur quand on ajoute un nouveau médicament (pas besoin de l'ID)
    public Medicament(String nom, String famille, double prix, int stock, int idFournisseur) {
        this.nom = nom;
        this.famille = famille;
        this.prix = prix;
        this.stock = stock;
        this.idFournisseur = idFournisseur;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getFamille() { return famille; }
    public void setFamille(String famille) { this.famille = famille; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getIdFournisseur() { return idFournisseur; }
    public void setIdFournisseur(int idFournisseur) { this.idFournisseur = idFournisseur; }

    @Override
    public String toString() {
        return nom + " (" + stock + ")";
    }
}

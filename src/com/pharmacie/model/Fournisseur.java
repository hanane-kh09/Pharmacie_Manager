package com.pharmacie.model;

public class Fournisseur {
    private int id;
    private String nom;
    private String ville;
    private String contact;

    public Fournisseur() {
    }

    public Fournisseur(int id, String nom, String ville, String contact) {
        this.id = id;
        this.nom = nom;
        this.ville = ville;
        this.contact = contact;
    }

    // Constructeur pour créer un nouveau fournisseur (sans ID)
    public Fournisseur(String nom, String ville, String contact) {
        this.nom = nom;
        this.ville = ville;
        this.contact = contact;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return nom;
    }
}

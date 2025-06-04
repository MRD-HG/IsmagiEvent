package com.boushib.eventsismagi.model;

public class AdminModel {

    private String id;
    private String nom;
    private String email;
    private String role;

    public AdminModel() {}

    public AdminModel(String nom, String email, String role) {
        this.nom = nom;
        this.email = email;
        this.role = role;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

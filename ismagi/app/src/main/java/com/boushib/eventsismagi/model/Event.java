package com.boushib.eventsismagi.model;

public class Event {
    private String id;
    private String createurId;
    private String titre;
    private String description;
    private String lieu;
    private String dateEvent;
    private String heureEvent;
    private int duree;
    private double prix;
    private boolean estPrive;
    private String imageUrl;

    public Event() {}
    public Event(String createurId, String titre, String description, String lieu, String dateEvent, String heureEvent, int duree, double prix, boolean estPrive, String imageUrl) {
        this.createurId = createurId;
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.dateEvent = dateEvent;
        this.heureEvent = heureEvent;
        this.duree = duree;
        this.prix = prix;
        this.estPrive = estPrive;
        this.imageUrl = imageUrl;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCreateurId() { return createurId; }
    public void setCreateurId(String createurId) { this.createurId = createurId; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }
    public String getDateEvent() { return dateEvent; }
    public void setDateEvent(String dateEvent) { this.dateEvent = dateEvent; }
    public String getHeureEvent() { return heureEvent; }
    public void setHeureEvent(String heureEvent) { this.heureEvent = heureEvent; }
    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public boolean isEstPrive() { return estPrive; }
    public void setEstPrive(boolean estPrive) { this.estPrive = estPrive; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

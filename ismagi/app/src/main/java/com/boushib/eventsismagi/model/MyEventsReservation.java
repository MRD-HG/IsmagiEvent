package com.boushib.eventsismagi.model;

import java.util.Date;

public class MyEventsReservation {
    private String userId;
    private String eventId;
    private String paymentType;
    private boolean paymentValid;


  private Date reservationDate;

    private String titre;

    private String lieu;
    private String dateEvent;


    private double prix;

    private String imageUrl;

    public MyEventsReservation(){

    }

    public MyEventsReservation(String userId, String eventId, String paymentType, boolean paymentValid, Date reservationDate, String titre, String lieu, String dateEvent, double prix, String imageUrl) {
        this.userId = userId;
        this.eventId = eventId;
        this.paymentType = paymentType;
        this.paymentValid = paymentValid;
        this.reservationDate = reservationDate;
        this.titre = titre;
        this.lieu = lieu;
        this.dateEvent = dateEvent;
        this.prix = prix;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isPaymentValid() {
        return paymentValid;
    }

    public void setPaymentValid(boolean paymentValid) {
        this.paymentValid = paymentValid;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

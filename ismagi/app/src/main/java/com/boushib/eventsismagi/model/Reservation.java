package com.boushib.eventsismagi.model;


import java.util.Date;

public class Reservation {
    private String userId;       // ID de l'utilisateur
    private String eventId;      // ID de l'événement
    private String paymentType;  // "carte" ou "especes"
    private boolean paymentValid;// false pour especes, true si paiement carte réussi
    private String cardLast4;    // 4 derniers chiffres de la carte (si paiement par carte)
    private Date reservationDate;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private int places;
    public Reservation() {
        // Constructeur vide requis pour Firestore
    }



    public Reservation(String userId,
                       String eventId,
                       String paymentType,
                       boolean paymentValid,
                       String cardLast4,
                       Date reservationDate,
                       String  email,
                       int places) {
        this.userId = userId;
        this.eventId = eventId;
        this.paymentType = paymentType;
        this.paymentValid = paymentValid;
        this.cardLast4 = cardLast4;
        this.reservationDate = reservationDate;
        this.email = email;
        this.places = places;
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

    public String getCardLast4() {
        return cardLast4;
    }

    public void setCardLast4(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }



}
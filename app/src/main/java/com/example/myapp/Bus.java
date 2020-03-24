package com.example.myapp;

public class Bus {
    private String horaire,ligne, direction;

    public Bus(String horaire, String ligne, String direction){
        this.direction=direction;
        this.horaire=horaire;
        this.ligne=ligne;
    }

    public String getHoraire() {
        return horaire;
    }

    public String getDirection() {
        return direction;
    }

    public String getLigne() {
        return ligne;
    }


}

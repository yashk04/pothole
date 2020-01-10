package com.example.pothole.Citizen.maps_citizen;

public class Position {
    double lat;
    double lont;

    public Position(double lat,double lont) {
        this.lat = lat;
        this.lont=lont;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLont() {
        return lont;
    }

    public void setLont(double lont) {
        this.lont = lont;
    }
}

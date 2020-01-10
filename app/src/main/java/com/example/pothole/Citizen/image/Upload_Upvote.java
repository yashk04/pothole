package com.example.pothole.Citizen.image;


public class Upload_Upvote

{
    double lati;
    double longi;
    String potholeid;

    public Upload_Upvote() {

    }

    public Upload_Upvote(double lati, double longi, String potholeid) {
        this.lati = lati;
        this.longi = longi;
        this.potholeid = potholeid;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public String getPotholeid() {
        return potholeid;
    }

    public void setPotholeid(String potholeid) {
        this.potholeid = potholeid;
    }
}

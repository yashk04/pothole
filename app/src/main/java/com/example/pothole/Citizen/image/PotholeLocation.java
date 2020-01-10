package com.example.pothole.Citizen.image;

public class PotholeLocation {

    public double lat;
    public double longi;
    public int noOfUpvotes;
    public int potholeCount;
    public String uploadBy;
    public int statusFlag;
    public String fullAddress;
    public String potholeImageUid;
    public String potholeUid;
    public PotholeLocation(double lat, double longi,int noOfUpvotes,int potholeCount,String uploadBy,String potholeUid,String potholeImageUid,int statusFlag,String fullAddress) {
        this.lat = lat;
        this.longi = longi;
        this.noOfUpvotes=noOfUpvotes;
        this.potholeCount=potholeCount;
        this.uploadBy=uploadBy;
        this.potholeUid=potholeUid;
        this.potholeImageUid=potholeImageUid;
        this.statusFlag = statusFlag;
        this.fullAddress = fullAddress;
    }
    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public void setPotholeImageUid(String potholeImageUid) {
        this.potholeImageUid = potholeImageUid;
    }
    public String getPotholeImageUid() {
        return potholeImageUid;
    }

    public void setPotholeImageUrl(String potholeImageUrl) {
        this.potholeImageUid = potholeImageUid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public int getNoOfUpvotes() {
        return noOfUpvotes;
    }

    public void setNoOfUpvotes(int noOfUpvotes) {
        this.noOfUpvotes = noOfUpvotes;
    }

    public int getPotholeCount() {
        return potholeCount;
    }

    public void setPotholeCount(int potholeCount) {
        this.potholeCount = potholeCount;
    }

    public String getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    public String getPotholeUid() {
        return potholeUid;
    }

    public void setPotholeUid(String potholeUid) {
        this.potholeUid = potholeUid;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}

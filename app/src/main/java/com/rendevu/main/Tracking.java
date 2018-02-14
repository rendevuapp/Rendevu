package com.rendevu.main;

/**
 * Created by ricardo.cantu on 2/6/2018.
 */

public class Tracking {
    private String fullname, uid, lat, lng;

    public Tracking(){

    }

    public Tracking(String uid, String lat, String lng){
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
    }
    public String getUid(){
        return uid;
    }

    public String getLat(){
        return lat;
    }

    public String getLng(){
        return lng;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

    public void setLat(String lat){
        this.lat = lat;
    }

    public void setLng(String lng){
        this.lng = lng;
    }
}

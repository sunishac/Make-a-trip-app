package com.example.chala.group12_hw09_part_a;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by chala on 4/30/2017.
 */

public class PlaceObject implements Serializable {
    String placeName;
    Double latitude;
    Double longitude;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PlaceObject() {


    }



    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

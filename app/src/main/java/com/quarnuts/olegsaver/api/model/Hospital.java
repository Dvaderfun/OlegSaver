package com.quarnuts.olegsaver.api.model;

import com.google.gson.annotations.SerializedName;

public class Hospital {
    @SerializedName("id")
    String id;

    @SerializedName("lat")
    String latitude;

    @SerializedName("lnt")
    String longitude;

    @SerializedName("title")
    String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}

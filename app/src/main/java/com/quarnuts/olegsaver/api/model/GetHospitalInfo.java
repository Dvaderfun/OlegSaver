package com.quarnuts.olegsaver.api.model;

public class GetHospitalInfo {
    String token;
    String lat;
    String lnt;
    String title;

    public GetHospitalInfo(String token, String lat, String lnt, String title) {
        this.token = token;
        this.lat = lat;
        this.lnt = lnt;
        this.title = title;
    }
}

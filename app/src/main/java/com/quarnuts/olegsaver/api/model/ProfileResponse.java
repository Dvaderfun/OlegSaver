package com.quarnuts.olegsaver.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("role")
    private String role;

    public void setToken(String token) {
        this.token = token;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }
}

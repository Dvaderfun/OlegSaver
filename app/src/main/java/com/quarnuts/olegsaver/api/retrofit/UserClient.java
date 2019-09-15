package com.quarnuts.olegsaver.api.retrofit;

import com.quarnuts.olegsaver.api.model.GetHospitalInfo;
import com.quarnuts.olegsaver.api.model.Hospital;
import com.quarnuts.olegsaver.api.model.LoginInformation;
import com.quarnuts.olegsaver.api.model.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface UserClient {
    /*@GET("/task/all")
    Call<List<Task>> getAllTasks();*/

    @POST("/api/signIn")
    Call<ProfileResponse> signIn(@Body LoginInformation loginInformation);

    @POST("/api/getOptimalHospital")
    Call<Hospital> getOptimalHospital(@Body GetHospitalInfo getHospitalInfo);
}

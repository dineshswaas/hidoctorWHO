package com.swaas.kangle.API.service;

import com.swaas.kangle.models.UserTrackingModel;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Sayellessh on 19-06-2017.
 */

public interface UserTrackerService {

    @POST("UserApi/insertUserTracker/{subdomain}")
    Call<String> insertUserTracker(@Body UserTrackingModel userTrackingModel, @Path("subdomain") String subdomain);

}

package com.swaas.kangle.API.service;

import com.swaas.kangle.API.model.AppInfo;
import com.swaas.kangle.models.APIResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;


public interface AppInfoService {
    @GET("UserApi/GetAppDetails/{companyCode}/{devicePlatform}")
    Call<APIResponse<AppInfo>> getLatestAppVersion(@Path("companyCode") String companyCode, @Path("devicePlatform") String osName);
}

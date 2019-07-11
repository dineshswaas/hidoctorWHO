package com.swaas.kangle.Notification;

import java.util.List;

import retrofit.http.GET;
import retrofit.Call;
import retrofit.http.Path;

/**
 * Created by Sayellessh on 17-10-2018.
 */

public interface NotificationService {

    //list of checklist api
    //@GET("bins/lvcs0")
    @GET("bins/wpses")
    Call<List<NotificationModel>> getAvailableNotifications();


    @GET("NotifyHubApi/getNotifiationHubDetails_Mobile/{subdomainName}/{companyId}/{userId}/{utcOffset}")
    Call<List<NotificationModel>> getNotifiationHubDetails(@Path("subdomainName") String subdomainName ,
                                                            @Path("companyId") int companyId ,
                                                            @Path("userId") int userId,
                                                            @Path("utcOffset") String utcOffset);

    @GET("NotifyHubApi/getNotificationHubCount/{subdomainName}/{companyId}/{userId}")
    Call<List<NotificationModel>> getNotificationHubCount(@Path("subdomainName") String subdomainName ,
                                                            @Path("companyId") int companyId ,
                                                            @Path("userId") int userId);

}

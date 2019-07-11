package com.swaas.kangle.API.service;

import com.squareup.okhttp.RequestBody;
import com.swaas.kangle.API.model.CheckUser;
import com.swaas.kangle.API.model.LandingPageAccess;
import com.swaas.kangle.API.model.User;
import com.swaas.kangle.Reports.ReportModel;
import com.swaas.kangle.models.DashBoardDetailsModel;
import com.swaas.kangle.models.MenuModel;
import com.swaas.kangle.models.ThemeModel;
import com.swaas.kangle.models.UpdatePasswordModel;
import com.swaas.kangle.userProfile.EducationAndInstitution;
import com.swaas.kangle.userProfile.UserDetails;
import com.swaas.kangle.userProfile.models.UserRoleModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


/**
 * Created by Sayellessh on 24-04-2017.
 */

public interface UserService {

    @POST("SelfSignOnApi/CheckUserAuthenticationLite/{subdomain}/{userName}")
    Call<CheckUser> checkUserAuthenticationLite(@Body RequestBody password, @Path("subdomain") String subdomain, @Path("userName") String userName);

    @GET("user/getKWUserInfo/{subdomain}/{userId}/{companyId}")
    Call<List<User>> getUserInfo(@Path("userId") int userId , @Path("subdomain") String subdomain , @Path("companyId") int companyId);

    @GET("UserApi/GetLandingPageAccessV1/{subdomain}/{companyId}/{userId}")
    Call<List<LandingPageAccess>> getLandingPageAccess(@Path("subdomain") String subdomain, @Path("userId") int userId, @Path("companyId") int companyId);

    //New Menus Api
    @GET("UserApi/GetLandingmenus_API/{subdomain}/{userId}/{companyId}/{Languagetype}")
    Call<List<MenuModel>> getLandingmenus_API(@Path("subdomain") String subdomain,
                                              @Path("userId") int userId,
                                              @Path("companyId") int companyId,
                                              @Path("Languagetype")String Languagetype);

    @POST("UserApi/AppUpdatePassword/{subdomain}/{companyId}")
    Call<UpdatePasswordModel> updatePassword(@Path("subdomain") String subdomain, @Path("companyId") int companyId, @Body UpdatePasswordModel password);

    @GET("UserApi/CheckCurrentBuildVersionLMS/{subdomain}/{version}/{OS}")
    Call<String> checkCurrentBuildVersionLMS(@Path("subdomain") String subdomain,@Path("version") String version,@Path("OS") String os);

    /*HiDoctor LMS service*/
    /*@GET("UserApi/CheckCurrentBuildVersionHidoctorLMS/{subdomain}/{version}/{OS}")
    Call<String> checkCurrentBuildVersionLMS(@Path("subdomain") String subdomain,@Path("version") String version,@Path("OS") String os);*/

    @POST("Home/CheckEmailExists/")
    Call<String> CheckEmailExists(@Body UpdatePasswordModel password);

    /*Profile apis*/
    @GET("UserApi/getUserImportantDate/{subdomain}/{userId}/{companyId}")
    Call<List<UserDetails>> getUserImportantDate(@Path("userId") int userId , @Path("subdomain") String subdomain , @Path("companyId") int companyId);

    @GET("UserApi/getUserEducationDetails/{subdomain}/{userId}/{companyId}")
    Call<List<UserDetails>> getUserEducationDetails(@Path("userId") int userId , @Path("subdomain") String subdomain , @Path("companyId") int companyId);

    @GET("UserApi/getUserWorkDetails/{subdomain}/{userId}/{companyId}")
    Call<List<UserDetails>> getUserWorkDetails(@Path("userId") int userId , @Path("subdomain") String subdomain , @Path("companyId") int companyId);

    @GET("UserApi/getUserInterestDetails/{subdomain}/{userId}/{companyId}")
    Call<List<UserDetails>> getUserInterestDetails(@Path("userId") int userId , @Path("subdomain") String subdomain , @Path("companyId") int companyId);

    @GET("UserApi/getUserLifeEvent/{subdomain}/{userId}/{companyId}")
    Call<List<UserDetails>> getUserLifeEvent(@Path("userId") int userId , @Path("subdomain") String subdomain , @Path("companyId") int companyId);


    //Get List of items
    @GET("UserApi/getAllEvent/{subdomain}")
    Call<List<UserDetails>> getAllEvent(@Path("subdomain") String subdomain);

    @GET("/UserApi/GetAllWorkName/{subdomain}")
    Call<List<UserDetails>> getAllWorkName(@Path("subdomain") String subdomain);

    @GET("/UserApi/getAllEducation/{subdomain}")
    Call<EducationAndInstitution> getAllEducation(@Path("subdomain") String subdomain);

    @GET("/UserApi/getAllInterests/{subdomain}")
    Call<List<UserDetails>> getAllInterests(@Path("subdomain") String subdomain);


    //Delete details
    @POST("/UserApi/deleteUserEvent/{subdomain}/{userId}/{eventId}")
    Call<Integer> deleteUserEvent(@Path("subdomain") String subdomain, @Path("userId") int userId,@Path("eventId") int eventId);

    @POST("/UserApi/deleteUserInterest/{subdomain}/{userId}/{userinterestId}")
    Call<Integer> deleteUserInterest(@Path("subdomain") String subdomain, @Path("userId") int userId,@Path("userinterestId") int userinterestId);

    @POST("/UserApi/deleteUserEducation/{subdomain}/{userId}/{usereducationId}")
    Call<Integer> deleteUserEducation(@Path("subdomain") String subdomain, @Path("userId") int userId,@Path("usereducationId") int usereducationId);

    @POST("/UserApi/deleteUserWork/{subdomain}/{userId}/{userworkid}")
    Call <Integer> deleteUserWork(@Path("subdomain") String subdomain, @Path("userId") int userId,@Path("userworkid") int userworkid);


    //Save Details

    @POST("/UserApi/saveUserEventDetails/{subdomain}/{userId}/{companyId}")
    Call<Integer> saveUserEventDetails(@Path("subdomain") String subdomain, @Path("companyId") int companyId, @Body UserDetails userDetails);

    @POST("/UserApi/saveUserInterests/{subdomain}/{userId}/{companyId}")
    Call<Integer> saveUserInterests(@Path("subdomain") String subdomain, @Path("companyId") int companyId, @Body UserDetails userDetails);

    @POST("/UserApi/saveUserEducationDetails/{subdomain}/{userId}/{companyId}")
    Call<Integer> saveUserEducationDetails(@Path("subdomain") String subdomain, @Path("companyId") int companyId, @Body UserDetails userDetails);

    @POST("/UserApi/saveUserWorkDetails/{subdomain}/{userId}/{companyId}")
    Call<Integer> saveUserWorkDetails(@Path("subdomain") String subdomain, @Path("companyId") int companyId, @Body UserDetails userDetails);


    @GET("LPCourseDashboardAPI/GetLPCourseDashboradDetails/{subdomain}/{compayId}/{userId}/{utcoffset}")
    Call<List<DashBoardDetailsModel>> getDashboardDetails(@Path("subdomain") String subdomain,
                                                          @Path("compayId") int compayId,
                                                          @Path("userId") int userId,
                                                          @Path("utcoffset") String utcoffset);

    @GET("LPCourseDashboardAPI/GetLPCourseDashboradDetails_Mobile/{subdomain}/{compayId}/{userId}/{utcoffset}")
    Call<List<ReportModel>> getDashboardDetailsReport(@Path("subdomain") String subdomain,
                                                      @Path("compayId") int compayId,
                                                      @Path("userId") int userId,
                                                      @Path("utcoffset") String utcoffset);

    @GET("/DashboardApi/testdropdown/{compayId}/{UserName}")
    Call<List<UserRoleModel>> getUserRefreshDetails(@Path("compayId") int compayId,
                                                    @Path("UserName") String UserName);

    @GET("/ThemeEngineApi/GetCompanyTheme/{subdomain}/{compayId}")
    Call<List<ThemeModel>> getTheme(@Path("subdomain") String subdomain,
                                                 @Path("compayId") int compayId);
}

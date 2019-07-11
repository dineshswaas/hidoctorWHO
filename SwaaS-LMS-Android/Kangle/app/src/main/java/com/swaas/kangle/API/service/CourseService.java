package com.swaas.kangle.API.service;


import com.swaas.kangle.models.DashBoardDetailsModel;
import com.swaas.kangle.models.TopfiveDashBoardAssetDetailModel;
import com.swaas.kangle.report.modle.MyCourseReportModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Sayellessh on 12-06-2017.
 */

public interface CourseService {

    @GET("LPCourseDashboardAPI/GetLpCourseDashboardTopFiveCompletedCourses/{subdomain}/{compayId}/{userId}/{utcoffset}")
    Call<List<TopfiveDashBoardAssetDetailModel>> getTopFiveCompletedCourses(@Path("subdomain") String subdomain,
                                                                            @Path("compayId") int compayId,
                                                                            @Path("userId") int userId,
                                                                            @Path("utcoffset") String utcoffset);

    @GET("LPCourseDashboardAPI/GetLpCourseDashboardDetails/{subdomain}/{compayId}/{userId}/{utcoffset}")
    Call<List<TopfiveDashBoardAssetDetailModel>> getTopFiveLatestCourses(@Path("subdomain") String subdomain,
                                                                            @Path("compayId") int compayId,
                                                                            @Path("userId") int userId,
                                                                            @Path("utcoffset") String utcoffset);

    @GET("LPCourseDashboardAPI/GetLPCourseDashboradDetails/{subdomain}/{compayId}/{userId}/{utcoffset}")
    Call<List<DashBoardDetailsModel>> getDashboardDetails(@Path("subdomain") String subdomain,
                                                          @Path("compayId") int compayId,
                                                          @Path("userId") int userId,
                                                          @Path("utcoffset") String utcoffset);


    @GET("LpCourseReportApi/GetUserCourseReportJson/{subdomain}/{compayId}/{userId}/{utcoffset}/{isTeamOrSelf}/{courseStatus}")
    Call<List<MyCourseReportModel>> getMyCourseReportList(@Path("subdomain") String subdomain,
                                                          @Path("compayId") int compayId,
                                                          @Path("userId") int userId,
                                                          @Path("utcoffset") String utcoffset,
                                                          @Path("isTeamOrSelf")int isTeamOrSelf,
                                                          @Path("courseStatus")String courseStatus);


    @GET("LpCourseReportApi/GetUserCommonReportJson/{subdomain}/{compayId}/{userId}/{utcoffset}/{isTeamOrSelf}/{courseStatus}/{moduletype}")
    Call<List<MyCourseReportModel>> getMyCommonReportList(@Path("subdomain") String subdomain,
                                                          @Path("compayId") int compayId,
                                                          @Path("userId") int userId,
                                                          @Path("utcoffset") String utcoffset,
                                                          @Path("isTeamOrSelf")int isTeamOrSelf,
                                                          @Path("courseStatus")String courseStatus,
                                                          @Path("moduletype")int moduletype);

}

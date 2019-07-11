package com.swaas.kangle.LPCourse;

import com.swaas.kangle.CheckList.model.UserforCourseChecklist;
import com.swaas.kangle.LPCourse.model.AnwerUploadModel;
import com.swaas.kangle.LPCourse.model.LPCourseReportModel;
import com.swaas.kangle.LPCourse.model.LPCourseReportSummaryModel;
import com.swaas.kangle.LPCourse.model.QuestionBaseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Sayellessh on 10-08-2017.
 */

public interface LPCourseService {

    @GET("LPCourseAPI/GetAvailableCourses/{subdomain}/{companyId}/{userId}/{utcoffset}")
    Call<List<CourseModel>> getAvailableCourses(@Path("subdomain") String subdomainname , @Path("companyId") int companyId,
                                                @Path("userId") int userId, @Path("utcoffset") String utcoffset);

    @GET("LPCourseAPI/GetAvailableKACourses/{subdomain}/{companyId}/{userId}/{utcoffset}")
    Call<List<CourseModel>> getAvailableKACourses(@Path("subdomain") String subdomainname , @Path("companyId") int companyId,
                                                @Path("userId") int userId, @Path("utcoffset") String utcoffset);



    @GET("LPCourseAPI/GetSectionDetailsOfCourse/{subdomain}/{companyId}/{userId}/{courseId}/{PublishId}")
    Call<List<SectionModel>> getSectionDetailsOfCourse(@Path("subdomain") String subdomainname , @Path("companyId") int companyId,
                                                       @Path("userId") int userId, @Path("courseId") int courseId,
                                                       @Path("PublishId") int PublishId);

    @GET("LPCourseAPI/GetKASectionDetailsOfCourse/{subdomain}/{companyId}/{userId}/{courseId}/{PublishId}")
    Call<List<SectionModel>> getKASectionDetailsOfCourse(@Path("subdomain") String subdomainname , @Path("companyId") int companyId,
                                                       @Path("userId") int userId, @Path("courseId") int courseId,
                                                       @Path("PublishId") int PublishId);

    @GET("LPCourseAPI/GetLPAssetsByCourseId/{subdomain}/{companyId}/{courseId}/{sectionId}")
    Call<List<CourseAssetModel>> getLPAssetsByCourseId(@Path("subdomain") String subdomainname , @Path("companyId") int companyId,
                                                       @Path("courseId") int courseId, @Path("sectionId") int sectionId);

    @POST("LPCourseAPI/InsertLPCourseViewAnalytics/{subdomain}")
    Call<String> insertLPCourseViewAnalytics(@Path("subdomain") String subdomainname ,
                                                             @Body CourseAnalyticsModel course);


    @POST("LPCourseAPI/InsertAssetMappingMaterialLog/{subdomain}")
    Call<String> insertAssetMappingMaterialLog(@Path("subdomain") String subdomainname ,
                                                               @Body CourseAnalyticsModel course);


    @POST("LPCourseAPI/insertLPCourseSectionUserExamHeader/{subdomainName}/{courseId}/{courseUserAssignMentId}/" +
            "{couseUserSectionId}/{publishId}/{userId}/{companyId}/{sectionId}")
    Call<String> insertLPCourseSectionUserExamHeader(@Path("subdomainName") String subdomainname ,
                                                     @Path("courseId") int courseId,
                                                     @Path("courseUserAssignMentId") int courseUserAssignMentId,
                                                     @Path("couseUserSectionId") int couseUserSectionId,
                                                     @Path("publishId") int PublishId,
                                                     @Path("userId") int userId,
                                                     @Path("companyId") int companyId,
                                                     @Path("sectionId") int sectionId);



    @GET("LPCourseAPI/getLPQuestionAnswerDetails/{subdomain}/{companyId}/{userId}/{courseId}/{sectionId}/{PublishId}")
    Call<List<QuestionBaseModel>> getLPQuestionAnswerDetails(@Path("subdomain") String subdomainname , @Path("companyId") int companyId,
                                                             @Path("userId") int userId, @Path("courseId") int courseId, @Path("sectionId") int sectionId,
                                                             @Path("PublishId") int PublishId);


    @POST("LPCourseAPI/insertLPCourseResponse/{subdomainName}/{companyId}/{userId}/{questionLoadCount}/{isLastQuestion}/{isTimeOut}")
    Call<String> insertTestCourseResponse(@Path("subdomainName") String subdomainname , @Path("companyId") int companyId,
                                          @Path("userId") int userId,@Path("questionLoadCount") int questionLoadCount,@Path("isLastQuestion") boolean isLastQuestion,@Path("isTimeOut") boolean isTimeOut, @Body AnwerUploadModel answermodel);




    @GET("LPCourseAPI/getLPSectionAttemptDetails/{subdomain}/{companyId}/{courseId}/{userId}/{PublishId}/{sectionId}/{utcoffset}")
    Call<ArrayList<LPCourseReportModel>> getLPSectionAttemptDetails(@Path("subdomain") String subdomainname,
                                                                    @Path("companyId") int companyId,
                                                                    @Path("courseId") int courseId,
                                                                    @Path("userId") int userId,
                                                                    @Path("PublishId") int PublishId,
                                                                    @Path("sectionId") int sectionId,
                                                                    @Path("utcoffset") String utcoffset);

    @GET("LPCourseAPI/GetLPSectionQuestionDetails/{subdomainName}/{courseSectionUserExamId}/{utcoffset}/{companyId}")
    Call<ArrayList<LPCourseReportSummaryModel>> getLPSectionQuestionDetails(@Path("subdomainName") String subdomainname,
                                                                            @Path("courseSectionUserExamId") int examId,
                                                                            @Path("utcoffset") String utcoffset,
                                                                            @Path("companyId") int companyId);


    //Section page api for auto extend
    @POST("LPCourseAPI/LPCourseAutoExtentAttemptsForUsers")
    Call<String> LPCourseAutoExtentAttemptsForUsers(@Body CourseExtendModel courseExtendModel);

    //Course page api for auto extend
    @POST("LPCourseAPI/LPCourseAutoExtentDateForUsers")
    Call<String> LPCourseAutoExtentDateForUsers(@Body CourseExtendModel courseExtendModel);

    //Course page api for checklist restart update
    @POST("LPCourseAPI/CourseCheckListRestartUpdate/{companyId}/{courseId}/{userId}/{subdomainName}")
    Call<String> CourseCheckListRestartUpdate(@Path("companyId") int companyId,
                                              @Path("courseId") int courseId,
                                              @Path("userId") int userId,
                                              @Path("subdomainName") String subdomainname);

    //Course page api for SectionChecklistReport
    @GET("LPCourseAPI/GetSectionChecklistReportList/{subdomainName}/{companyId}/{userId}/{courseId}/{Section_Id}")
    Call<ArrayList<UserforCourseChecklist>> getSectionChecklistReportList(@Path("subdomainName") String subdomainname,
                                                                          @Path("companyId") int companyId,
                                                                          @Path("userId") int courseId,
                                                                          @Path("courseId") int userId,
                                                                          @Path("Section_Id") int PublishId);

    @GET("LPCourseAPI/GetCourseChecklistReportList/{subdomainName}/{companyId}/{userId}/{courseId}/{Section_Id}")
    Call<ArrayList<UserforCourseChecklist>> getCourseChecklistReportList(@Path("subdomainName") String subdomainname,
                                                                         @Path("companyId") int companyId,
                                                                         @Path("userId") int courseId,
                                                                         @Path("courseId") int userId,
                                                                         @Path("Section_Id") int PublishId);
    @GET("LPCourseAPI/TestEvaluation/{courseId}/{userId}/{companyId}")
    Call<ArrayList<LPCourseReportSummaryModel>> getCourseFullcourseReportList(@Path("courseId") int userId,
                                                                              @Path("userId") int courseId,
                                                                              @Path("companyId") int companyId);

}

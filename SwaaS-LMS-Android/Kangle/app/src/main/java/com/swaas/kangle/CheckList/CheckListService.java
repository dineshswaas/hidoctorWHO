package com.swaas.kangle.CheckList;

import com.squareup.okhttp.RequestBody;
import com.swaas.kangle.CheckList.model.AllSectionsQADetailModel;
import com.swaas.kangle.CheckList.model.AnwerUploadModel;
import com.swaas.kangle.CheckList.model.CheckListModel;
import com.swaas.kangle.CheckList.model.ChecklistFileUploadResult;
import com.swaas.kangle.CheckList.model.QuestionBaseModel;
import com.swaas.kangle.CheckList.model.UserCCHistoryDetailModel;
import com.swaas.kangle.CheckList.model.UserforCourseChecklist;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;

/**
 * Created by Sayellessh on 23-04-2018.
 */

public interface CheckListService {

    //list of checklist api
    @GET("ChecklistAPI/GetAvailableCheckList/{subdomain}/{companyId}/{userId}/{utcoffset}")
    Call<List<CheckListModel>> getAvailableChecklists(@Path("subdomain") String subdomainname ,
                                                      @Path("companyId") int companyId,
                                                      @Path("userId") int userId,
                                                      @Path("utcoffset") String utcoffset);

    //header list insert for each section in checklist
    @POST("ChecklistAPI/insertLPChecklistSectionUserExamHeader/{subdomainName}/{ChecklistId}/{ChecklistUserAssignMentId}/{ChecklistUserSectionId}/{publishId}/{userId}/{companyId}/{sectionId}")
    Call<Integer> insertLPChecklistSectionUserExamHeader(@Path("subdomainName") String subdomainName ,
                                                         @Path("ChecklistId") int ChecklistId,
                                                         @Path("ChecklistUserAssignMentId") int ChecklistUserAssignMentId,
                                                         @Path("ChecklistUserSectionId") int ChecklistUserSectionId,
                                                         @Path("publishId") int publishId,
                                                         @Path("userId") int userId,
                                                         @Path("companyId") int companyId,
                                                         @Path("sectionId") int sectionId);

    //get list of sections and questions of the sections
    @GET("ChecklistAPI/GetChecklistQuestionAnswerDetailsAPI/{subdomain}/{companyId}/{userId}/{checklistId}/{CUAId}/{DraftId}")
    Call<ArrayList<QuestionBaseModel>> getChecklistQuestionAnswerDetailsAPI(@Path("subdomain") String subdomainname ,
                                                               @Path("companyId") int companyId,
                                                               @Path("userId") int userId,
                                                               @Path("checklistId") int checklistId,
                                                               @Path("CUAId") int CUAId,
                                                               @Path("DraftId") String DraftId);

    //upload attachment for questions
    @Multipart
    @POST("ChecklistAPI/uploadFile/{subdomainName}/{companyId}/{userId}/{ChecklistId}/{SectionId}/{questionId}")
    Call<ChecklistFileUploadResult> uploadFile(@Path("subdomainName") String subdomainName,
                                               @Path("companyId") int companyId,
                                               @Path("userId") int userId,
                                               @Path("ChecklistId") int ChecklistId,
                                               @Path("SectionId") int SectionId,
                                               @Path("questionId") int questionId,
                                               @Part("ChecklistAttachmentFile\"; filename=\"image\" ") RequestBody file,
                                               @Part("localFileName") RequestBody localFileName);
    //@Part("localFileName") RequestBody fileName

    //save question answers for checklist
    @POST("ChecklistAPI/SaveQuestion/{subdomainName}/{companyId}/{userId}/{questionLoadCount}/{isLastQuestion}/{isTimeOut}")
    Call<String> InsertChecklistResponse(@Path("subdomainName") String subdomainname ,
                                         @Path("companyId") int companyId,
                                         @Path("userId") int userId,
                                         @Path("questionLoadCount") int questionLoadCount,
                                         @Path("isLastQuestion") boolean isLastQuestion,
                                         @Path("isTimeOut") boolean isTimeOut,
                                         @Body AnwerUploadModel answermodel);

    //Report checklist
    @GET("ChecklistAPI/GetSectionQuetsionsAnswer/{subdomainName}/{Checklist_Id}/{utcOffset}/{companyId}/{User_Id}/{cumid}/{group_id}")
    Call<List<AllSectionsQADetailModel>> getSectionQuetsionsAnswer(@Path("subdomainName") String subdomainName ,
                                                                   @Path("Checklist_Id") int Checklist_Id,
                                                                   @Path("utcOffset") String utcOffset,
                                                                   @Path("companyId") int companyId,
                                                                   @Path("User_Id") int User_Id,
                                                                   @Path("cumid") int cumid,
                                                                   @Path("group_id") int group_id);

    //Course Checklist User list
    @GET("ChecklistAPI/GetUserforCourseChecklist/{subdomainName}/{Checklist_Id}/{utcOffset}/{companyId}/{User_Id}/{Course_Id}")
    Call<List<UserforCourseChecklist>> getUserforCourseChecklist(@Path("subdomainName") String subdomainName ,
                                                                 @Path("Checklist_Id") int Checklist_Id,
                                                                 @Path("utcOffset") String utcOffset,
                                                                 @Path("companyId") int companyId,
                                                                 @Path("User_Id") int User_Id,
                                                                 @Path("Course_Id") int Course_Id);

    @GET("/ChecklistAPI/GetUserforDraftCourseChecklist/{subdomainName}/{Checklist_Id}/{utcOffset}/{companyId}/{User_Id}/{Course_Id}")
    Call<List<UserforCourseChecklist>> getUserforDraftCourseChecklist(@Path("subdomainName") String subdomainName ,
                                                                      @Path("Checklist_Id") int Checklist_Id,
                                                                      @Path("utcOffset") String utcOffset,
                                                                      @Path("companyId") int companyId,
                                                                      @Path("User_Id") int User_Id,
                                                                      @Path("Course_Id") int Course_Id);

    @GET("ChecklistAPI/GetDraftCourseCheckListUser/{guid}/{subdomainName}/{Company_Id}")
    Call<List<UserforCourseChecklist>> getDraftCourseCheckListUser(@Path("guid") String guid ,
                                                                   @Path("subdomainName") String subdomainName,
                                                                   @Path("Company_Id") int companyId);


    //CourseChecklist User Report
    @GET("/ChecklistAPI/GetCourseChecklistReportAPI/{subdomainName}/{Checklist_Id}/{utcOffset}/{companyId}/{User_Id}/{course_Id}/{Attempt_count}/{section_Id}")
    Call<List<AllSectionsQADetailModel>> getUserwiseCCSectionQuetsionsAnswer(@Path("subdomainName") String subdomainName ,
                                                                             @Path("Checklist_Id") int Checklist_Id,
                                                                             @Path("utcOffset") String utcOffset,
                                                                             @Path("companyId") int companyId,
                                                                             @Path("User_Id") int User_Id,
                                                                             @Path("course_Id") int course_Id,
                                                                             @Path("Attempt_count") int Attempt_count,
                                                                             @Path("section_Id") int section_Id);

    @GET("/ChecklistAPI/GetCourseChecklistAttemptsAPI/{subdomainName}/{Checklist_Id}/{utcOffset}/{companyId}/{User_Id}/{course_Id}/{section_Id}")
    Call<List<UserCCHistoryDetailModel>> getCourseChecklistAttemptsAPI(@Path("subdomainName") String subdomainName ,
                                                                       @Path("Checklist_Id") int Checklist_Id,
                                                                       @Path("utcOffset") String utcOffset,
                                                                       @Path("companyId") int companyId,
                                                                       @Path("User_Id") int User_Id,
                                                                       @Path("course_Id") int course_Id,
                                                                       @Path("section_Id") int section_Id);
}

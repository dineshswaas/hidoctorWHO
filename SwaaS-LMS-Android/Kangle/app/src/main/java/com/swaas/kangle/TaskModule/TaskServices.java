package com.swaas.kangle.TaskModule;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Sayellessh on 22-11-2018.
 */

public interface TaskServices {

    //My Task List Api
    @GET("ChecklistAPI/getTaskListDataApi/{subdomain}/{companyId}/{userId}")
    Call<ArrayList<TaskListModel>> getTaskListDataApi(@Path("subdomain") String subdomainname ,
                                                      @Path("companyId") int companyId,
                                                      @Path("userId") int userId);

    //Reporting member list of the User logged in
    @GET("ChecklistAPI/getTaskAssignListApi/{subdomain}/{companyId}/{user_code}")
    Call<ArrayList<TaskListModel>> getTaskAssignListApi(@Path("subdomain") String subdomainname ,
                                                 @Path("companyId") int companyId,
                                                 @Path("user_code") String usercode);

    //My Team Task List Api
    @GET("ChecklistAPI/getTaskListDataTeamApi/{subdomain}/{companyId}/{userId}/{user_code}")
    Call<ArrayList<TaskGroupListModel>> getTaskListDataTeamApi(@Path("subdomain") String subdomainname ,
                                                   @Path("companyId") int companyId,
                                                   @Path("userId") int userId,
                                                   @Path("user_code") String usercode);

    //Update Task Status
    @POST("ChecklistAPI/fnChangeStatusTaskApi/{subdomain}/{companyId}/{userId}/{Task_Id}")
    Call<String> fnChangeStatusTaskApi(@Path("subdomain") String subdomainname ,
                                       @Path("companyId") int companyId,
                                       @Path("userId") int userId,
                                       @Path("Task_Id") int Task_Id,
                                       @Body TaskStatusModel response);

    // Show History in Task List
    @GET("ChecklistAPI/getTaskListStatusHistoryApi/{subdomain}/{companyId}/{Task_Id}")
    Call<ArrayList<TaskListModel>> getTaskListStatusHistoryApi(@Path("subdomain") String subdomainname ,
                                                               @Path("companyId") int companyId,
                                                               @Path("Task_Id") int Task_Id);

    // Show attachemnts in Task List page
    @GET("ChecklistAPI/getTaskAttachmentDataApi/{subdomain}/{companyId}/{Task_Id}")
    Call<ArrayList<TaskListModel>> getTaskAttachmentDataApi(@Path("subdomain") String subdomainname ,
                                                               @Path("companyId") int companyId,
                                                               @Path("Task_Id") int Task_Id);

    //Create Checklist Allowed or not check
    @GET("ChecklistAPI/GetChildUserdetailsTaskApi/{subdomain}/{companyId}/{user_code}")
    Call<ArrayList<TaskListModel>> getChildUserdetailsTaskApi(@Path("subdomain") String subdomainname ,
                                                               @Path("companyId") int companyId,
                                                               @Path("user_code") String usercode);

    //Auto Complete
    @GET("ChecklistAPI/fngetAdminlevelUserlistApi/{subdomain}/{companyId}/{user_code}")
    Call<ArrayList<TaskListModel>> fngetAdminlevelUserlistApi(@Path("subdomain") String subdomainname ,
                                                               @Path("companyId") int companyId,
                                                               @Path("user_code") String usercode);

    @GET("ChecklistAPI/getTaskAssignListApi/{subdomain}/{companyId}/{user_code}")
    Call<ArrayList<TaskListModel>> getChildUserlist(@Path("subdomain") String subdomainname ,
                                                    @Path("companyId") int companyId,
                                                    @Path("user_code") String usercode);
    @POST("ChecklistAPI/InsertTaskPageApi/{subdomain}/{companyId}/{User_Id}")
    Call<Boolean> getInsertTaskApi(@Path("subdomain") String subdomainname ,
                                   @Path("companyId") int companyId,
                                   @Path("User_Id") int userID,
                                   @Body ArrayList<TaskListModel> taskListModel);


    @GET("/ChecklistAPI/getTaskSaveDraftNormalChecklistApi/{subdomainName}/{companyId}/{Checklist_Id}/{Question_Id}/{User_Id}/{GroupChecklistCheck}/{Course_SectionId}/{Checklist_GroupId}")
    Call<List<TaskListModel>> getTaskSaveDraftNormalChecklistApi(@Path("subdomainName") String subdomainName ,
                                                                 @Path("companyId") int companyId,
                                                                 @Path("Checklist_Id") int Checklist_Id,
                                                                 @Path("Question_Id") int Question_Id,
                                                                 @Path("User_Id") int User_Id,
                                                                 @Path("GroupChecklistCheck") int GroupChecklistCheck,
                                                                 @Path("Course_SectionId") int Course_SectionId,
                                                                 @Path("Checklist_GroupId") int Checklist_GroupId);

    @GET("/ChecklistAPI/getTaskSaveDraftCourseChecklistApi/{subdomainName}/{companyId}/{Checklist_Id}/{Question_Id}/{User_Id}/{GroupChecklistCheck}/{Course_SectionId}/" +
            "{Course_Id}/{Course_Draft_Id}/{Selected_CourseId}/{Selected_UserId}")
    Call<List<TaskListModel>> getTaskSaveDraftCourseChecklistApi(@Path("subdomainName") String subdomainName ,
                                                                 @Path("companyId") int companyId,
                                                                 @Path("Checklist_Id") int Checklist_Id,
                                                                 @Path("Question_Id") int Question_Id,
                                                                 @Path("User_Id") int User_Id,
                                                                 @Path("GroupChecklistCheck") int GroupChecklistCheck,
                                                                 @Path("Course_SectionId") int Course_SectionId,
                                                                 @Path("Course_Id") int Course_Id,
                                                                 @Path("Course_Draft_Id") String Course_Draft_Id,
                                                                 @Path("Selected_CourseId") int Selected_CourseId,
                                                                 @Path("Selected_UserId") int Selected_UserId);


    @GET("ChecklistAPI/getTaskListStatusDataApi/{subdomain}/{companyId}/{task_id}")
    Call<ArrayList<TaskListModel>> getTaskListDetailsApi(@Path("subdomain") String subdomainname ,
                                                      @Path("companyId") int companyId,
                                                      @Path("task_id") int taskId);


}

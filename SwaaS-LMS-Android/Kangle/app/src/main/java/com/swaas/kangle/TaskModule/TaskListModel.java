package com.swaas.kangle.TaskModule;

import com.swaas.kangle.CheckList.model.Acknowledgement_urls;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 13-11-2018.
 */

public class TaskListModel implements Serializable {

    public String Task_Name;
    public String Task_Description;
    public int Task_Category_ID;
    public String Task_Due_Date;
    public String Task_Closed_date;
    public int Task_Priority;
    public String Task_Remarks;
    public int Task_Assigned_User_ID;
    public int Task_Status;
    public int Created_By;
    public String Created_DateTime;
    public int Updated_By;
    public String Updated_DateTime;
    public int IsActive;
    public int Company_Id;
    public String Notify_Intimation_type;
    public int Is_Draft_Task;
    public String Notification_created_datetime;
    public String Email_Intimation_type;
    public String Email_Created_datetime;
    public String Attachment_URL;
    public String Attachment_created_datetime;
    public int Response_User_ID;
    public String Response_text;
    public int Status_while_responded;
    public String Response_created_datetime;
    public String Priority;
    public String Assigned_By;
    public String TaskStatus;
    public int Task_Id;
    public String User_Name;
    public String User_Type_Name;
    public int User_Id;
    public String User_Code;
    public String Assigned_User_ID;
    public int duedays;
    public String Assigned_To;
    public int Source_Qustionid;
    public String Question_Text;
    public String Checklist_Name;
    public String Action;
    public String Intent;
    public String Intent_Type;
    public int Is_Task_Enabled;
    public String Assigned_User_Name;
    public String Notify_Self;
    public String Notify_Emp;
    public String Notify_Emp_Manager;
    public int Task_Assigned_Type;
    public String Over_due;
    public int GroupChecklistCheck;
    public int Course_Id;
    public String Under_User_Code;
    public int Course_Section_Id;
    public int Checklist_Id;
    public int Checklist_Group_Id;
    public String FormatedDueDate;

    public List<Acknowledgement_urls> Task_Attach_urls;
    public boolean userchecked;

    public List<Acknowledgement_urls> getAttachment_urls_list() {
        return Task_Attach_urls;
    }

    public void setAttachment_urls_list(List<Acknowledgement_urls> task_Attach_urls) {
        Task_Attach_urls = task_Attach_urls;
    }

    public boolean isUserchecked() {
        return userchecked;
    }

    public void setUserchecked(boolean userchecked) {
        this.userchecked = userchecked;
    }

    public String getTask_Name() {
        return Task_Name;
    }

    public void setTask_Name(String task_Name) {
        Task_Name = task_Name;
    }

    public String getTask_Description() {
        return Task_Description;
    }

    public void setTask_Description(String task_Description) {
        Task_Description = task_Description;
    }

    public int getTask_Category_ID() {
        return Task_Category_ID;
    }

    public void setTask_Category_ID(int task_Category_ID) {
        Task_Category_ID = task_Category_ID;
    }

    public String getTask_Due_Date() {
        return Task_Due_Date;
    }

    public void setTask_Due_Date(String task_Due_Date) {
        Task_Due_Date = task_Due_Date;
    }

    public String getTask_Closed_date() {
        return Task_Closed_date;
    }

    public void setTask_Closed_date(String task_Closed_date) {
        Task_Closed_date = task_Closed_date;
    }

    public int getTask_Priority() {
        return Task_Priority;
    }

    public void setTask_Priority(int task_Priority) {
        Task_Priority = task_Priority;
    }

    public String getTask_Remarks() {
        return Task_Remarks;
    }

    public void setTask_Remarks(String task_Remarks) {
        Task_Remarks = task_Remarks;
    }

    public int getTask_Assigned_User_ID() {
        return Task_Assigned_User_ID;
    }

    public void setTask_Assigned_User_ID(int task_Assigned_User_ID) {
        Task_Assigned_User_ID = task_Assigned_User_ID;
    }

    public int getTask_Status() {
        return Task_Status;
    }

    public void setTask_Status(int task_Status) {
        Task_Status = task_Status;
    }

    public int getCreated_By() {
        return Created_By;
    }

    public void setCreated_By(int created_By) {
        Created_By = created_By;
    }

    public String getCreated_DateTime() {
        return Created_DateTime;
    }

    public void setCreated_DateTime(String created_DateTime) {
        Created_DateTime = created_DateTime;
    }

    public int getUpdated_By() {
        return Updated_By;
    }

    public void setUpdated_By(int updated_By) {
        Updated_By = updated_By;
    }

    public String getUpdated_DateTime() {
        return Updated_DateTime;
    }

    public void setUpdated_DateTime(String updated_DateTime) {
        Updated_DateTime = updated_DateTime;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public String getNotify_Intimation_type() {
        return Notify_Intimation_type;
    }

    public void setNotify_Intimation_type(String notify_Intimation_type) {
        Notify_Intimation_type = notify_Intimation_type;
    }

    public int getIs_Draft_Task() {
        return Is_Draft_Task;
    }

    public void setIs_Draft_Task(int is_Draft_Task) {
        Is_Draft_Task = is_Draft_Task;
    }

    public String getNotification_created_datetime() {
        return Notification_created_datetime;
    }

    public void setNotification_created_datetime(String notification_created_datetime) {
        Notification_created_datetime = notification_created_datetime;
    }

    public String getEmail_Intimation_type() {
        return Email_Intimation_type;
    }

    public void setEmail_Intimation_type(String email_Intimation_type) {
        Email_Intimation_type = email_Intimation_type;
    }

    public String getEmail_Created_datetime() {
        return Email_Created_datetime;
    }

    public void setEmail_Created_datetime(String email_Created_datetime) {
        Email_Created_datetime = email_Created_datetime;
    }

    public String getAttachment_URL() {
        return Attachment_URL;
    }

    public void setAttachment_URL(String attachment_URL) {
        Attachment_URL = attachment_URL;
    }

    public String getAttachment_created_datetime() {
        return Attachment_created_datetime;
    }

    public void setAttachment_created_datetime(String attachment_created_datetime) {
        Attachment_created_datetime = attachment_created_datetime;
    }

    public int getResponse_User_ID() {
        return Response_User_ID;
    }

    public void setResponse_User_ID(int response_User_ID) {
        Response_User_ID = response_User_ID;
    }

    public String getResponse_text() {
        return Response_text;
    }

    public void setResponse_text(String response_text) {
        Response_text = response_text;
    }

    public int getStatus_while_responded() {
        return Status_while_responded;
    }

    public void setStatus_while_responded(int status_while_responded) {
        Status_while_responded = status_while_responded;
    }

    public String getResponse_created_datetime() {
        return Response_created_datetime;
    }

    public void setResponse_created_datetime(String response_created_datetime) {
        Response_created_datetime = response_created_datetime;
    }

    public String getPriority() {
        return Priority;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getAssigned_By() {
        return Assigned_By;
    }

    public void setAssigned_By(String assigned_By) {
        Assigned_By = assigned_By;
    }

    public String getTaskStatus() {
        return TaskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        TaskStatus = taskStatus;
    }

    public int getTask_Id() {
        return Task_Id;
    }

    public void setTask_Id(int task_Id) {
        Task_Id = task_Id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_Type_Name() {
        return User_Type_Name;
    }

    public void setUser_Type_Name(String user_Type_Name) {
        User_Type_Name = user_Type_Name;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public String getUser_Code() {
        return User_Code;
    }

    public void setUser_Code(String user_Code) {
        User_Code = user_Code;
    }

    public String getAssigned_User_ID() {
        return Assigned_User_ID;
    }

    public void setAssigned_User_ID(String assigned_User_ID) {
        Assigned_User_ID = assigned_User_ID;
    }

    public int getDuedays() {
        return duedays;
    }

    public void setDuedays(int duedays) {
        this.duedays = duedays;
    }

    public String getAssigned_To() {
        return Assigned_To;
    }

    public void setAssigned_To(String assigned_To) {
        Assigned_To = assigned_To;
    }

    public int getSource_Qustionid() {
        return Source_Qustionid;
    }

    public void setSource_Qustionid(int source_Qustionid) {
        Source_Qustionid = source_Qustionid;
    }

    public String getQuestion_Text() {
        return Question_Text;
    }

    public void setQuestion_Text(String question_Text) {
        Question_Text = question_Text;
    }

    public String getChecklist_Name() {
        return Checklist_Name;
    }

    public void setChecklist_Name(String checklist_Name) {
        Checklist_Name = checklist_Name;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public String getIntent() {
        return Intent;
    }

    public void setIntent(String intent) {
        Intent = intent;
    }

    public String getIntent_Type() {
        return Intent_Type;
    }

    public void setIntent_Type(String intent_Type) {
        Intent_Type = intent_Type;
    }

    public int getIs_Task_Enabled() {
        return Is_Task_Enabled;
    }

    public void setIs_Task_Enabled(int is_Task_Enabled) {
        Is_Task_Enabled = is_Task_Enabled;
    }

    public String getAssigned_User_Name() {
        return Assigned_User_Name;
    }

    public void setAssigned_User_Name(String assigned_User_Name) {
        Assigned_User_Name = assigned_User_Name;
    }

    public String getNotify_Self() {
        return Notify_Self;
    }

    public void setNotify_Self(String notify_Self) {
        Notify_Self = notify_Self;
    }

    public String getNotify_Emp() {
        return Notify_Emp;
    }

    public void setNotify_Emp(String notify_Emp) {
        Notify_Emp = notify_Emp;
    }

    public String getNotify_Emp_Manager() {
        return Notify_Emp_Manager;
    }

    public void setNotify_Emp_Manager(String notify_Emp_Manager) {
        Notify_Emp_Manager = notify_Emp_Manager;
    }

    public int getTask_Assigned_Type() {
        return Task_Assigned_Type;
    }

    public void setTask_Assigned_Type(int task_Assigned_Type) {
        Task_Assigned_Type = task_Assigned_Type;
    }

    public String getOver_due() {
        return Over_due;
    }

    public void setOver_due(String over_due) {
        Over_due = over_due;
    }

    public int getGroupChecklistCheck() {
        return GroupChecklistCheck;
    }

    public void setGroupChecklistCheck(int groupChecklistCheck) {
        GroupChecklistCheck = groupChecklistCheck;
    }

    public int getCourse_Id() {
        return Course_Id;
    }

    public void setCourse_Id(int course_Id) {
        Course_Id = course_Id;
    }

    public String getUnder_User_Code() {
        return Under_User_Code;
    }

    public void setUnder_User_Code(String under_User_Code) {
        Under_User_Code = under_User_Code;
    }

    public int getCourse_Section_Id() {
        return Course_Section_Id;
    }

    public void setCourse_Section_Id(int course_Section_Id) {
        Course_Section_Id = course_Section_Id;
    }

    public int getChecklist_Id() {
        return Checklist_Id;
    }

    public void setChecklist_Id(int checklist_Id) {
        Checklist_Id = checklist_Id;
    }

    public int getChecklist_Group_Id() {
        return Checklist_Group_Id;
    }

    public void setChecklist_Group_Id(int checklist_Group_Id) {
        Checklist_Group_Id = checklist_Group_Id;
    }

    public String getFormatedDueDate() {
        return FormatedDueDate;
    }

    public void setFormatedDueDate(String formatedDueDate) {
        FormatedDueDate = formatedDueDate;
    }
}

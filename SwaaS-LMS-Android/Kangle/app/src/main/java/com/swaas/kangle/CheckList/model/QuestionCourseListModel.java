package com.swaas.kangle.CheckList.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 01-05-2018.
 */

public class QuestionCourseListModel implements Serializable {


    private int Company_Id;
    private int Checklist_Id;
    private String Checklist_Name;
    private int Is_Prep_Test;
    private String Checklist_Type;
    private String Checklist_Reward_Mode;
    private String Checklist_Description;
    private String Checklist_Category;
    private String Checklist_Tags;
    private String Checklist_Instruction;
    private String Checklist_Image_URL;
    private int Checklist_Point;
    private boolean Active_Status;
    private String Effective_From;
    private String Effective_To;
    private int No_Of_Assets_Mapped;
    private int No_Of_Questions;
    private int Created_By;
    private String Created_Date;
    private int Updated_By;
    private String Updated_Date;
    private int Checklist_Response_Count;
    private int Is_Published;
    private int Publish_Count;
    private String Published_Date;
    private int Publish_ID;
    private String Category_Name;
    private int Pass_Percentage;
    private int Checklist_Attempt_Count;
    private int Checklist_Section_Count;
    private int Is_View;
    private String Created_Date_String;
    private String User_Name;
    private int Section_Id;
    private String Section_Name;
    private String Duration;

    private String Test_Description;
    private String Schedule_From;
    private String Schedule_To;
    private String Checklist_Status;
    private int No_Of_Questions_Mapped;
    private int ShowFullSummary;
    private int QuestionLoadCount;
    private int Checklist_User_Mapping_Id;
    private int Checklist_Section_User_Mapping_Id;

    public String overallAttachmentURL;

    public int User_Course_Checklist_Mapping_Id;

    public boolean Is_Task_Enabled;

    public boolean is_Task_Enabled() {
        return Is_Task_Enabled;
    }

    public void setIs_Task_Enabled(boolean is_Task_Enabled) {
        Is_Task_Enabled = is_Task_Enabled;
    }

    public int getUser_Course_Checklist_Mapping_Id() {
        return User_Course_Checklist_Mapping_Id;
    }

    public void setUser_Course_Checklist_Mapping_Id(int user_Course_Checklist_Mapping_Id) {
        User_Course_Checklist_Mapping_Id = user_Course_Checklist_Mapping_Id;
    }

    public boolean isActive_Status() {
        return Active_Status;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getCourse_Id() {
        return Checklist_Id;
    }

    public void setCourse_Id(int course_Id) {
        Checklist_Id = course_Id;
    }

    public String getCourse_Name() {
        return Checklist_Name;
    }

    public void setCourse_Name(String course_Name) {
        Checklist_Name = course_Name;
    }

    public int getIs_Prep_Test() {
        return Is_Prep_Test;
    }

    public void setIs_Prep_Test(int is_Prep_Test) {
        Is_Prep_Test = is_Prep_Test;
    }

    public String getCourse_Type() {
        return Checklist_Type;
    }

    public void setCourse_Type(String course_Type) {
        Checklist_Type = course_Type;
    }

    public String getCourse_Reward_Mode() {
        return Checklist_Reward_Mode;
    }

    public void setCourse_Reward_Mode(String course_Reward_Mode) {
        Checklist_Reward_Mode = course_Reward_Mode;
    }

    public String getCourse_Description() {
        return Checklist_Description;
    }

    public void setCourse_Description(String course_Description) {
        Checklist_Description = course_Description;
    }

    public String getCourse_Category() {
        return Checklist_Category;
    }

    public void setCourse_Category(String course_Category) {
        Checklist_Category = course_Category;
    }

    public String getCourse_Tags() {
        return Checklist_Tags;
    }

    public void setCourse_Tags(String course_Tags) {
        Checklist_Tags = course_Tags;
    }

    public String getCourse_Instruction() {
        return Checklist_Instruction;
    }

    public void setCourse_Instruction(String course_Instruction) {
        Checklist_Instruction = course_Instruction;
    }

    public String getCourse_Image_URL() {
        return Checklist_Image_URL;
    }

    public void setCourse_Image_URL(String course_Image_URL) {
        Checklist_Image_URL = course_Image_URL;
    }

    public int getCourse_Point() {
        return Checklist_Point;
    }

    public void setCourse_Point(int course_Point) {
        Checklist_Point = course_Point;
    }

    public boolean getActive_Status() {
        return Active_Status;
    }

    public void setActive_Status(boolean active_Status) {
        Active_Status = active_Status;
    }

    public String getEffective_From() {
        return Effective_From;
    }

    public void setEffective_From(String effective_From) {
        Effective_From = effective_From;
    }

    public String getEffective_To() {
        return Effective_To;
    }

    public void setEffective_To(String effective_To) {
        Effective_To = effective_To;
    }

    public int getNo_Of_Assets_Mapped() {
        return No_Of_Assets_Mapped;
    }

    public void setNo_Of_Assets_Mapped(int no_Of_Assets_Mapped) {
        No_Of_Assets_Mapped = no_Of_Assets_Mapped;
    }

    public int getNo_Of_Questions() {
        return No_Of_Questions;
    }

    public void setNo_Of_Questions(int no_Of_Questions) {
        No_Of_Questions = no_Of_Questions;
    }

    public int getCreated_By() {
        return Created_By;
    }

    public void setCreated_By(int created_By) {
        Created_By = created_By;
    }

    public String getCreated_Date() {
        return Created_Date;
    }

    public void setCreated_Date(String created_Date) {
        Created_Date = created_Date;
    }

    public int getUpdated_By() {
        return Updated_By;
    }

    public void setUpdated_By(int updated_By) {
        Updated_By = updated_By;
    }

    public String getUpdated_Date() {
        return Updated_Date;
    }

    public void setUpdated_Date(String updated_Date) {
        Updated_Date = updated_Date;
    }

    public int getCourse_Response_Count() {
        return Checklist_Response_Count;
    }

    public void setCourse_Response_Count(int course_Response_Count) {
        Checklist_Response_Count = course_Response_Count;
    }

    public int getIs_Published() {
        return Is_Published;
    }

    public void setIs_Published(int is_Published) {
        Is_Published = is_Published;
    }

    public int getPublish_Count() {
        return Publish_Count;
    }

    public void setPublish_Count(int publish_Count) {
        Publish_Count = publish_Count;
    }

    public String getPublished_Date() {
        return Published_Date;
    }

    public void setPublished_Date(String published_Date) {
        Published_Date = published_Date;
    }

    public int getPublish_ID() {
        return Publish_ID;
    }

    public void setPublish_ID(int publish_ID) {
        Publish_ID = publish_ID;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public int getPass_Percentage() {
        return Pass_Percentage;
    }

    public void setPass_Percentage(int pass_Percentage) {
        Pass_Percentage = pass_Percentage;
    }

    public int getCourse_Attempt_Count() {
        return Checklist_Attempt_Count;
    }

    public void setCourse_Attempt_Count(int course_Attempt_Count) {
        Checklist_Attempt_Count = course_Attempt_Count;
    }

    public int getCourse_Section_Count() {
        return Checklist_Section_Count;
    }

    public void setCourse_Section_Count(int course_Section_Count) {
        Checklist_Section_Count = course_Section_Count;
    }

    public int getIs_View() {
        return Is_View;
    }

    public void setIs_View(int is_View) {
        Is_View = is_View;
    }

    public String getCreated_Date_String() {
        return Created_Date_String;
    }

    public void setCreated_Date_String(String created_Date_String) {
        Created_Date_String = created_Date_String;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }

    public String getSection_Name() {
        return Section_Name;
    }

    public void setSection_Name(String section_Name) {
        Section_Name = section_Name;
    }

    public String getTest_Description() {
        return Test_Description;
    }

    public void setTest_Description(String test_Description) {
        Test_Description = test_Description;
    }

    public String getSchedule_From() {
        return Schedule_From;
    }

    public void setSchedule_From(String schedule_From) {
        Schedule_From = schedule_From;
    }

    public String getSchedule_To() {
        return Schedule_To;
    }

    public void setSchedule_To(String schedule_To) {
        Schedule_To = schedule_To;
    }

    public String getCourse_Status() {
        return Checklist_Status;
    }

    public void setCourse_Status(String course_Status) {
        Checklist_Status = course_Status;
    }

    public int getNo_Of_Questions_Mapped() {
        return No_Of_Questions_Mapped;
    }

    public void setNo_Of_Questions_Mapped(int no_Of_Questions_Mapped) {
        No_Of_Questions_Mapped = no_Of_Questions_Mapped;
    }

    public int getShowFullSummary() {
        return ShowFullSummary;
    }

    public void setShowFullSummary(int showFullSummary) {
        ShowFullSummary = showFullSummary;
    }

    public int getQuestionLoadCount() {
        return QuestionLoadCount;
    }

    public void setQuestionLoadCount(int questionLoadCount) {
        QuestionLoadCount = questionLoadCount;
    }

    public int getChecklist_User_Mapping_Id() {
        return Checklist_User_Mapping_Id;
    }

    public void setChecklist_User_Mapping_Id(int checklist_User_Mapping_Id) {
        Checklist_User_Mapping_Id = checklist_User_Mapping_Id;
    }

    public int getChecklist_Section_User_Mapping_Id() {
        return Checklist_Section_User_Mapping_Id;
    }

    public void setChecklist_Section_User_Mapping_Id(int checklist_Section_User_Mapping_Id) {
        Checklist_Section_User_Mapping_Id = checklist_Section_User_Mapping_Id;
    }

    public String getOverallAttachmentURL() {
        return overallAttachmentURL;
    }

    public void setOverallAttachmentURL(String overallAttachmentURL) {
        this.overallAttachmentURL = overallAttachmentURL;
    }
}

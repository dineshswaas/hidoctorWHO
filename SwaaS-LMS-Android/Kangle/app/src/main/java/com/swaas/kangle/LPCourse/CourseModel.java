package com.swaas.kangle.LPCourse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 10-08-2017.
 */

public class CourseModel implements Serializable {


    public int Course_Id;
    public String Course_Name;
    public String Course_Type;
    public String Course_Description;
    public String Course_Category_Id;
    public String Course_Tags;
    public String Course_Image_URL;
    public String Category_Name;
    public String Valid_From;
    public String Valid_To;
    public int Publish_Id;
    public boolean Course_Status;
    public boolean Section_Status;
    public int Course_Attempts_Count;
    public int Section_Attempts_Count;
    public String Course_Status_String;
    public int No_Of_Sections_Completed;
    public boolean Show_Print_Certificate;
    public int Total_Sections;
    public String Valid_From_String;
    public String Valid_To_String;
    public int Course_User_Assignment_Id;
    public int Company_Id;
    public boolean Is_Print_Certificate;
    public boolean Is_Prep_Test;
    public String Publish_Date;
    public String Status;
    public String Prerequisite;

    public int Section_Id;
    public String Section_Name;
    public float Achieved_Percentage;
    public int Course_Status_Value;
    public String Last_Test_Date_String;
    public String Last_Test_Date;

    public String Tags;

    public boolean iscatchecked;
    public boolean istagchecked;

    public int Evaluation_Type;
    public int Evaluation_Status;

    public int AutoExtendDays;
    public boolean isCourseExtend;
    public int ExtendDays;
    public int ExtendAttempts;
    public int ExtendLimits;

    public int No_of_Assets;
    public int No_of_Questions;

    public int No_Course_Checklist;
    public int No_Section_Checklist;

    public int Course_Evaluation_Status;

    public int Ref_Id;

    public int Course_Checklist_Count;
    public String Evaluation_Mode;
    public int Manual_Evaluation_Status;
    public String Minimum_Duration;
    public List<CourseSectionProgressModel> sectionDetails;


    public int getCourse_Id() {
        return Course_Id;
    }

    public void setCourse_Id(int course_Id) {
        Course_Id = course_Id;
    }

    public String getCourse_Name() {
        return Course_Name;
    }

    public void setCourse_Name(String course_Name) {
        Course_Name = course_Name;
    }

    public String getCourse_Type() {
        return Course_Type;
    }

    public void setCourse_Type(String course_Type) {
        Course_Type = course_Type;
    }

    public String getCourse_Description() {
        return Course_Description;
    }

    public void setCourse_Description(String course_Description) {
        Course_Description = course_Description;
    }

    public String getCourse_Category_Id() {
        return Course_Category_Id;
    }

    public void setCourse_Category_Id(String course_Category_Id) {
        Course_Category_Id = course_Category_Id;
    }

    public String getCourse_Tags() {
        return Course_Tags;
    }

    public void setCourse_Tags(String course_Tags) {
        Course_Tags = course_Tags;
    }

    public String getCourse_Image_URL() {
        return Course_Image_URL;
    }

    public void setCourse_Image_URL(String course_Image_URL) {
        Course_Image_URL = course_Image_URL;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public String getValid_From() {
        return Valid_From;
    }

    public void setValid_From(String valid_From) {
        Valid_From = valid_From;
    }

    public String getValid_To() {
        return Valid_To;
    }

    public void setValid_To(String valid_To) {
        Valid_To = valid_To;
    }

    public int getPublish_Id() {
        return Publish_Id;
    }

    public void setPublish_Id(int publish_Id) {
        Publish_Id = publish_Id;
    }

    public boolean isCourse_Status() {
        return Course_Status;
    }

    public void setCourse_Status(boolean course_Status) {
        Course_Status = course_Status;
    }

    public boolean isSection_Status() {
        return Section_Status;
    }

    public void setSection_Status(boolean section_Status) {
        Section_Status = section_Status;
    }

    public int getCourse_Attempts_Count() {
        return Course_Attempts_Count;
    }

    public void setCourse_Attempts_Count(int course_Attempts_Count) {
        Course_Attempts_Count = course_Attempts_Count;
    }

    public int getSection_Attempts_Count() {
        return Section_Attempts_Count;
    }

    public void setSection_Attempts_Count(int section_Attempts_Count) {
        Section_Attempts_Count = section_Attempts_Count;
    }

    public String getCourse_Status_String() {
        return Course_Status_String;
    }

    public void setCourse_Status_String(String course_Status_String) {
        Course_Status_String = course_Status_String;
    }

    public int getNo_Of_Sections_Completed() {
        return No_Of_Sections_Completed;
    }

    public void setNo_Of_Sections_Completed(int no_Of_Sections_Completed) {
        No_Of_Sections_Completed = no_Of_Sections_Completed;
    }

    public boolean isShow_Print_Certificate() {
        return Show_Print_Certificate;
    }

    public void setShow_Print_Certificate(boolean show_Print_Certificate) {
        Show_Print_Certificate = show_Print_Certificate;
    }

    public int getTotal_Sections() {
        return Total_Sections;
    }

    public void setTotal_Sections(int total_Sections) {
        Total_Sections = total_Sections;
    }

    public String getValid_From_String() {
        return Valid_From_String;
    }

    public void setValid_From_String(String valid_From_String) {
        Valid_From_String = valid_From_String;
    }

    public String getValid_To_String() {
        return Valid_To_String;
    }

    public void setValid_To_String(String valid_To_String) {
        Valid_To_String = valid_To_String;
    }

    public int getCourse_User_Assignment_Id() {
        return Course_User_Assignment_Id;
    }

    public void setCourse_User_Assignment_Id(int course_User_Assignment_Id) {
        Course_User_Assignment_Id = course_User_Assignment_Id;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public boolean is_Print_Certificate() {
        return Is_Print_Certificate;
    }

    public void setIs_Print_Certificate(boolean is_Print_Certificate) {
        Is_Print_Certificate = is_Print_Certificate;
    }

    public boolean is_Prep_Test() {
        return Is_Prep_Test;
    }

    public void setIs_Prep_Test(boolean is_Prep_Test) {
        Is_Prep_Test = is_Prep_Test;
    }

    public String getPublish_Date() {
        return Publish_Date;
    }

    public void setPublish_Date(String publish_Date) {
        Publish_Date = publish_Date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPrerequisite() {
        return Prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        Prerequisite = prerequisite;
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

    public float getAchieved_Percentage() {
        return Achieved_Percentage;
    }

    public void setAchieved_Percentage(float achieved_Percentage) {
        Achieved_Percentage = achieved_Percentage;
    }

    public int getCourse_Status_Value() {
        return Course_Status_Value;
    }

    public void setCourse_Status_Value(int course_Status_Value) {
        Course_Status_Value = course_Status_Value;
    }

    public String getLast_Test_Date_String() {
        return Last_Test_Date_String;
    }

    public void setLast_Test_Date_String(String last_Test_Date_String) {
        Last_Test_Date_String = last_Test_Date_String;
    }

    public String getLast_Test_Date() {
        return Last_Test_Date;
    }

    public void setLast_Test_Date(String last_Test_Date) {
        Last_Test_Date = last_Test_Date;
    }

    private String CourseTitle;
    private String CourseDesc;
    private String CourseStatus;
    private String DA_id;
    private boolean isCompleted;
    private int isCourseRestart;

    public String getCourseTitle() {
        return CourseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        CourseTitle = courseTitle;
    }

    public String getCourseDesc() {
        return CourseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        CourseDesc = courseDesc;
    }

    public String getCourseStatus() {
        return CourseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        CourseStatus = courseStatus;
    }

    public String getDA_id() {
        return DA_id;
    }

    public void setDA_id(String DA_id) {
        this.DA_id = DA_id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public boolean iscatchecked() {
        return iscatchecked;
    }

    public void setIscatchecked(boolean iscatchecked) {
        this.iscatchecked = iscatchecked;
    }

    public boolean istagchecked() {
        return istagchecked;
    }

    public void setIstagchecked(boolean istagchecked) {
        this.istagchecked = istagchecked;
    }

    public List<CourseSectionProgressModel> getSectionDetails() {
        return sectionDetails;
    }

    public void setSectionDetails(List<CourseSectionProgressModel> sectionDetails) {
        this.sectionDetails = sectionDetails;
    }

    public int getEvaluation_Type() {
        return Evaluation_Type;
    }

    public void setEvaluation_Type(int evaluation_Type) {
        Evaluation_Type = evaluation_Type;
    }

    public int getEvaluation_Status() {
        return Evaluation_Status;
    }

    public void setEvaluation_Status(int evaluation_Status) {
        Evaluation_Status = evaluation_Status;
    }

    public int getAutoExtendDays() {
        return AutoExtendDays;
    }

    public void setAutoExtendDays(int autoExtendDays) {
        AutoExtendDays = autoExtendDays;
    }

    public boolean isCourseExtend() {
        return isCourseExtend;
    }

    public void setCourseExtend(boolean courseExtend) {
        isCourseExtend = courseExtend;
    }

    public int getExtendDays() {
        return ExtendDays;
    }

    public void setExtendDays(int extendDays) {
        ExtendDays = extendDays;
    }

    public int getExtendAttempts() {
        return ExtendAttempts;
    }

    public void setExtendAttempts(int extendAttempts) {
        ExtendAttempts = extendAttempts;
    }

    public int getExtendLimits() {
        return ExtendLimits;
    }

    public void setExtendLimits(int extendLimits) {
        ExtendLimits = extendLimits;
    }

    public int getIsCourseRestart() {
        return isCourseRestart;
    }

    public void setIsCourseRestart(int isCourseRestart) {
        this.isCourseRestart = isCourseRestart;
    }

    public int getNo_of_Assets() {
        return No_of_Assets;
    }

    public void setNo_of_Assets(int no_of_Assets) {
        No_of_Assets = no_of_Assets;
    }

    public int getNo_of_Questions() {
        return No_of_Questions;
    }

    public void setNo_of_Questions(int no_of_Questions) {
        No_of_Questions = no_of_Questions;
    }

    public int getNo_Course_Checklist() {
        return No_Course_Checklist;
    }

    public void setNo_Course_Checklist(int no_Course_Checklist) {
        No_Course_Checklist = no_Course_Checklist;
    }

    public int getNo_Section_Checklist() {
        return No_Section_Checklist;
    }

    public void setNo_Section_Checklist(int no_Section_Checklist) {
        No_Section_Checklist = no_Section_Checklist;
    }

    public int getCourse_Evaluation_Status() {
        return Course_Evaluation_Status;
    }

    public void setCourse_Evaluation_Status(int course_Evaluation_Status) {
        Course_Evaluation_Status = course_Evaluation_Status;
    }

    public int getRef_Id() {
        return Ref_Id;
    }

    public void setRef_Id(int ref_Id) {
        Ref_Id = ref_Id;
    }

    public int getCourse_Checklist_Count() {
        return Course_Checklist_Count;
    }

    public void setCourse_Checklist_Count(int course_Checklist_Count) {
        Course_Checklist_Count = course_Checklist_Count;
    }

    public String getEvaluation_Mode() {
        return Evaluation_Mode;
    }

    public void setEvaluation_Mode(String evaluation_Mode) {
        Evaluation_Mode = evaluation_Mode;
    }

    public int getManual_Evaluation_Status() {
        return Manual_Evaluation_Status;
    }

    public void setManual_Evaluation_Status(int manual_Evaluation_Status) {
        Manual_Evaluation_Status = manual_Evaluation_Status;
    }

    public String getMinimum_Duration() {
        return Minimum_Duration;
    }

    public void setMinimum_Duration(String minimum_Duration) {
        Minimum_Duration = minimum_Duration;
    }
}

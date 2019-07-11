package com.swaas.kangle.LPCourse;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 10-08-2017.
 */

public class SectionModel implements Serializable {

    public int Course_Id;
    public String Course_Name;
    public int Publish_Id;
    public int Section_Id;
    public String Section_Name;
    public int No_Of_Assets_Mapped;
    public double Pass_Percentage;
    public double Achieved_Percentage;
    public boolean Course_Status;
    public boolean Section_Status;
    public int Course_Attempts_Count;
    public int Section_Attempts_Count;
    public int Course_User_Assignment_Id;
    public int Couse_User_Section_Mapping_Id;
    public String User_Name;
    public String Region_Code;
    public String Region_Name;
    public String Section_Status_String;
    public String Course_Status_String;
    public boolean Show_Print_Certificate;
    public boolean Is_Read_Assets;
    public String Valid_To;
    public int Section_Max_Attempts;
    public String Status;
    public boolean Is_Print_Certificate;
    public int ShowFullSummary;
    public int Course_Status_Value;
    public List<SectionAssetModel> lstMandoryAssets;
    public boolean IsMandatoryOpen;

    public int Section_Status_Value;
    public String Section_Status_Text;
    public String Course_Status_Text;
    public boolean Is_Qualified;

    public int Display_Order;
    public boolean Is_Course_Section_Mandatory;

    public int No_Of_Visible_Questions;

    public int Evaluation_Type;
    public int Evaluation_Status;
    public int Section_Ref_Id;

    public int Section_Checklist_Count;
    public String Evaluation_Mode;

    public int getSection_Checklist_Count() {
        return Section_Checklist_Count;
    }

    public void setSection_Checklist_Count(int section_Checklist_Count) {
        Section_Checklist_Count = section_Checklist_Count;
    }

    public int getSection_Ref_Id() {
        return Section_Ref_Id;
    }

    public void setSection_Ref_Id(int section_Ref_Id) {
        Section_Ref_Id = section_Ref_Id;
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

    public int getNo_Of_Visible_Questions() {
        return No_Of_Visible_Questions;
    }

    public void setNo_Of_Visible_Questions(int no_Of_Visible_Questions) {
        No_Of_Visible_Questions = no_Of_Visible_Questions;
    }

    public boolean isMandatoryOpen() {
        return IsMandatoryOpen;
    }

    public void setMandatoryOpen(boolean mandatoryOpen) {
        IsMandatoryOpen = mandatoryOpen;
    }

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

    public int getPublish_Id() {
        return Publish_Id;
    }

    public void setPublish_Id(int publish_Id) {
        Publish_Id = publish_Id;
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

    public int getNo_Of_Assets_Mapped() {
        return No_Of_Assets_Mapped;
    }

    public void setNo_Of_Assets_Mapped(int no_Of_Assets_Mapped) {
        No_Of_Assets_Mapped = no_Of_Assets_Mapped;
    }

    public double getPass_Percentage() {
        return Pass_Percentage;
    }

    public void setPass_Percentage(double pass_Percentage) {
        Pass_Percentage = pass_Percentage;
    }

    public double getAchieved_Percentage() {
        return Achieved_Percentage;
    }

    public void setAchieved_Percentage(double achieved_Percentage) {
        Achieved_Percentage = achieved_Percentage;
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

    public int getCourse_User_Assignment_Id() {
        return Course_User_Assignment_Id;
    }

    public void setCourse_User_Assignment_Id(int course_User_Assignment_Id) {
        Course_User_Assignment_Id = course_User_Assignment_Id;
    }

    public int getCouse_User_Section_Mapping_Id() {
        return Couse_User_Section_Mapping_Id;
    }

    public void setCouse_User_Section_Mapping_Id(int couse_User_Section_Mapping_Id) {
        Couse_User_Section_Mapping_Id = couse_User_Section_Mapping_Id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getRegion_Code() {
        return Region_Code;
    }

    public void setRegion_Code(String region_Code) {
        Region_Code = region_Code;
    }

    public String getRegion_Name() {
        return Region_Name;
    }

    public void setRegion_Name(String region_Name) {
        Region_Name = region_Name;
    }

    public String getSection_Status_String() {
        return Section_Status_String;
    }

    public void setSection_Status_String(String section_Status_String) {
        Section_Status_String = section_Status_String;
    }

    public String getCourse_Status_String() {
        return Course_Status_String;
    }

    public void setCourse_Status_String(String course_Status_String) {
        Course_Status_String = course_Status_String;
    }

    public boolean isShow_Print_Certificate() {
        return Show_Print_Certificate;
    }

    public void setShow_Print_Certificate(boolean show_Print_Certificate) {
        Show_Print_Certificate = show_Print_Certificate;
    }


    public String getValid_To() {
        return Valid_To;
    }

    public void setValid_To(String valid_To) {
        Valid_To = valid_To;
    }

    public int getSection_Max_Attempts() {
        return Section_Max_Attempts;
    }

    public void setSection_Max_Attempts(int section_Max_Attempts) {
        Section_Max_Attempts = section_Max_Attempts;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public boolean is_Print_Certificate() {
        return Is_Print_Certificate;
    }

    public void setIs_Print_Certificate(boolean is_Print_Certificate) {
        Is_Print_Certificate = is_Print_Certificate;
    }

    public int getShowFullSummary() {
        return ShowFullSummary;
    }

    public void setShowFullSummary(int showFullSummary) {
        ShowFullSummary = showFullSummary;
    }

    public int getCourse_Status_Value() {
        return Course_Status_Value;
    }

    public void setCourse_Status_Value(int course_Status_Value) {
        Course_Status_Value = course_Status_Value;
    }

    public List<SectionAssetModel> getLstMandoryAssets() {
        return lstMandoryAssets;
    }

    public void setLstMandoryAssets(List<SectionAssetModel> lstMandoryAssets) {
        this.lstMandoryAssets = lstMandoryAssets;
    }

    private String Sno;
    private String SectionName;
    private String NoOfContents;
    private Bitmap mStatus;
    private boolean isAlreadyCompleted;

    public String getSno() {
        return Sno;
    }

    public void setSno(String sno) {
        Sno = sno;
    }

    public String getSectionName() {
        return SectionName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

    public String getNoOfContents() {
        return NoOfContents;
    }

    public void setNoOfContents(String noOfContents) {
        NoOfContents = noOfContents;
    }

    public Bitmap getmStatus() {
        return mStatus;
    }

    public void setmStatus(Bitmap mStatus) {
        this.mStatus = mStatus;
    }

    public boolean isAlreadyCompleted() {
        return isAlreadyCompleted;
    }

    public void setAlreadyCompleted(boolean alreadyCompleted) {
        isAlreadyCompleted = alreadyCompleted;
    }

    public boolean is_Read_Assets() {
        return Is_Read_Assets;
    }

    public void setIs_Read_Assets(boolean is_Read_Assets) {
        Is_Read_Assets = is_Read_Assets;
    }

    public int getSection_Status_Value() {
        return Section_Status_Value;
    }

    public void setSection_Status_Value(int section_Status_Value) {
        Section_Status_Value = section_Status_Value;
    }

    public String getSection_Status_Text() {
        return Section_Status_Text;
    }

    public void setSection_Status_Text(String section_Status_Text) {
        Section_Status_Text = section_Status_Text;
    }

    public String getCourse_Status_Text() {
        return Course_Status_Text;
    }

    public void setCourse_Status_Text(String course_Status_Text) {
        Course_Status_Text = course_Status_Text;
    }

    public boolean is_Qualified() {
        return Is_Qualified;
    }

    public void setIs_Qualified(boolean is_Qualified) {
        Is_Qualified = is_Qualified;
    }

    public int getDisplay_Order() {
        return Display_Order;
    }

    public void setDisplay_Order(int display_Order) {
        Display_Order = display_Order;
    }

    public boolean is_Course_Section_Mandatory() {
        return Is_Course_Section_Mandatory;
    }

    public void setIs_Course_Section_Mandatory(boolean is_Course_Section_Mandatory) {
        Is_Course_Section_Mandatory = is_Course_Section_Mandatory;
    }

    public int ExtendDays;
    public int ExtendAttempts;
    public int ExtendLimits;
    public int AutoExtendAttemptsCount;
    public boolean isCourseExtend;

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

    public int getAutoExtendAttemptsCount() {
        return AutoExtendAttemptsCount;
    }

    public void setAutoExtendAttemptsCount(int autoExtendAttemptsCount) {
        AutoExtendAttemptsCount = autoExtendAttemptsCount;
    }

    public boolean isCourseExtend() {
        return isCourseExtend;
    }

    public void setCourseExtend(boolean courseExtend) {
        isCourseExtend = courseExtend;
    }

    public String getEvaluation_Mode() {
        return Evaluation_Mode;
    }

    public void setEvaluation_Mode(String evaluation_Mode) {
        Evaluation_Mode = evaluation_Mode;
    }
}

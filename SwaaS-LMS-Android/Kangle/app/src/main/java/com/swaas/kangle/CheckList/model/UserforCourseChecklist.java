package com.swaas.kangle.CheckList.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 09-08-2018.
 */

public class UserforCourseChecklist implements Serializable {

    public int Course_User_Mapping_Id;
    public int Company_Id;
    public int Course_Id;
    public int User_Id;
    public int Is_Qualified;
    public String Course_Status;
    public String Course_Status_Text;
    public int Course_Status_Value;
    public int Created_By;
    public String Created_Date;
    public String Course_Name;
    public String Checklist_Name;
    public String User_Name;
    public String Evaluation_Status;
    public String Checklist_Result_Status;

    public boolean choosenuser;
    public int isCourseRestart;
    public boolean courseRestart;
    public boolean Checklist_Restart;

    public int CheckList_id;

    public String Section_Name;
    public int Section_Id;

    public String CourseCheckListDraft;

    public String DraftId;

    public String getSection_Name() {
        return Section_Name;
    }

    public void setSection_Name(String section_Name) {
        Section_Name = section_Name;
    }

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }

    public String getCourseCheckListDraft() {
        return CourseCheckListDraft;
    }

    public void setCourseCheckListDraft(String courseCheckListDraft) {
        CourseCheckListDraft = courseCheckListDraft;
    }

    public String getDraftId() {
        return DraftId;
    }

    public void setDraftId(String draftId) {
        DraftId = draftId;
    }

    public int getCourse_User_Mapping_Id() {
        return Course_User_Mapping_Id;
    }

    public void setCourse_User_Mapping_Id(int course_User_Mapping_Id) {
        Course_User_Mapping_Id = course_User_Mapping_Id;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getCourse_Id() {
        return Course_Id;
    }

    public void setCourse_Id(int course_Id) {
        Course_Id = course_Id;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public int getIs_Qualified() {
        return Is_Qualified;
    }

    public void setIs_Qualified(int is_Qualified) {
        Is_Qualified = is_Qualified;
    }

    public String getCourse_Status() {
        return Course_Status;
    }

    public void setCourse_Status(String course_Status) {
        Course_Status = course_Status;
    }

    public String getCourse_Status_Text() {
        return Course_Status_Text;
    }

    public void setCourse_Status_Text(String course_Status_Text) {
        Course_Status_Text = course_Status_Text;
    }

    public int getCourse_Status_Value() {
        return Course_Status_Value;
    }

    public void setCourse_Status_Value(int course_Status_Value) {
        Course_Status_Value = course_Status_Value;
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

    public String getCourse_Name() {
        return Course_Name;
    }

    public void setCourse_Name(String course_Name) {
        Course_Name = course_Name;
    }

    public String getChecklist_Name() {
        return Checklist_Name;
    }

    public void setChecklist_Name(String checklist_Name) {
        Checklist_Name = checklist_Name;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getEvaluation_Status() {
        return Evaluation_Status;
    }

    public void setEvaluation_Status(String evaluation_Status) {
        Evaluation_Status = evaluation_Status;
    }

    public String getChecklist_Result_Status() {
        return Checklist_Result_Status;
    }

    public void setChecklist_Result_Status(String checklist_Result_Status) {
        Checklist_Result_Status = checklist_Result_Status;
    }

    public boolean isChoosenuser() {
        return choosenuser;
    }

    public void setChoosenuser(boolean choosenuser) {
        this.choosenuser = choosenuser;
    }

    public int getIsCourseRestart() {
        return isCourseRestart;
    }

    public void setIsCourseRestart(int isCourseRestart) {
        this.isCourseRestart = isCourseRestart;
    }

    public boolean isCourseRestart() {
        return courseRestart;
    }

    public void setCourseRestart(boolean courseRestart) {
        this.courseRestart = courseRestart;
    }

    public boolean isChecklist_Restart() {
        return Checklist_Restart;
    }

    public void setChecklist_Restart(boolean checklist_Restart) {
        Checklist_Restart = checklist_Restart;
    }

    public int getCheckList_id() {
        return CheckList_id;
    }

    public void setCheckList_id(int checkList_id) {
        CheckList_id = checkList_id;
    }
}

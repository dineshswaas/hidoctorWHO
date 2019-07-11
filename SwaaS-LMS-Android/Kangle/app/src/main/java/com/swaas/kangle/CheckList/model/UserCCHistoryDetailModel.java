package com.swaas.kangle.CheckList.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 18-09-2018.
 */

public class UserCCHistoryDetailModel implements Serializable {

    public int User_Id;
    public int Course_Id;
    public int Checklist_Id;
    public int Attempt_Number;
    public String Formatted_Section_Exam_Start_Time;
    public int Section_Id;

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public int getCourse_Id() {
        return Course_Id;
    }

    public void setCourse_Id(int course_Id) {
        Course_Id = course_Id;
    }

    public int getChecklist_Id() {
        return Checklist_Id;
    }

    public void setChecklist_Id(int checklist_Id) {
        Checklist_Id = checklist_Id;
    }

    public int getAttempt_Number() {
        return Attempt_Number;
    }

    public void setAttempt_Number(int attempt_Number) {
        Attempt_Number = attempt_Number;
    }

    public String getFormatted_Section_Exam_Start_Time() {
        return Formatted_Section_Exam_Start_Time;
    }

    public void setFormatted_Section_Exam_Start_Time(String formatted_Section_Exam_Start_Time) {
        Formatted_Section_Exam_Start_Time = formatted_Section_Exam_Start_Time;
    }

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }
}

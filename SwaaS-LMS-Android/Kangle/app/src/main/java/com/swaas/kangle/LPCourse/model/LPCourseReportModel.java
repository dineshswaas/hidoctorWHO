package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 29-08-2017.
 */

public class LPCourseReportModel implements Serializable {

    private int Course_Section_User_Exam_Id;
    private String Section_Exam_Start_Time;
    private String Section_Exam_End_Time;
    private String Section_Name;
    private boolean Is_Qualified;
    private int Attempt_Number;
    private int Record_Status;
    private String Course_Name;
    private String Formatted_Section_Exam_Start_Time;
    private String Formatted_Section_Exam_End_Time;
    private String Employee_Name;
    private int ShowFullSummary;

    public int No_Of_Questions_Mapped;

    public int getCourse_Section_User_Exam_Id() {
        return Course_Section_User_Exam_Id;
    }

    public void setCourse_Section_User_Exam_Id(int course_Section_User_Exam_Id) {
        Course_Section_User_Exam_Id = course_Section_User_Exam_Id;
    }

    public String getSection_Exam_Start_Time() {
        return Section_Exam_Start_Time;
    }

    public void setSection_Exam_Start_Time(String section_Exam_Start_Time) {
        Section_Exam_Start_Time = section_Exam_Start_Time;
    }

    public String getSection_Exam_End_Time() {
        return Section_Exam_End_Time;
    }

    public void setSection_Exam_End_Time(String section_Exam_End_Time) {
        Section_Exam_End_Time = section_Exam_End_Time;
    }

    public String getSection_Name() {
        return Section_Name;
    }

    public void setSection_Name(String section_Name) {
        Section_Name = section_Name;
    }

    public boolean is_Qualified() {
        return Is_Qualified;
    }

    public void setIs_Qualified(boolean is_Qualified) {
        Is_Qualified = is_Qualified;
    }

    public int getAttempt_Number() {
        return Attempt_Number;
    }

    public void setAttempt_Number(int attempt_Number) {
        Attempt_Number = attempt_Number;
    }

    public int getRecord_Status() {
        return Record_Status;
    }

    public void setRecord_Status(int record_Status) {
        Record_Status = record_Status;
    }

    public String getCourse_Name() {
        return Course_Name;
    }

    public void setCourse_Name(String course_Name) {
        Course_Name = course_Name;
    }

    public String getFormatted_Section_Exam_Start_Time() {
        return Formatted_Section_Exam_Start_Time;
    }

    public void setFormatted_Section_Exam_Start_Time(String formatted_Section_Exam_Start_Time) {
        Formatted_Section_Exam_Start_Time = formatted_Section_Exam_Start_Time;
    }

    public String getFormatted_Section_Exam_End_Time() {
        return Formatted_Section_Exam_End_Time;
    }

    public void setFormatted_Section_Exam_End_Time(String formatted_Section_Exam_End_Time) {
        Formatted_Section_Exam_End_Time = formatted_Section_Exam_End_Time;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
    }

    public int getShowFullSummary() {
        return ShowFullSummary;
    }

    public void setShowFullSummary(int showFullSummary) {
        ShowFullSummary = showFullSummary;
    }

    public int getNo_Of_Questions_Mapped() {
        return No_Of_Questions_Mapped;
    }

    public void setNo_Of_Questions_Mapped(int no_Of_Questions_Mapped) {
        No_Of_Questions_Mapped = no_Of_Questions_Mapped;
    }
}

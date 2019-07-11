package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

/**
 * Created by Hariharan on 24/8/17.
 */

public class CourseUserAssessHeader implements Serializable {

     public String Company_Id;
     public int User_Id;
     public int Course_Section_User_Exam_Id;
     public int Course_User_Assignment_Id;
     public int Couse_User_Section_Mapping_Id;
     public int Course_ID;
     public int Publish_ID;
     public int Section_ID;
     public int Achieved_Percentage;
     public String Pass_Percentage;
     public int Is_Qualified;
     public String Local_TimeZone;
     public String  Offset_Value;

    public String getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(String company_Id) {
        Company_Id = company_Id;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public int getCourse_Section_User_Exam_Id() {
        return Course_Section_User_Exam_Id;
    }

    public void setCourse_Section_User_Exam_Id(int course_Section_User_Exam_Id) {
        Course_Section_User_Exam_Id = course_Section_User_Exam_Id;
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

    public int getCourse_ID() {
        return Course_ID;
    }

    public void setCourse_ID(int course_ID) {
        Course_ID = course_ID;
    }

    public int getPublish_ID() {
        return Publish_ID;
    }

    public void setPublish_ID(int publish_ID) {
        Publish_ID = publish_ID;
    }

    public int getSection_ID() {
        return Section_ID;
    }

    public void setSection_ID(int section_ID) {
        Section_ID = section_ID;
    }

    public int getAchieved_Percentage() {
        return Achieved_Percentage;
    }

    public void setAchieved_Percentage(int achieved_Percentage) {
        Achieved_Percentage = achieved_Percentage;
    }

    public String getPass_Percentage() {
        return Pass_Percentage;
    }

    public void setPass_Percentage(String pass_Percentage) {
        Pass_Percentage = pass_Percentage;
    }

    public int getIs_Qualified() {
        return Is_Qualified;
    }

    public void setIs_Qualified(int is_Qualified) {
        Is_Qualified = is_Qualified;
    }

    public String getLocal_TimeZone() {
        return Local_TimeZone;
    }

    public void setLocal_TimeZone(String local_TimeZone) {
        Local_TimeZone = local_TimeZone;
    }

    public String getOffset_Value() {
        return Offset_Value;
    }

    public void setOffset_Value(String offset_Value) {
        Offset_Value = offset_Value;
    }
}

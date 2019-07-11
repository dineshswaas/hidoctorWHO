package com.swaas.kangle.CheckList.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 01-05-2018.
 */

public class CourseUserAssessHeader implements Serializable {

     public String Company_Id;
     public int User_Id;
     public int Checklist_Section_User_Exam_Id;
     public int Checklist_User_Assignment_Id;
     public int Couse_User_Section_Mapping_Id;
     public int Checklist_ID;
     public int Publish_ID;
     public int Section_ID;
     public int Achieved_Percentage;
     public String Pass_Percentage;
     public int Is_Qualified;
     public String Local_TimeZone;
     public String  Offset_Value;

    public int Is_Acknowledge;
    public String Acknowledge_Comments;
    public String Acknowledge_img_url;

    public List<Acknowledgement_urls> Ack_urls;

    public int Checklist_Publish_Type;
    public int CheckList_Publish_Group_Id;
    public String On_Behalf;
    public String Ack_Given_Name;

    public int User_Course_Checklist_Mapping_Id;
    public String Is_Draft;

    public int getUser_Course_Checklist_Mapping_Id() {
        return User_Course_Checklist_Mapping_Id;
    }

    public void setUser_Course_Checklist_Mapping_Id(int user_Course_Checklist_Mapping_Id) {
        User_Course_Checklist_Mapping_Id = user_Course_Checklist_Mapping_Id;
    }

    public String getIs_Draft() {
        return Is_Draft;
    }

    public void setIs_Draft(String is_Draft) {
        Is_Draft = is_Draft;
    }

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
        return Checklist_Section_User_Exam_Id;
    }

    public void setCourse_Section_User_Exam_Id(int course_Section_User_Exam_Id) {
        Checklist_Section_User_Exam_Id = course_Section_User_Exam_Id;
    }

    public int getCourse_User_Assignment_Id() {
        return Checklist_User_Assignment_Id;
    }

    public void setCourse_User_Assignment_Id(int course_User_Assignment_Id) {
        Checklist_User_Assignment_Id = course_User_Assignment_Id;
    }

    public int getCouse_User_Section_Mapping_Id() {
        return Couse_User_Section_Mapping_Id;
    }

    public void setCouse_User_Section_Mapping_Id(int couse_User_Section_Mapping_Id) {
        Couse_User_Section_Mapping_Id = couse_User_Section_Mapping_Id;
    }

    public int getCourse_ID() {
        return Checklist_ID;
    }

    public void setCourse_ID(int course_ID) {
        Checklist_ID = course_ID;
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


    public int getIs_Acknowledge() {
        return Is_Acknowledge;
    }

    public void setIs_Acknowledge(int is_Acknowledge) {
        Is_Acknowledge = is_Acknowledge;
    }

    public String getAcknowledge_Comments() {
        return Acknowledge_Comments;
    }

    public void setAcknowledge_Comments(String acknowledge_Comments) {
        Acknowledge_Comments = acknowledge_Comments;
    }

    public String getAcknowledge_img_url() {
        return Acknowledge_img_url;
    }

    public void setAcknowledge_img_url(String acknowledge_img_url) {
        Acknowledge_img_url = acknowledge_img_url;
    }

    public int getChecklist_Publish_Type() {
        return Checklist_Publish_Type;
    }

    public void setChecklist_Publish_Type(int checklist_Publish_Type) {
        Checklist_Publish_Type = checklist_Publish_Type;
    }

    public String getOn_Behalf() {
        return On_Behalf;
    }

    public void setOn_Behalf(String on_Behalf) {
        On_Behalf = on_Behalf;
    }

    public List<Acknowledgement_urls> getAck_urls() {
        return Ack_urls;
    }

    public void setAck_urls(List<Acknowledgement_urls> ack_urls) {
        Ack_urls = ack_urls;
    }

    public String getAck_Given_Name() {
        return Ack_Given_Name;
    }

    public void setAck_Given_Name(String ack_Given_Name) {
        Ack_Given_Name = ack_Given_Name;
    }
}

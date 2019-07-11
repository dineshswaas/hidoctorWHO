package com.swaas.kangle.LPCourse;

import java.io.Serializable;

/**
 * Created by Sayellessh on 18-08-2017.
 */

public class CourseAnalyticsModel implements Serializable {

    public int _Id;
    public int Company_Id;
    public int Course_Id;
    public int Section_Id;
    public int Publish_Id;
    public String DA_Code;
    public String User_Id;
    public String User_Name;
    public String Region_Name;
    public String Region_Code;

    public int Offline_Play;
    public int Online_Play;
    public String Play_Time;
    public boolean Is_Preview ;

    public String Local_TimeZone;
    public int Offset_Value;
    public int Image_Id;
    public String Doc_Type;
    public int Course_User_Assignment_Id;
    public int Couse_User_Section_Mapping_Id;


    public int get_Id() {
        return _Id;
    }

    public void set_Id(int _Id) {
        this._Id = _Id;
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

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }

    public int getPublish_Id() {
        return Publish_Id;
    }

    public void setPublish_Id(int publish_Id) {
        Publish_Id = publish_Id;
    }

    public String getDA_Code() {
        return DA_Code;
    }

    public void setDA_Code(String DA_Code) {
        this.DA_Code = DA_Code;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getRegion_Name() {
        return Region_Name;
    }

    public void setRegion_Name(String region_Name) {
        Region_Name = region_Name;
    }

    public String getRegion_Code() {
        return Region_Code;
    }

    public void setRegion_Code(String region_Code) {
        Region_Code = region_Code;
    }

    public int getOffline_Play() {
        return Offline_Play;
    }

    public void setOffline_Play(int offline_Play) {
        Offline_Play = offline_Play;
    }

    public int getOnline_Play() {
        return Online_Play;
    }

    public void setOnline_Play(int online_Play) {
        Online_Play = online_Play;
    }

    public String getPlay_Time() {
        return Play_Time;
    }

    public void setPlay_Time(String play_Time) {
        Play_Time = play_Time;
    }

    public boolean is_Preview() {
        return Is_Preview;
    }

    public void setIs_Preview(boolean is_Preview) {
        Is_Preview = is_Preview;
    }

    public String getLocal_TimeZone() {
        return Local_TimeZone;
    }

    public void setLocal_TimeZone(String local_TimeZone) {
        Local_TimeZone = local_TimeZone;
    }

    public int getOffset_Value() {
        return Offset_Value;
    }

    public void setOffset_Value(int offset_Value) {
        Offset_Value = offset_Value;
    }

    public int getImage_Id() {
        return Image_Id;
    }

    public void setImage_Id(int image_Id) {
        Image_Id = image_Id;
    }

    public String getDoc_Type() {
        return Doc_Type;
    }

    public void setDoc_Type(String doc_Type) {
        Doc_Type = doc_Type;
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
}

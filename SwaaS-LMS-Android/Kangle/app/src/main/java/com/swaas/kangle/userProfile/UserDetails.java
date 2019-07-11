package com.swaas.kangle.userProfile;

import java.io.Serializable;

/**
 * Created by Sayellessh on 05-07-2018.
 */

public class UserDetails implements Serializable {

    public int User_Id;
    public String User_Name;
    public int Company_Id;
    public String DOA;
    public String DOB;
    public String Designation;
    public String Region_Code;
    public String Region_Name;


    public int Active_Status;
    public int City_Id;
    public String City_Name;
    public int Created_By;
    public String Created_Date;
    public int Updated_By;
    public String Updated_Date;

    //educationdetails
    public int User_Education_Id;
    public int Education_From;
    public int Education_Id;
    public String Education_Name;
    public int Education_To;
    public String Education_Type;
    public int Institution_Id;
    public String Institution_Name;
    public int Is_Current_Education;

    //workDetails
    public int Is_Current_Work;
    public int User_Work_Id;
    public int Work_From;
    public int Work_Id;
    public String Work_Name;
    public String Work_Position;
    public int Work_To;


    //interest
    public int Interest_Id;
    public String Interest_Name;
    public int User_Interest_Id;

    //life events
    public int Day;
    public int EventGroup_Id;
    public int EventType_Id;
    public String EventType_Name;
    public int Mode;
    public int Month;
    public int User_Event_Id;
    public int Year;

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public String getDOA() {
        return DOA;
    }

    public void setDOA(String DOA) {
        this.DOA = DOA;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
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

    public int getActive_Status() {
        return Active_Status;
    }

    public void setActive_Status(int active_Status) {
        Active_Status = active_Status;
    }

    public int getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(int city_Id) {
        City_Id = city_Id;
    }

    public String getCity_Name() {
        return City_Name;
    }

    public void setCity_Name(String city_Name) {
        City_Name = city_Name;
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

    public int getUser_Education_Id() {
        return User_Education_Id;
    }

    public void setUser_Education_Id(int user_Education_Id) {
        User_Education_Id = user_Education_Id;
    }

    public int getEducation_From() {
        return Education_From;
    }

    public void setEducation_From(int education_From) {
        Education_From = education_From;
    }

    public int getEducation_Id() {
        return Education_Id;
    }

    public void setEducation_Id(int education_Id) {
        Education_Id = education_Id;
    }

    public String getEducation_Name() {
        return Education_Name;
    }

    public void setEducation_Name(String education_Name) {
        Education_Name = education_Name;
    }

    public int getEducation_To() {
        return Education_To;
    }

    public void setEducation_To(int education_To) {
        Education_To = education_To;
    }

    public String getEducation_Type() {
        return Education_Type;
    }

    public void setEducation_Type(String education_Type) {
        Education_Type = education_Type;
    }

    public int getInstitution_Id() {
        return Institution_Id;
    }

    public void setInstitution_Id(int institution_Id) {
        Institution_Id = institution_Id;
    }

    public String getInstitution_Name() {
        return Institution_Name;
    }

    public void setInstitution_Name(String institution_Name) {
        Institution_Name = institution_Name;
    }

    public int getIs_Current_Education() {
        return Is_Current_Education;
    }

    public void setIs_Current_Education(int is_Current_Education) {
        Is_Current_Education = is_Current_Education;
    }

    public int getIs_Current_Work() {
        return Is_Current_Work;
    }

    public void setIs_Current_Work(int is_Current_Work) {
        Is_Current_Work = is_Current_Work;
    }

    public int getUser_Work_Id() {
        return User_Work_Id;
    }

    public void setUser_Work_Id(int user_Work_Id) {
        User_Work_Id = user_Work_Id;
    }

    public int getWork_From() {
        return Work_From;
    }

    public void setWork_From(int work_From) {
        Work_From = work_From;
    }

    public int getWork_Id() {
        return Work_Id;
    }

    public void setWork_Id(int work_Id) {
        Work_Id = work_Id;
    }

    public String getWork_Name() {
        return Work_Name;
    }

    public void setWork_Name(String work_Name) {
        Work_Name = work_Name;
    }

    public String getWork_Position() {
        return Work_Position;
    }

    public void setWork_Position(String work_Position) {
        Work_Position = work_Position;
    }

    public int getWork_To() {
        return Work_To;
    }

    public void setWork_To(int work_To) {
        Work_To = work_To;
    }

    public int getInterest_Id() {
        return Interest_Id;
    }

    public void setInterest_Id(int interest_Id) {
        Interest_Id = interest_Id;
    }

    public String getInterest_Name() {
        return Interest_Name;
    }

    public void setInterest_Name(String interest_Name) {
        Interest_Name = interest_Name;
    }

    public int getUser_Interest_Id() {
        return User_Interest_Id;
    }

    public void setUser_Interest_Id(int user_Interest_Id) {
        User_Interest_Id = user_Interest_Id;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public int getEventGroup_Id() {
        return EventGroup_Id;
    }

    public void setEventGroup_Id(int eventGroup_Id) {
        EventGroup_Id = eventGroup_Id;
    }

    public int getEventType_Id() {
        return EventType_Id;
    }

    public void setEventType_Id(int eventType_Id) {
        EventType_Id = eventType_Id;
    }

    public String getEventType_Name() {
        return EventType_Name;
    }

    public void setEventType_Name(String eventType_Name) {
        EventType_Name = eventType_Name;
    }

    public int getMode() {
        return Mode;
    }

    public void setMode(int mode) {
        Mode = mode;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getUser_Event_Id() {
        return User_Event_Id;
    }

    public void setUser_Event_Id(int user_Event_Id) {
        User_Event_Id = user_Event_Id;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }
}

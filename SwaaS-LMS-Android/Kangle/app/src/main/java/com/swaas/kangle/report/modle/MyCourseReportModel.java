package com.swaas.kangle.report.modle;

import java.io.Serializable;

/**
 * Created by Hariharan on 7/9/17.
 */

public class MyCourseReportModel implements Serializable {

    public int User_Id;
    public String Company_Id;
    public String Employee_Name;
    public String User_Name;
    public String Region_Name;
    public String User_Type_Name;
    public String Course_Name;
    public String Category_Name;
    public String Section_Name;
    public int AttemptNo;
    public int No_Of_Questions_Mapped;
    public int No_Of_Correct_Answers;
    public int Pass_Percentage;
    public int Achieved_Percentage;
    public String Publish_Date;
    public String Last_Access_Date;
    public String Region_Type_Name;
    public String Course_Status_Text;
    public int Course_Status_Value;
    public int Section_Status_value;

    public String Country;

    public String FirstName;
    public String LastName;

    //new Api

    public int Module_Id;
    public String Module_Name;
    public String Module_Status_Text;
    public int Module_Status_Value;
    public int Reference_Id;

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public String getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(String company_Id) {
        Company_Id = company_Id;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
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

    public String getUser_Type_Name() {
        return User_Type_Name;
    }

    public void setUser_Type_Name(String user_Type_Name) {
        User_Type_Name = user_Type_Name;
    }

    public String getCourse_Name() {
        return Course_Name;
    }

    public void setCourse_Name(String course_Name) {
        Course_Name = course_Name;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public String getSection_Name() {
        return Section_Name;
    }

    public void setSection_Name(String section_Name) {
        Section_Name = section_Name;
    }

    public int getAttemptNo() {
        return AttemptNo;
    }

    public void setAttemptNo(int attemptNo) {
        AttemptNo = attemptNo;
    }

    public int getNo_Of_Questions_Mapped() {
        return No_Of_Questions_Mapped;
    }

    public void setNo_Of_Questions_Mapped(int no_Of_Questions_Mapped) {
        No_Of_Questions_Mapped = no_Of_Questions_Mapped;
    }

    public int getNo_Of_Correct_Answers() {
        return No_Of_Correct_Answers;
    }

    public void setNo_Of_Correct_Answers(int no_Of_Correct_Answers) {
        No_Of_Correct_Answers = no_Of_Correct_Answers;
    }

    public int getPass_Percentage() {
        return Pass_Percentage;
    }

    public void setPass_Percentage(int pass_Percentage) {
        Pass_Percentage = pass_Percentage;
    }

    public int getAchieved_Percentage() {
        return Achieved_Percentage;
    }

    public void setAchieved_Percentage(int achieved_Percentage) {
        Achieved_Percentage = achieved_Percentage;
    }

    public String getPublish_Date() {
        return Publish_Date;
    }

    public void setPublish_Date(String publish_Date) {
        Publish_Date = publish_Date;
    }

    public String getLast_Access_Date() {
        return Last_Access_Date;
    }

    public void setLast_Access_Date(String last_Access_Date) {
        Last_Access_Date = last_Access_Date;
    }

    public String getRegion_Type_Name() {
        return Region_Type_Name;
    }

    public void setRegion_Type_Name(String region_Type_Name) {
        Region_Type_Name = region_Type_Name;
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

    public int getSection_Status_value() {
        return Section_Status_value;
    }

    public void setSection_Status_value(int section_Status_value) {
        Section_Status_value = section_Status_value;
    }


    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }


    public int getModule_Id() {
        return Module_Id;
    }

    public void setModule_Id(int module_Id) {
        Module_Id = module_Id;
    }

    public String getModule_Name() {
        return Module_Name;
    }

    public void setModule_Name(String module_Name) {
        Module_Name = module_Name;
    }

    public String getModule_Status_Text() {
        return Module_Status_Text;
    }

    public void setModule_Status_Text(String module_Status_Text) {
        Module_Status_Text = module_Status_Text;
    }

    public int getModule_Status_Value() {
        return Module_Status_Value;
    }

    public void setModule_Status_Value(int module_Status_Value) {
        Module_Status_Value = module_Status_Value;
    }

    public int getReference_Id() {
        return Reference_Id;
    }

    public void setReference_Id(int reference_Id) {
        Reference_Id = reference_Id;
    }
}

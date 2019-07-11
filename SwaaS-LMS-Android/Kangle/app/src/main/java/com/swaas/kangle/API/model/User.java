package com.swaas.kangle.API.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 24-04-2017.
 */

public class User implements Serializable {

    public int UserID;
    public int Company_Id;
    public String Company_Code;
    public String FirstName;
    public String LastName;
    public String MiddleName;
    public String SurName;
    public String Email_Id;
    public String Mobile_Number;
    public String DOB;
    public String Gender;
    public String Profile_Photo_BLOB_URL;
    public String Region_Name;
    public String Employee_Name;
    public String Support_Email;
    public String Support_Phone_Number;
    public String Is_Ad_Course_Enabled;
    public String User_Code;
    public String Region_Code;
    public String User_Type_Code;
    public String pswd;
    public String Division_Code;
    public String Division_Name;
    public String Company_Logo;

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public String getCompany_Code() {
        return Company_Code;
    }

    public void setCompany_Code(String company_Code) {
        Company_Code = company_Code;
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

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getSurName() {
        return SurName;
    }

    public void setSurName(String surName) {
        SurName = surName;
    }

    public String getEmail_Id() {
        return Email_Id;
    }

    public void setEmail_Id(String email_id) {
        Email_Id = email_id;
    }

    public String getMobile_Number() {
        return Mobile_Number;
    }

    public void setMobile_Number(String mobile_Number) {
        Mobile_Number = mobile_Number;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getProfile_Photo_BLOB_URL() {
        return Profile_Photo_BLOB_URL;
    }

    public void setProfile_Photo_BLOB_URL(String profile_Photo_BLOB_URL) {
        Profile_Photo_BLOB_URL = profile_Photo_BLOB_URL;
    }

    public String getRegion_Name() {
        return Region_Name;
    }

    public void setRegion_Name(String region_Name) {
        Region_Name = region_Name;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
    }

    public String getSupport_Email() {
        return Support_Email;
    }

    public void setSupport_Email(String support_Email) {
        Support_Email = support_Email;
    }

    public String getSupport_Phone_Number() {
        return Support_Phone_Number;
    }

    public void setSupport_Phone_Number(String support_Phone_Number) {
        Support_Phone_Number = support_Phone_Number;
    }

    public String getIs_Ad_Course_Enabled() {
        return Is_Ad_Course_Enabled;
    }

    public void setIs_Ad_Course_Enabled(String is_Ad_Course_Enabled) {
        Is_Ad_Course_Enabled = is_Ad_Course_Enabled;
    }

    public String getUser_Code() {
        return User_Code;
    }

    public void setUser_Code(String user_Code) {
        User_Code = user_Code;
    }

    public String getRegion_Code() {
        return Region_Code;
    }

    public void setRegion_Code(String region_Code) {
        Region_Code = region_Code;
    }

    public String getUser_Type_Code() {
        return User_Type_Code;
    }

    public void setUser_Type_Code(String user_Type_Code) {
        User_Type_Code = user_Type_Code;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getDivision_Code() {
        return Division_Code;
    }

    public void setDivision_Code(String division_Code) {
        Division_Code = division_Code;
    }

    public String getDivision_Name() {
        return Division_Name;
    }

    public void setDivision_Name(String division_Name) {
        Division_Name = division_Name;
    }

    public String getCompany_Logo() {
        return Company_Logo;
    }

    public void setCompany_Logo(String company_Logo) {
        Company_Logo = company_Logo;
    }
}

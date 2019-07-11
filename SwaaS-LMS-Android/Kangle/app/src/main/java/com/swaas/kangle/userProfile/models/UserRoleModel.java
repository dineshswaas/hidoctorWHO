package com.swaas.kangle.userProfile.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 04-12-2018.
 */

public class UserRoleModel implements Serializable {

    public String Company_Code;
    public int Company_Id;
    public String Company_Name;
    public String Employee_Name;
    public String Region_Code;
    public String Region_Name;
    public int User_Id;
    public String User_Name;
    public String User_Type_Code;
    public String User_Type_Name;

    public String getCompany_Code() {
        return Company_Code;
    }

    public void setCompany_Code(String company_Code) {
        Company_Code = company_Code;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public String getCompany_Name() {
        return Company_Name;
    }

    public void setCompany_Name(String company_Name) {
        Company_Name = company_Name;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
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

    public String getUser_Type_Code() {
        return User_Type_Code;
    }

    public void setUser_Type_Code(String user_Type_Code) {
        User_Type_Code = user_Type_Code;
    }

    public String getUser_Type_Name() {
        return User_Type_Name;
    }

    public void setUser_Type_Name(String user_Type_Name) {
        User_Type_Name = user_Type_Name;
    }
}

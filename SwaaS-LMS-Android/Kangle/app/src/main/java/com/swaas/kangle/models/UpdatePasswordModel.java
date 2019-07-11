package com.swaas.kangle.models;

/**
 * Created by Sayellessh on 02-06-2017.
 */

public class UpdatePasswordModel {

    public int Company_Id;
    public String Company_Code;
    public String User_Name;
    public String User_Code;
    public String New_Password;
    public String Old_Password;
    public String Confirm_Password;
    public String Message;
    public boolean Status;

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

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_Code() {
        return User_Code;
    }

    public void setUser_Code(String user_Code) {
        User_Code = user_Code;
    }

    public String getNew_Password() {
        return New_Password;
    }

    public void setNew_Password(String new_Password) {
        New_Password = new_Password;
    }

    public String getOld_Password() {
        return Old_Password;
    }

    public void setOld_Password(String old_Password) {
        Old_Password = old_Password;
    }

    public String getConfirm_Password() {
        return Confirm_Password;
    }

    public void setConfirm_Password(String confirm_Password) {
        Confirm_Password = confirm_Password;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}

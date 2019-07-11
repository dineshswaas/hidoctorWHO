package com.swaas.kangle.API.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 24-04-2017.
 */

public class CheckUser implements Serializable {

    public boolean Transaction_Status;
    public String Message_To_Display;
    public String Additional_Context;
    public int Company_Id;
    public int User_Id;
    public String Additional_Context_More;

    public boolean getTransaction_Status() {
        return Transaction_Status;
    }

    public void setTransaction_Status(boolean transaction_Status) {
        Transaction_Status = transaction_Status;
    }

    public String getMessage_To_Display() {
        return Message_To_Display;
    }

    public void setMessage_To_Display(String message_To_Display) {
        Message_To_Display = message_To_Display;
    }

    public String getAdditional_Context() {
        return Additional_Context;
    }

    public void setAdditional_Context(String additional_Context) {
        Additional_Context = additional_Context;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public String getAdditional_Context_More() {
        return Additional_Context_More;
    }

    public void setAdditional_Context_More(String additional_Context_More) {
        Additional_Context_More = additional_Context_More;
    }
}

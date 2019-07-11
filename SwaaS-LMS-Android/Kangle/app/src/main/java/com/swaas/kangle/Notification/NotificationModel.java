package com.swaas.kangle.Notification;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sayellessh on 17-10-2018.
 */

public class NotificationModel implements Serializable {

    public int Reference_Id;
    public String Title;
    public String Category;
    public String Message_text;
    public String Message_Date;
    public int Importance;
    public int Company_Id;
    public String Attachment;
    public String Id;
    public String Context;
    public String Context1;
    public String Formatted_DateTime;

    public Date date;

    public String onlyDate;

    public String App_Message_Date;

    public int getCategoryId() {
        return Reference_Id;
    }

    public void setCategoryId(int categoryId) {
        Reference_Id = categoryId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getMessage_text() {
        return Message_text;
    }

    public void setMessage_text(String message_text) {
        Message_text = message_text;
    }

    public String getMessage_Date() {
        return Message_Date;
    }

    public void setMessage_Date(String message_Date) {
        Message_Date = message_Date;
    }

    public int getImportance() {
        return Importance;
    }

    public void setImportance(int importance) {
        Importance = importance;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    public String getContext1() {
        return Context1;
    }

    public void setContext1(String context1) {
        Context1 = context1;
    }

    public String getFormatted_DateTime() {
        return Formatted_DateTime;
    }

    public void setFormatted_DateTime(String formatted_DateTime) {
        Formatted_DateTime = formatted_DateTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //Notification count model

    public String Category_Id;
    public String Category_Name;
    public int Count;

    public String getCategory_Id() {
        return Category_Id;
    }

    public void setCategory_Id(String category_Id) {
        Category_Id = category_Id;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getApp_Message_Date() {
        return App_Message_Date;
    }

    public void setApp_Message_Date(String app_Message_Date) {
        App_Message_Date = app_Message_Date;
    }

    public String getOnlyDate() {
        return onlyDate;
    }

    public void setOnlyDate(String onlyDate) {
        this.onlyDate = onlyDate;
    }
}

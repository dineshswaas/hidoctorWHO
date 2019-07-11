package com.swaas.kangle.CheckList.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 19-04-2018.
 */

public class CheckListModel implements Serializable {

    public int Checklist_Id;
    public String Checklist_Name;
    public String Checklist_Type;
    public int Checklist_Type_ID;
    public String Checklist_Frequency_Type;
    public int Checklist_Frequency_Id;
    public String Checklist_Description;
    public String Checklist_Category_Id;
    public String Checklist_Tags;
    public String Checklist_Image_URL;
    public String Category_Name;
    public String Valid_From;
    public String Valid_To;
    public int Publish_Id;
    public boolean Checklist_Status;
    public String Checklist_Status_String;
    public String Valid_From_String;
    public String Valid_To_String;
    public int Checklist_User_Assignment_Id;
    public int Company_Id;
    public String Publish_Date;
    public String Publish_Date_String;
    public String Status;
    public int Checklist_Status_Value;
    public String Last_Test_Date_String;
    public String Last_Test_Date;
    public int CheckList_Publish_Group_Id;

    public String Tags;

    public boolean iscatchecked;
    public boolean istagchecked;

    public int Checklist_Classification;
    public int Ref_Id;


    public int getChecklist_Id() {
        return Checklist_Id;
    }

    public void setChecklist_Id(int checklist_Id) {
        Checklist_Id = checklist_Id;
    }

    public String getChecklist_Name() {
        return Checklist_Name;
    }

    public void setChecklist_Name(String checklist_Name) {
        Checklist_Name = checklist_Name;
    }

    public String getChecklist_Type() {
        return Checklist_Type;
    }

    public void setChecklist_Type(String checklist_Type) {
        Checklist_Type = checklist_Type;
    }

    public int getChecklist_Type_ID() {
        return Checklist_Type_ID;
    }

    public void setChecklist_Type_ID(int checklist_Type_ID) {
        Checklist_Type_ID = checklist_Type_ID;
    }

    public String getChecklist_Frequency_Type() {
        return Checklist_Frequency_Type;
    }

    public void setChecklist_Frequency_Type(String checklist_Frequency_Type) {
        Checklist_Frequency_Type = checklist_Frequency_Type;
    }

    public int getChecklist_Frequency_Id() {
        return Checklist_Frequency_Id;
    }

    public void setChecklist_Frequency_Id(int checklist_Frequency_Id) {
        Checklist_Frequency_Id = checklist_Frequency_Id;
    }

    public String getChecklist_Description() {
        return Checklist_Description;
    }

    public void setChecklist_Description(String checklist_Description) {
        Checklist_Description = checklist_Description;
    }

    public String getChecklist_Category_Id() {
        return Checklist_Category_Id;
    }

    public void setChecklist_Category_Id(String checklist_Category_Id) {
        Checklist_Category_Id = checklist_Category_Id;
    }

    public String getChecklist_Tags() {
        return Checklist_Tags;
    }

    public void setChecklist_Tags(String checklist_Tags) {
        Checklist_Tags = checklist_Tags;
    }

    public String getChecklist_Image_URL() {
        return Checklist_Image_URL;
    }

    public void setChecklist_Image_URL(String checklist_Image_URL) {
        Checklist_Image_URL = checklist_Image_URL;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }

    public String getValid_From() {
        return Valid_From;
    }

    public void setValid_From(String valid_From) {
        Valid_From = valid_From;
    }

    public String getValid_To() {
        return Valid_To;
    }

    public void setValid_To(String valid_To) {
        Valid_To = valid_To;
    }

    public int getPublish_Id() {
        return Publish_Id;
    }

    public void setPublish_Id(int publish_Id) {
        Publish_Id = publish_Id;
    }

    public boolean isChecklist_Status() {
        return Checklist_Status;
    }

    public void setChecklist_Status(boolean checklist_Status) {
        Checklist_Status = checklist_Status;
    }

    public String getChecklist_Status_String() {
        return Checklist_Status_String;
    }

    public void setChecklist_Status_String(String checklist_Status_String) {
        Checklist_Status_String = checklist_Status_String;
    }

    public String getValid_From_String() {
        return Valid_From_String;
    }

    public void setValid_From_String(String valid_From_String) {
        Valid_From_String = valid_From_String;
    }

    public String getValid_To_String() {
        return Valid_To_String;
    }

    public void setValid_To_String(String valid_To_String) {
        Valid_To_String = valid_To_String;
    }

    public int getChecklist_User_Assignment_Id() {
        return Checklist_User_Assignment_Id;
    }

    public void setChecklist_User_Assignment_Id(int checklist_User_Assignment_Id) {
        Checklist_User_Assignment_Id = checklist_User_Assignment_Id;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public String getPublish_Date() {
        return Publish_Date;
    }

    public void setPublish_Date(String publish_Date) {
        Publish_Date = publish_Date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getChecklist_Status_Value() {
        return Checklist_Status_Value;
    }

    public void setChecklist_Status_Value(int checklist_Status_Value) {
        Checklist_Status_Value = checklist_Status_Value;
    }

    public String getLast_Test_Date_String() {
        return Last_Test_Date_String;
    }

    public void setLast_Test_Date_String(String last_Test_Date_String) {
        Last_Test_Date_String = last_Test_Date_String;
    }

    public String getLast_Test_Date() {
        return Last_Test_Date;
    }

    public void setLast_Test_Date(String last_Test_Date) {
        Last_Test_Date = last_Test_Date;
    }

    public String getPublish_Date_String() {
        return Publish_Date_String;
    }

    public void setPublish_Date_String(String publish_Date_String) {
        Publish_Date_String = publish_Date_String;
    }

    public int getCheckList_Publish_Group_Id() {
        return CheckList_Publish_Group_Id;
    }

    public void setCheckList_Publish_Group_Id(int checkList_Publish_Group_Id) {
        CheckList_Publish_Group_Id = checkList_Publish_Group_Id;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public boolean iscatchecked() {
        return iscatchecked;
    }

    public void setIscatchecked(boolean iscatchecked) {
        this.iscatchecked = iscatchecked;
    }

    public boolean istagchecked() {
        return istagchecked;
    }

    public void setIstagchecked(boolean istagchecked) {
        this.istagchecked = istagchecked;
    }

    public int getChecklist_Classification() {
        return Checklist_Classification;
    }

    public void setChecklist_Classification(int checklist_Classification) {
        Checklist_Classification = checklist_Classification;
    }

    public int getRef_Id() {
        return Ref_Id;
    }

    public void setRef_Id(int ref_Id) {
        Ref_Id = ref_Id;
    }
}

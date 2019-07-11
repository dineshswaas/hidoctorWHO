package com.swaas.kangle.LPCourse;

import java.io.Serializable;

/**
 * Created by Sayellessh on 11-08-2017.
 */

public class SectionAssetModel implements Serializable {

    public int Company_Id;
    public int Mapping_Id;
    public int Course_Id;
    public int Section_Id;
    public String Section_Name;
    public int Asset_Id;
    public int Display_Order;
    public boolean Record_Status;
    public int Created_By;
    public String Created_Date;
    public int Modified_By;
    public String Modified_Date;
    public boolean Is_Active;
    public boolean IsMandatory;
    public double Min_Duration;
    public double Min_Minitues;
    public double Min_Secounds;
    public String DA_Type;
    public boolean Is_Minutes;
    public String Mandatory;
    public String DA_Name;


    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getMapping_Id() {
        return Mapping_Id;
    }

    public void setMapping_Id(int mapping_Id) {
        Mapping_Id = mapping_Id;
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

    public String getSection_Name() {
        return Section_Name;
    }

    public void setSection_Name(String section_Name) {
        Section_Name = section_Name;
    }

    public int getAsset_Id() {
        return Asset_Id;
    }

    public void setAsset_Id(int asset_Id) {
        Asset_Id = asset_Id;
    }

    public int getDisplay_Order() {
        return Display_Order;
    }

    public void setDisplay_Order(int display_Order) {
        Display_Order = display_Order;
    }

    public boolean isRecord_Status() {
        return Record_Status;
    }

    public void setRecord_Status(boolean record_Status) {
        Record_Status = record_Status;
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

    public int getModified_By() {
        return Modified_By;
    }

    public void setModified_By(int modified_By) {
        Modified_By = modified_By;
    }

    public String getModified_Date() {
        return Modified_Date;
    }

    public void setModified_Date(String modified_Date) {
        Modified_Date = modified_Date;
    }

    public boolean is_Active() {
        return Is_Active;
    }

    public void setIs_Active(boolean is_Active) {
        Is_Active = is_Active;
    }

    public boolean isMandatory() {
        return IsMandatory;
    }

    public void setMandatory(boolean mandatory) {
        IsMandatory = mandatory;
    }

    public double getMin_Duration() {
        return Min_Duration;
    }

    public void setMin_Duration(double min_Duration) {
        Min_Duration = min_Duration;
    }

    public double getMin_Minitues() {
        return Min_Minitues;
    }

    public void setMin_Minitues(double min_Minitues) {
        Min_Minitues = min_Minitues;
    }

    public double getMin_Secounds() {
        return Min_Secounds;
    }

    public void setMin_Secounds(double min_Secounds) {
        Min_Secounds = min_Secounds;
    }

    public String getDA_Type() {
        return DA_Type;
    }

    public void setDA_Type(String DA_Type) {
        this.DA_Type = DA_Type;
    }

    public boolean is_Minutes() {
        return Is_Minutes;
    }

    public void setIs_Minutes(boolean is_Minutes) {
        Is_Minutes = is_Minutes;
    }

    public String getMandatory() {
        return Mandatory;
    }

    public void setMandatory(String mandatory) {
        Mandatory = mandatory;
    }

    public String getDA_Name() {
        return DA_Name;
    }

    public void setDA_Name(String DA_Name) {
        this.DA_Name = DA_Name;
    }
}

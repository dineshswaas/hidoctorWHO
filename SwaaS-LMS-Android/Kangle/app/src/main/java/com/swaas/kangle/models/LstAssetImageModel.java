package com.swaas.kangle.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 09-05-2017.
 */

public class LstAssetImageModel implements Serializable {

    public int Company_Id;
    public String DA_Code;
    public String Image_Url;
    public String Image_Name;
    public int Image_Id;

    public String Offline_URL;

    public int position;

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public String getDA_Code() {
        return DA_Code;
    }

    public void setDA_Code(String DA_Code) {
        this.DA_Code = DA_Code;
    }

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }

    public String getImage_Name() {
        return Image_Name;
    }

    public void setImage_Name(String image_Name) {
        Image_Name = image_Name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getOffline_URL() {
        return Offline_URL;
    }

    public void setOffline_URL(String offline_URL) {
        Offline_URL = offline_URL;
    }

    public int getImage_Id() {
        return Image_Id;
    }

    public void setImage_Id(int image_Id) {
        Image_Id = image_Id;
    }
}

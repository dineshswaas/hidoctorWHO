package com.swaas.kangle.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 09-05-2017.
 */

public class AssetImages implements Serializable {
    public String DA_Code;
    public String Is_Downloadable;
    public String DA_Size_In_MB;
    public String Is_Viewable;
    public String Is_Sharable;
    public List<LstAssetImageModel> lstAssetImageModel;

    public String getDA_Code() {
        return DA_Code;
    }

    public void setDA_Code(String DA_Code) {
        this.DA_Code = DA_Code;
    }

    public String getIs_Downloadable() {
        return Is_Downloadable;
    }

    public void setIs_Downloadable(String is_Downloadable) {
        Is_Downloadable = is_Downloadable;
    }

    public String getDA_Size_In_MB() {
        return DA_Size_In_MB;
    }

    public void setDA_Size_In_MB(String DA_Size_In_MB) {
        this.DA_Size_In_MB = DA_Size_In_MB;
    }

    public String getIs_Viewable() {
        return Is_Viewable;
    }

    public void setIs_Viewable(String is_Viewable) {
        Is_Viewable = is_Viewable;
    }

    public String getIs_Sharable() {
        return Is_Sharable;
    }

    public void setIs_Sharable(String is_Sharable) {
        Is_Sharable = is_Sharable;
    }

    public List<LstAssetImageModel> getLstAssetImageModel() {
        return lstAssetImageModel;
    }

    public void setLstAssetImageModel(List<LstAssetImageModel> lstAssetImageModel) {
        this.lstAssetImageModel = lstAssetImageModel;
    }
}

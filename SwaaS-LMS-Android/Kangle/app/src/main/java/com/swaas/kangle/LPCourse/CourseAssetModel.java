package com.swaas.kangle.LPCourse;

import com.swaas.kangle.models.LstAssetImageModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 11-08-2017.
 */

public class CourseAssetModel implements Serializable {

    public String Company_Code;
    public String DA_Code;
    public String Asset_Name;
    public String File_Path;
    public String DA_Thumbnail_URL;
    public String OfflineUrl;
    public List<LstAssetImageModel> lstAssetImage;
    public List<String> lstEncodedUrls;

    //BrightCove properties
    public String DA_Type;
    public String VideoId;
    public String VideoType;
    public String PolicyKey;
    public String Video_Account_Id;
    public String AccountId;
    public String PlayerId;

    public String getDA_Type() {
        return DA_Type;
    }

    public void setDA_Type(String DA_Type) {
        this.DA_Type = DA_Type;
    }

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String videoId) {
        VideoId = videoId;
    }

    public String getVideoType() {
        return VideoType;
    }

    public void setVideoType(String videoType) {
        VideoType = videoType;
    }

    public String getPolicyKey() {
        return PolicyKey;
    }

    public void setPolicyKey(String policyKey) {
        PolicyKey = policyKey;
    }

    public String getVideo_Account_Id() {
        return Video_Account_Id;
    }

    public void setVideo_Account_Id(String video_Account_Id) {
        Video_Account_Id = video_Account_Id;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getPlayerId() {
        return PlayerId;
    }

    public void setPlayerId(String playerId) {
        PlayerId = playerId;
    }

    //BrightCove video properties

    public String getCompany_Code() {
        return Company_Code;
    }

    public void setCompany_Code(String company_Code) {
        Company_Code = company_Code;
    }

    public String getDA_Code() {
        return DA_Code;
    }

    public void setDA_Code(String DA_Code) {
        this.DA_Code = DA_Code;
    }

    public String getAsset_Name() {
        return Asset_Name;
    }

    public void setAsset_Name(String asset_Name) {
        Asset_Name = asset_Name;
    }

    public String getFile_Path() {
        return File_Path;
    }

    public void setFile_Path(String file_Path) {
        File_Path = file_Path;
    }

    public String getDA_Thumbnail_URL() {
        return DA_Thumbnail_URL;
    }

    public void setDA_Thumbnail_URL(String DA_Thumbnail_URL) {
        this.DA_Thumbnail_URL = DA_Thumbnail_URL;
    }

    public String getOfflineUrl() {
        return OfflineUrl;
    }

    public void setOfflineUrl(String offlineUrl) {
        OfflineUrl = offlineUrl;
    }

    public List<LstAssetImageModel> getLstAssetImage() {
        return lstAssetImage;
    }

    public void setLstAssetImage(List<LstAssetImageModel> lstAssetImage) {
        this.lstAssetImage = lstAssetImage;
    }

    public List<String> getLstEncodedUrls() {
        return lstEncodedUrls;
    }

    public void setLstEncodedUrls(List<String> lstEncodedUrls) {
        this.lstEncodedUrls = lstEncodedUrls;
    }
}

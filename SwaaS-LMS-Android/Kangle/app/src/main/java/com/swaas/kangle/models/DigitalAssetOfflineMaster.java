package com.swaas.kangle.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 05-05-2017.
 */

public class DigitalAssetOfflineMaster implements Serializable {

    public String DAID;
    public String DAMode;
    public String offlineURL;
    public String onlineURL;
    public String Tags;
    public String OnlineOutPutId;
    public String OfflineOutPutId;
    public String DocumentType;
    public String IsDownloadable;
    public String IsViewable;
    public String IsSharable;
    public String ThumnailURL;
    public String DAName;
    public String DACategoryCode;
    public String DACategoryName;
    public int TotalViews;
    public int TotalLikes;
    public int TotalDislikes;
    public int TotalRatings;
    public String UDTags;
    public String DA_Description;
    public String DA_Type;
    public int DACategoryUsageCount;
    public String DA_Size_In_MB;
    public String lstAssetImageModel;
    public boolean Is_Read;
    public boolean Is_Downloaded;
    public String Uploaded_Date;
    public String Uploaded_By;
    public List<String> lstEncodedUrls;
    public String Is_Asset_Sharable;
    public String DA_File_Name;
    public int User_Like;
    public int User_Rating;

    public int categoryCount;

    public String getDAID() {
        return DAID;
    }

    public void setDAID(String DAID) {
        this.DAID = DAID;
    }

    public String getDAMode() {
        return DAMode;
    }

    public void setDAMode(String DAMode) {
        this.DAMode = DAMode;
    }

    public String getOfflineURL() {
        return offlineURL;
    }

    public void setOfflineURL(String offlineURL) {
        this.offlineURL = offlineURL;
    }

    public String getOnlineURL() {
        return onlineURL;
    }

    public void setOnlineURL(String onlineURL) {
        this.onlineURL = onlineURL;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getOnlineOutPutId() {
        return OnlineOutPutId;
    }

    public void setOnlineOutPutId(String onlineOutPutId) {
        OnlineOutPutId = onlineOutPutId;
    }

    public String getOfflineOutPutId() {
        return OfflineOutPutId;
    }

    public void setOfflineOutPutId(String offlineOutPutId) {
        OfflineOutPutId = offlineOutPutId;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }

    public String getIsDownloadable() {
        return IsDownloadable;
    }

    public void setIsDownloadable(String isDownloadable) {
        IsDownloadable = isDownloadable;
    }

    public String getIsViewable() {
        return IsViewable;
    }

    public void setIsViewable(String isViewable) {
        IsViewable = isViewable;
    }

    public String getIsSharable() {
        return IsSharable;
    }

    public void setIsSharable(String isSharable) {
        IsSharable = isSharable;
    }

    public String getThumnailURL() {
        return ThumnailURL;
    }

    public void setThumnailURL(String thumnailURL) {
        ThumnailURL = thumnailURL;
    }

    public String getDAName() {
        return DAName;
    }

    public void setDAName(String DAName) {
        this.DAName = DAName;
    }

    public String getDACategoryCode() {
        return DACategoryCode;
    }

    public void setDACategoryCode(String DACategoryCode) {
        this.DACategoryCode = DACategoryCode;
    }

    public String getDACategoryName() {
        return DACategoryName;
    }

    public void setDACategoryName(String DACategoryName) {
        this.DACategoryName = DACategoryName;
    }

    public int getTotalViews() {
        return TotalViews;
    }

    public void setTotalViews(int totalViews) {
        TotalViews = totalViews;
    }

    public int getTotalLikes() {
        return TotalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        TotalLikes = totalLikes;
    }

    public int getTotalDislikes() {
        return TotalDislikes;
    }

    public void setTotalDislikes(int totalDislikes) {
        TotalDislikes = totalDislikes;
    }

    public int getTotalRatings() {
        return TotalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        TotalRatings = totalRatings;
    }

    public String getUDTags() {
        return UDTags;
    }

    public void setUDTags(String UDTags) {
        this.UDTags = UDTags;
    }

    public String getDA_Description() {
        return DA_Description;
    }

    public void setDA_Description(String DA_Description) {
        this.DA_Description = DA_Description;
    }

    public String getDA_Type() {
        return DA_Type;
    }

    public void setDA_Type(String DA_Type) {
        this.DA_Type = DA_Type;
    }

    public int getDACategoryUsageCount() {
        return DACategoryUsageCount;
    }

    public void setDACategoryUsageCount(int DACategoryUsageCount) {
        this.DACategoryUsageCount = DACategoryUsageCount;
    }

    public String getDA_Size_In_MB() {
        return DA_Size_In_MB;
    }

    public void setDA_Size_In_MB(String DA_Size_In_MB) {
        this.DA_Size_In_MB = DA_Size_In_MB;
    }

    public String getLstAssetImageModel() {
        return lstAssetImageModel;
    }

    public void setLstAssetImageModel(String lstAssetImageModel) {
        this.lstAssetImageModel = lstAssetImageModel;
    }

    public boolean is_Read() {
        return Is_Read;
    }

    public void setIs_Read(boolean is_Read) {
        Is_Read = is_Read;
    }

    public boolean is_Downloaded() {
        return Is_Downloaded;
    }

    public void setIs_Downloaded(boolean is_Downloaded) {
        Is_Downloaded = is_Downloaded;
    }

    public String getUploaded_Date() {
        return Uploaded_Date;
    }

    public void setUploaded_Date(String uploaded_Date) {
        Uploaded_Date = uploaded_Date;
    }

    public String getUploaded_By() {
        return Uploaded_By;
    }

    public void setUploaded_By(String uploaded_By) {
        Uploaded_By = uploaded_By;
    }

    public List<String> getLstEncodedUrls() {
        return lstEncodedUrls;
    }

    public void setLstEncodedUrls(List<String> lstEncodedUrls) {
        this.lstEncodedUrls = lstEncodedUrls;
    }

    public String getIs_Asset_Sharable() {
        return Is_Asset_Sharable;
    }

    public void setIs_Asset_Sharable(String is_Asset_Sharable) {
        Is_Asset_Sharable = is_Asset_Sharable;
    }

    public String getDA_File_Name() {
        return DA_File_Name;
    }

    public void setDA_File_Name(String DA_File_Name) {
        this.DA_File_Name = DA_File_Name;
    }

    public int getUser_Like() {
        return User_Like;
    }

    public void setUser_Like(int user_Like) {
        User_Like = user_Like;
    }

    public int getUser_Rating() {
        return User_Rating;
    }

    public void setUser_Rating(int user_Rating) {
        User_Rating = user_Rating;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

}

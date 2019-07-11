package com.swaas.kangle.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 02-05-2017.
 */

public class DigitalAssetTransactionMaster implements Serializable {

    public int SlNo;
    public String DAID;
    public long playtime;
    public int User_Like;
    public int User_Rating;
    public boolean Is_Read;
    public boolean Is_Downloaded;
    public int TotalViews;
    public int TotalLikes;
    public int TotalDislikes;
    public int TotalRatings;
    public String DAName;
    public String UDTags;
    public String offlineURL;
    public String onlineURL;
    public String Tags;
    public String DACategoryCode;
    public String DACategoryName;

    public String DA_Description;
    public String DA_Type;
    public String DocumentType;

    public int isSynced;
    public String Recorddate;

    public String OfflinePlay;
    public String OnlinePlay;
    public String Longitude;
    public String Lattitude;

    public String getOfflinePlay() {
        return OfflinePlay;
    }

    public void setOfflinePlay(String offlinePlay) {
        OfflinePlay = offlinePlay;
    }

    public String getOnlinePlay() {
        return OnlinePlay;
    }

    public void setOnlinePlay(String onlinePlay) {
        OnlinePlay = onlinePlay;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }

    public int getSlNo() {
        return SlNo;
    }

    public void setSlNo(int slNo) {
        SlNo = slNo;
    }

    public String getDAID() {
        return DAID;
    }

    public void setDAID(String DAID) {
        this.DAID = DAID;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getRecorddate() {
        return Recorddate;
    }

    public void setRecorddate(String recorddate) {
        Recorddate = recorddate;
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

    public String getDAName() {
        return DAName;
    }

    public void setDAName(String DAName) {
        this.DAName = DAName;
    }

    public String getUDTags() {
        return UDTags;
    }

    public void setUDTags(String UDTags) {
        this.UDTags = UDTags;
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

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }
}

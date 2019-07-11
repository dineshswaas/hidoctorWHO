package com.swaas.kangle.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 02-05-2017.
 */

public class DigitalAssetTransactionChild implements Serializable {

    public int SlNo;
    public String DAID;
    public String DAName;
    public int SlideNo;
    public long playtime;
    public int Liked;
    public boolean Read;
    public int isSynced;
    public String RecordDate;

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

    public String getDAName() {
        return DAName;
    }

    public void setDAName(String DAName) {
        this.DAName = DAName;
    }

    public int getSlideNo() {
        return SlideNo;
    }

    public void setSlideNo(int slideNo) {
        SlideNo = slideNo;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public int getLiked() {
        return Liked;
    }

    public void setLiked(int liked) {
        Liked = liked;
    }

    public boolean isRead() {
        return Read;
    }

    public void setRead(boolean read) {
        Read = read;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getRecordDate() {
        return RecordDate;
    }

    public void setRecordDate(String recordDate) {
        RecordDate = recordDate;
    }
}

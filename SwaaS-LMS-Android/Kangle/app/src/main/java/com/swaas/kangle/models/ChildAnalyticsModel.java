package com.swaas.kangle.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 22-06-2017.
 */

public class ChildAnalyticsModel implements Serializable {

    public int Company_Id;
    public int Asset_Id;
    public int Image_Id;
    public double Duration;
    public int Viewed_By;
    public String Local_Time_Zone;

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getAsset_Id() {
        return Asset_Id;
    }

    public void setAsset_Id(int asset_Id) {
        Asset_Id = asset_Id;
    }

    public int getImage_Id() {
        return Image_Id;
    }

    public void setImage_Id(int image_Id) {
        Image_Id = image_Id;
    }

    public double getDuration() {
        return Duration;
    }

    public void setDuration(double duration) {
        Duration = duration;
    }

    public int getViewed_By() {
        return Viewed_By;
    }

    public void setViewed_By(int viewed_By) {
        Viewed_By = viewed_By;
    }

    public String getLocal_Time_Zone() {
        return Local_Time_Zone;
    }

    public void setLocal_Time_Zone(String local_Time_Zone) {
        Local_Time_Zone = local_Time_Zone;
    }
}

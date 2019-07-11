package com.swaas.kangle.API.model;

import java.io.Serializable;

public class AppInfo implements Serializable {

    private String Version;
    private String Release;
    private int isUpgradeRequired;


    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        this.Version = version;
    }

    public String getRelease() {
        return Release;
    }

    public void setRelease(String release) {
        this.Release = release;
    }

    public int isUpgradeRequired() {
        return isUpgradeRequired;
    }

    public void setUpgradeRequired(int upgradeRequired) {
        isUpgradeRequired = upgradeRequired;
    }
}
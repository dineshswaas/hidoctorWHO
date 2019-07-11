package com.swaas.kangle.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 19-06-2017.
 */

public class UserTrackingModel implements Serializable {

    public int SlNo;
    public String CompanyId;
    public String UserId;
    public String RegionCode;
    public String Module;
    public String DeviceType;
    public String DeviceModel;
    public String AppVersion;
    public String Device_OS_Type;
    public String Browser;
    public String OSBrowserVersion;
    public String OSVersion;
    public String UserAnonymous;
    public String OtherData1;
    public String OtherData2;
    public String longitude;
    public String lattitude;
    public String Address;

    public int isSynced;

    public int getSlNo() {
        return SlNo;
    }

    public void setSlNo(int slNo) {
        SlNo = slNo;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(String regionCode) {
        RegionCode = regionCode;
    }

    public String getModule() {
        return Module;
    }

    public void setModule(String module) {
        Module = module;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getDeviceModel() {
        return DeviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        DeviceModel = deviceModel;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public String getDevice_OS_Type() {
        return Device_OS_Type;
    }

    public void setDevice_OS_Type(String device_OS_Type) {
        Device_OS_Type = device_OS_Type;
    }

    public String getBrowser() {
        return Browser;
    }

    public void setBrowser(String browser) {
        Browser = browser;
    }

    public String getOSBrowserVersion() {
        return OSBrowserVersion;
    }

    public void setOSBrowserVersion(String OSBrowserVersion) {
        this.OSBrowserVersion = OSBrowserVersion;
    }

    public String getOSVersion() {
        return OSVersion;
    }

    public void setOSVersion(String OSVersion) {
        this.OSVersion = OSVersion;
    }

    public String getUserAnonymous() {
        return UserAnonymous;
    }

    public void setUserAnonymous(String userAnonymous) {
        UserAnonymous = userAnonymous;
    }

    public String getOtherData1() {
        return OtherData1;
    }

    public void setOtherData1(String otherData1) {
        OtherData1 = otherData1;
    }

    public String getOtherData2() {
        return OtherData2;
    }

    public void setOtherData2(String otherData2) {
        OtherData2 = otherData2;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }
}

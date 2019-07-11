package com.swaas.kangle.LPCourse;

import java.io.Serializable;

/**
 * Created by Sayellessh on 17-09-2018.
 */

public class CourseExtendModel implements Serializable {

    public int companyId;
    public String CourseUserMappingIds;
    public int CourseId;
    public int ExtendDays;
    public String localTimeZone;
    public String offsetValue;
    public String sectionUserMappingIds;
    public int extentAttemptCount;
    public int User_Id;
    public String subdomainName;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCourseUserMappingIds() {
        return CourseUserMappingIds;
    }

    public void setCourseUserMappingIds(String courseUserMappingIds) {
        CourseUserMappingIds = courseUserMappingIds;
    }

    public int getCourseId() {
        return CourseId;
    }

    public void setCourseId(int courseId) {
        CourseId = courseId;
    }

    public int getExtendDays() {
        return ExtendDays;
    }

    public void setExtendDays(int extendDays) {
        ExtendDays = extendDays;
    }

    public String getLocalTimeZone() {
        return localTimeZone;
    }

    public void setLocalTimeZone(String localTimeZone) {
        this.localTimeZone = localTimeZone;
    }

    public String getOffsetValue() {
        return offsetValue;
    }

    public void setOffsetValue(String offsetValue) {
        this.offsetValue = offsetValue;
    }

    public String getSectionUserMappingIds() {
        return sectionUserMappingIds;
    }

    public void setSectionUserMappingIds(String sectionUserMappingIds) {
        this.sectionUserMappingIds = sectionUserMappingIds;
    }

    public int getExtentAttemptCount() {
        return extentAttemptCount;
    }

    public void setExtentAttemptCount(int extentAttemptCount) {
        this.extentAttemptCount = extentAttemptCount;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public String getSubdomainName() {
        return subdomainName;
    }

    public void setSubdomainName(String subdomainName) {
        this.subdomainName = subdomainName;
    }
}

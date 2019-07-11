package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hariharan on 24/8/17.
 */

public class AnwerUploadModel implements Serializable {

    public String lstCourseUserAnswers;
    public String lstCourseUserAssessDet;
    public String lstCourseUserAssessHeader;

    public String getLstCourseUserAnswers() {
        return lstCourseUserAnswers;
    }

    public void setLstCourseUserAnswers(String lstCourseUserAnswers) {
        this.lstCourseUserAnswers = lstCourseUserAnswers;
    }

    public String getLstCourseUserAssessDet() {
        return lstCourseUserAssessDet;
    }

    public void setLstCourseUserAssessDet(String lstCourseUserAssessDet) {
        this.lstCourseUserAssessDet = lstCourseUserAssessDet;
    }

    public String getLstCourseUserAssessHeader() {
        return lstCourseUserAssessHeader;
    }

    public void setLstCourseUserAssessHeader(String lstCourseUserAssessHeader) {
        this.lstCourseUserAssessHeader = lstCourseUserAssessHeader;
    }
}

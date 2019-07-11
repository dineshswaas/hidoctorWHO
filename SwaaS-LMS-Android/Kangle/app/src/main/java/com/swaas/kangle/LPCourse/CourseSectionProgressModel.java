package com.swaas.kangle.LPCourse;

import java.io.Serializable;

/**
 * Created by Sayellessh on 20-08-2018.
 */

public class CourseSectionProgressModel implements Serializable {

    public int Course_Id;
    public int Section_Id;
    public String Section_Status_Value;

    public int getCourse_Id() {
        return Course_Id;
    }

    public void setCourse_Id(int course_Id) {
        Course_Id = course_Id;
    }

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }

    public String getSection_Status_Value() {
        return Section_Status_Value;
    }

    public void setSection_Status_Value(String section_Status_Value) {
        Section_Status_Value = section_Status_Value;
    }
}

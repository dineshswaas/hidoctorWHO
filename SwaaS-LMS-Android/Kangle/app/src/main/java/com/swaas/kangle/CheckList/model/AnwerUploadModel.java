package com.swaas.kangle.CheckList.model;

import com.swaas.kangle.TaskModule.TaskListModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 01-05-2018.
 */

public class AnwerUploadModel implements Serializable {

    public List<CourseUserAnswers> lstChecklistUserAnswersObj;
    public List<CourseUserAssessDet> lstChecklistUserAssessDeObjt;
    public List<CourseUserAssessHeader> lstChecklistUserAssessHeaderObj;
    public List<UserforCourseChecklist> userlistsobj;
    public List<TaskListModel> lstTaskCreatelist;

    public List<CourseUserAnswers> getLstChecklistUserAnswers() {
        return lstChecklistUserAnswersObj;
    }

    public void setLstChecklistUserAnswers(List<CourseUserAnswers> lstChecklistUserAnswers) {
        this.lstChecklistUserAnswersObj = lstChecklistUserAnswers;
    }

    public List<CourseUserAssessDet> getLstChecklistUserAssessDet() {
        return lstChecklistUserAssessDeObjt;
    }

    public void setLstChecklistUserAssessDet(List<CourseUserAssessDet> lstChecklistUserAssessDet) {
        this.lstChecklistUserAssessDeObjt = lstChecklistUserAssessDet;
    }

    public List<CourseUserAssessHeader> getLstChecklistUserAssessHeader() {
        return lstChecklistUserAssessHeaderObj;
    }

    public void setLstChecklistUserAssessHeader(List<CourseUserAssessHeader> lstChecklistUserAssessHeader) {
        this.lstChecklistUserAssessHeaderObj = lstChecklistUserAssessHeader;
    }

    public List<UserforCourseChecklist> getUserlistsobj() {
        return userlistsobj;
    }

    public void setUserlistsobj(List<UserforCourseChecklist> userlistsobj) {
        this.userlistsobj = userlistsobj;
    }

    public List<TaskListModel> getTaskCreatelist() {
        return lstTaskCreatelist;
    }

    public void setTaskCreatelist(List<TaskListModel> taskCreatelist) {
        lstTaskCreatelist = taskCreatelist;
    }
}

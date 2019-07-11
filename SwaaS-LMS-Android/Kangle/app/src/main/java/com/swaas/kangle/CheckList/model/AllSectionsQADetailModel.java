package com.swaas.kangle.CheckList.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 03-05-2018.
 */

public class AllSectionsQADetailModel implements Serializable{

    public int SectionId;
    public String SectionName;
    public int ChecklistId;
    public String ChecklistName;
    public List<ReportQuestionAnsersList> lstQuestions;
    public List<String> lstAttachments;
    public String Acknowledge_Comments;
    public String Submitted_By;
    public String Submitted_Date;
    public String On_Behalf;
    public int Attempt_Number;


    public int getSectionId() {
        return SectionId;
    }

    public void setSectionId(int sectionId) {
        SectionId = sectionId;
    }

    public String getSectionName() {
        return SectionName;
    }

    public void setSectionName(String sectionName) {
        SectionName = sectionName;
    }

    public int getChecklistId() {
        return ChecklistId;
    }

    public void setChecklistId(int checklistId) {
        ChecklistId = checklistId;
    }

    public String getChecklistName() {
        return ChecklistName;
    }

    public void setChecklistName(String checklistName) {
        ChecklistName = checklistName;
    }

    public List<ReportQuestionAnsersList> getLstQuestions() {
        return lstQuestions;
    }

    public void setLstQuestions(List<ReportQuestionAnsersList> lstQuestions) {
        this.lstQuestions = lstQuestions;
    }

    public List<String> getLstAttachments() {
        return lstAttachments;
    }

    public void setLstAttachments(List<String> lstAttachments) {
        this.lstAttachments = lstAttachments;
    }

    public String getAcknowledge_Comments() {
        return Acknowledge_Comments;
    }

    public void setAcknowledge_Comments(String acknowledge_Comments) {
        Acknowledge_Comments = acknowledge_Comments;
    }

    public String getSubmitted_By() {
        return Submitted_By;
    }

    public void setSubmitted_By(String submitted_By) {
        Submitted_By = submitted_By;
    }

    public String getSubmitted_Date() {
        return Submitted_Date;
    }

    public void setSubmitted_Date(String submitted_Date) {
        Submitted_Date = submitted_Date;
    }

    public String getOn_Behalf() {
        return On_Behalf;
    }

    public void setOn_Behalf(String on_Behalf) {
        On_Behalf = on_Behalf;
    }

    public int getAttempt_Number() {
        return Attempt_Number;
    }

    public void setAttempt_Number(int attempt_Number) {
        Attempt_Number = attempt_Number;
    }
}

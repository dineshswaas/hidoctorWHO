package com.swaas.kangle.CheckList.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 16-05-2018.
 */

public class ReportQuestionAnsersList implements Serializable {

    public int QuestionId;
    public String QuestionText;
    public int ChecklistId;
    public String ChecklistName;
    public int SectionId;
    public List<ReportAnswersList> lstAnswers;
    public List<String> lstAttachments;
    public String Question_Remarks;

    public String Question_Number_Title;


    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getQuestionText() {
        return QuestionText;
    }

    public void setQuestionText(String questionText) {
        QuestionText = questionText;
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

    public int getSectionId() {
        return SectionId;
    }

    public void setSectionId(int sectionId) {
        SectionId = sectionId;
    }

    public List<ReportAnswersList> getLstAnswers() {
        return lstAnswers;
    }

    public void setLstAnswers(List<ReportAnswersList> lstAnswers) {
        this.lstAnswers = lstAnswers;
    }

    public String getQuestion_Remarks() {
        return Question_Remarks;
    }

    public void setQuestion_Remarks(String question_Remarks) {
        Question_Remarks = question_Remarks;
    }

    public List<String> getLstAttachments() {
        return lstAttachments;
    }

    public void setLstAttachments(List<String> lstAttachments) {
        this.lstAttachments = lstAttachments;
    }

    public String getQuestion_Number_Title() {
        return Question_Number_Title;
    }

    public void setQuestion_Number_Title(String question_Number_Title) {
        Question_Number_Title = question_Number_Title;
    }
}

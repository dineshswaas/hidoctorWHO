package com.swaas.kangle.CheckList.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 16-05-2018.
 */

public class ReportAnswersList implements Serializable {

    public int AnswerId;
    public String AnswerText;
    public int QuestionId;
    public int ChecklistId;
    public int SectionId;

    public int getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(int answerId) {
        AnswerId = answerId;
    }

    public String getAnswerText() {
        return AnswerText;
    }

    public void setAnswerText(String answerText) {
        AnswerText = answerText;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public int getChecklistId() {
        return ChecklistId;
    }

    public void setChecklistId(int checklistId) {
        ChecklistId = checklistId;
    }

    public int getSectionId() {
        return SectionId;
    }

    public void setSectionId(int sectionId) {
        SectionId = sectionId;
    }
}

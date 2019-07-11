package com.swaas.kangle.CheckList.model;

import java.util.List;

/**
 * Created by Sayellessh on 03-05-2018.
 */

public class QuestionBaseModel {

    public List<QuestionCourseListModel> lstChecklist;
    public  List<QuestionQuestionListModel> lstQuestion;
    public  List<QuestionAnswerListModel> lstAnswer;

    public List<QuestionCourseListModel> getLstCourse() {
        return lstChecklist;
    }

    public void setLstCourse(List<QuestionCourseListModel> lstCourse) {
        this.lstChecklist = lstCourse;
    }

    public List<QuestionQuestionListModel> getLstQuestion() {
        return lstQuestion;
    }

    public void setLstQuestion(List<QuestionQuestionListModel> lstQuestion) {
        this.lstQuestion = lstQuestion;
    }

    public List<QuestionAnswerListModel> getLstAnswer() {
        return lstAnswer;
    }

    public void setLstAnswer(List<QuestionAnswerListModel> lstAnswer) {
        this.lstAnswer = lstAnswer;
    }
    public String lstAttempts;

}

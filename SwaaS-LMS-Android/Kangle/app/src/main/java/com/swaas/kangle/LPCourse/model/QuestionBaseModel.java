package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hariharan on 21/8/17.
 */

public class QuestionBaseModel {

    public List<QuestionCourseListModel> lstCourse;
    public  List<QuestionQuestionListModel> lstQuestion;
    public  List<QuestionAnswerListModel> lstAnswer;
    public List<lstRandom> lstRandom;
    public List<lstMatchingQA> lstMatchingQA;
    public List<QuestionCourseListModel> getLstCourse() {
        return lstCourse;
    }

    public void setLstCourse(List<QuestionCourseListModel> lstCourse) {
        this.lstCourse = lstCourse;
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

    public List<com.swaas.kangle.LPCourse.model.lstRandom> getLstRandom() {
        return lstRandom;
    }

    public void setLstRandom(List<com.swaas.kangle.LPCourse.model.lstRandom> lstRandom) {
        this.lstRandom = lstRandom;
    }

    public List<com.swaas.kangle.LPCourse.model.lstMatchingQA> getLstMatchingQA() {
        return lstMatchingQA;
    }

    public void setLstMatchingQA(List<com.swaas.kangle.LPCourse.model.lstMatchingQA> lstMatchingQA) {
        this.lstMatchingQA = lstMatchingQA;
    }
}

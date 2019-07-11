package com.swaas.kangle.CheckList.model;

import com.swaas.kangle.LPCourse.model.lstMatchingQA;
import com.swaas.kangle.LPCourse.model.lstRandom;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 01-05-2018.
 */

public class QuestionAndAnswerModel implements Serializable {

    public QuestionQuestionListModel questionModel;

    public List<QuestionCourseListModel> lstCourse;

    public  List<QuestionAnswerListModel> lstAnswer;

    public String ChoosenAnswer;

    public String CorrectAnswer;

    public String ChoosenAnswerId;

    public String CorrectAnswerId;


    public int SectionMapId;

    public int CourseAssignmentId;

    public int SectionId;

    public String commentText;

    public String attachmentURL;

    public List<com.swaas.kangle.LPCourse.model.lstRandom> lstRandom;
    public List<com.swaas.kangle.LPCourse.model.lstMatchingQA> lstMatchingQA;
    public String getAttachmentURL() {
        return attachmentURL;
    }

    public void setAttachmentURL(String attachmentURL) {
        this.attachmentURL = attachmentURL;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getSectionId() {
        return SectionId;
    }

    public void setSectionId(int sectionId) {
        SectionId = sectionId;
    }

    public int getSectionMapId() {
        return SectionMapId;
    }

    public void setSectionMapId(int sectionMapId) {
        SectionMapId = sectionMapId;
    }

    public int getCourseAssignmentId() {
        return CourseAssignmentId;
    }

    public void setCourseAssignmentId(int courseAssignmentId) {
        CourseAssignmentId = courseAssignmentId;
    }


    public String getChoosenAnswerId() {
        return ChoosenAnswerId;
    }

    public void setChoosenAnswerId(String choosenAnswerId) {
        ChoosenAnswerId = choosenAnswerId;
    }

    public String getCorrectAnswerId() {
        return CorrectAnswerId;
    }

    public void setCorrectAnswerId(String correctAnswerId) {
        CorrectAnswerId = correctAnswerId;
    }

    public String getChoosenAnswer() {
        return ChoosenAnswer;
    }

    public void setChoosenAnswer(String choosenAnswer) {
        ChoosenAnswer = choosenAnswer;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public List<QuestionAnswerListModel> getLstAnswer() {
        return lstAnswer;
    }

    public void setLstAnswer(List<QuestionAnswerListModel> lstAnswer) {
        this.lstAnswer = lstAnswer;
    }

    public QuestionQuestionListModel getQuestionModel() {
        return questionModel;
    }

    public void setQuestionModel(QuestionQuestionListModel questionModel) {
        this.questionModel = questionModel;
    }

    public List<QuestionCourseListModel> getLstCourse() {
        return lstCourse;
    }

    public void setLstCourse(List<QuestionCourseListModel> lstCourse) {
        this.lstCourse = lstCourse;
    }

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

package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

public class lstMatchingQA implements Serializable {

    public int company_id;
    public int Course_Id;
    public int Section_Id;
    public int question_id;
    public int question_type;
    public int User_Exam_Id;
    public String SubQuestiontext;
    public String SubAnswertext;
    public int user_id;


    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

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

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(int question_type) {
        this.question_type = question_type;
    }

    public int getUser_Exam_Id() {
        return User_Exam_Id;
    }

    public void setUser_Exam_Id(int user_Exam_Id) {
        User_Exam_Id = user_Exam_Id;
    }

    public String getSubQuestiontext() {
        return SubQuestiontext;
    }

    public void setSubQuestiontext(String subQuestiontext) {
        SubQuestiontext = subQuestiontext;
    }

    public String getSubAnswertext() {
        return SubAnswertext;
    }

    public void setSubAnswertext(String subAnswertext) {
        SubAnswertext = subAnswertext;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

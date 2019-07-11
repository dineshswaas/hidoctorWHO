package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

public class lstRandom implements Serializable {

    public int company_id;
    public int Course_Id;
    public int Section_Id;
    public int question_id;
    public int question_type;
    public String RandomAnswertext;
    public String ChoosenAnswer;

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

    public String getRandomAnswertext() {
        return RandomAnswertext;
    }

    public void setRandomAnswertext(String randomAnswertext) {
        RandomAnswertext = randomAnswertext;
    }

    public String getChoosenAnswer() {
        return ChoosenAnswer;
    }

    public void setChoosenAnswer(String choosenAnswer) {
        ChoosenAnswer = choosenAnswer;
    }
}

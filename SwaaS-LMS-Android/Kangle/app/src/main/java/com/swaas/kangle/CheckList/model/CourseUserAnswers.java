package com.swaas.kangle.CheckList.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 01-05-2018.
 */

public class CourseUserAnswers implements Serializable {

    public String Company_Id;

    public int Checklist_Section_User_Exam_Id;

    public int User_Id;

    public String User_Answer_Text;

    public String Question_Id;

    public String Answer_Id;

    public int Section_Id;
    public int Checklist_Id;

    public String getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(String company_Id) {
        Company_Id = company_Id;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public String getQuestion_Id() {
        return Question_Id;
    }

    public void setQuestion_Id(String question_Id) {
        Question_Id = question_Id;
    }

    public String getAnswer_Id() {
        return Answer_Id;
    }

    public void setAnswer_Id(String answer_Id) {
        Answer_Id = answer_Id;
    }


    /*public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }*/

    public String getUser_Answer_Text() {
        return User_Answer_Text;
    }

    public void setUser_Answer_Text(String user_Answer_Text) {
        User_Answer_Text = user_Answer_Text;
    }

    public int getChecklist_Section_User_Exam_Id() {
        return Checklist_Section_User_Exam_Id;
    }

    public void setChecklist_Section_User_Exam_Id(int checklist_Section_User_Exam_Id) {
        Checklist_Section_User_Exam_Id = checklist_Section_User_Exam_Id;
    }

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }

    public int getChecklist_Id() {
        return Checklist_Id;
    }

    public void setChecklist_Id(int checklist_Id) {
        Checklist_Id = checklist_Id;
    }
}

package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

/**
 * Created by Hariharan on 24/8/17.
 */

public class CourseUserAnswers implements Serializable {

    public String Company_Id;

    public int User_Id;

    public String Text;

    public String Question_Id;

    public String Answer_Id;

    public String User_Answer_Text;

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

    public String getUser_Answer_Text() {
        return User_Answer_Text;
    }

    public void setUser_Answer_Text(String user_Answer_Text) {
        User_Answer_Text = user_Answer_Text;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

}

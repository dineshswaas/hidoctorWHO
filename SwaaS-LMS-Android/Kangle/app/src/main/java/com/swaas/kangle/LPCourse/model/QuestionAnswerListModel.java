package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

/**
 * Created by Hariharan on 21/8/17.
 */

public class QuestionAnswerListModel implements Serializable {


    public int Company_Id;
    public int Course_Mapper_ID;
    public int Course_Id;
    public int Question_Id;
    public int Answer_Id;
    public String Answer_Text;
    public int Display_Order;
    public int Is_Correct_Answer;
    public int Created_By;
    public String Created_Date;
    public int Modified_By;
    public String Modified_Date;
    public int Publish_Id;
    public int Question_Type;
    public int Section_Id;
    public boolean IsActive;
    public String IncludeNA;

    public boolean chosenRadioanswer;

    public int chosenbooleanAnswer;

    public boolean choosencheckboxAnswer;

    public int choosensliderAnswer;
    public boolean choosensliderAnswerflag;

    public String Answer_Type;
    public boolean Attachment_Allowed;
    public String Answer_Colour;
    public String Answer_Label;

    public int Is_Draft_Answer_Id;
    public String Is_Draft_Answer_Text;

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getCourse_Mapper_ID() {
        return Course_Mapper_ID;
    }

    public void setCourse_Mapper_ID(int course_Mapper_ID) {
        Course_Mapper_ID = course_Mapper_ID;
    }

    public int getCourse_Id() {
        return Course_Id;
    }

    public void setCourse_Id(int course_Id) {
        Course_Id = course_Id;
    }

    public int getQuestion_Id() {
        return Question_Id;
    }

    public void setQuestion_Id(int question_Id) {
        Question_Id = question_Id;
    }

    public int getAnswer_Id() {
        return Answer_Id;
    }

    public void setAnswer_Id(int answer_Id) {
        Answer_Id = answer_Id;
    }

    public String getAnswer_Text() {
        return Answer_Text;
    }

    public void setAnswer_Text(String answer_Text) {
        Answer_Text = answer_Text;
    }

    public int getDisplay_Order() {
        return Display_Order;
    }

    public void setDisplay_Order(int display_Order) {
        Display_Order = display_Order;
    }

    public int getIs_Correct_Answer() {
        return Is_Correct_Answer;
    }

    public void setIs_Correct_Answer(int is_Correct_Answer) {
        Is_Correct_Answer = is_Correct_Answer;
    }

    public int getCreated_By() {
        return Created_By;
    }

    public void setCreated_By(int created_By) {
        Created_By = created_By;
    }

    public String getCreated_Date() {
        return Created_Date;
    }

    public void setCreated_Date(String created_Date) {
        Created_Date = created_Date;
    }

    public int getModified_By() {
        return Modified_By;
    }

    public void setModified_By(int modified_By) {
        Modified_By = modified_By;
    }

    public String getModified_Date() {
        return Modified_Date;
    }

    public void setModified_Date(String modified_Date) {
        Modified_Date = modified_Date;
    }

    public int getPublish_Id() {
        return Publish_Id;
    }

    public void setPublish_Id(int publish_Id) {
        Publish_Id = publish_Id;
    }

    public int getQuestion_Type() {
        return Question_Type;
    }

    public void setQuestion_Type(int question_Type) {
        Question_Type = question_Type;
    }

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public boolean isChosenRadioanswer() {
        return chosenRadioanswer;
    }

    public void setChosenRadioanswer(boolean chosenRadioanswer) {
        this.chosenRadioanswer = chosenRadioanswer;
    }

    public String getIncludeNA() {
        return IncludeNA;
    }

    public void setIncludeNA(String includeNA) {
        IncludeNA = includeNA;
    }

    public int isChosenbooleanAnswer() {
        return chosenbooleanAnswer;
    }

    public void setChosenbooleanAnswer(int chosenbooleanAnswer) {
        this.chosenbooleanAnswer = chosenbooleanAnswer;
    }

    public boolean isChoosencheckboxAnswer() {
        return choosencheckboxAnswer;
    }

    public void setChoosencheckboxAnswer(boolean choosencheckboxAnswer) {
        this.choosencheckboxAnswer = choosencheckboxAnswer;
    }

    public int getChoosensliderAnswer() {
        return choosensliderAnswer;
    }

    public void setChoosensliderAnswer(int choosensliderAnswer) {
        this.choosensliderAnswer = choosensliderAnswer;
    }

    public boolean isChoosensliderAnswerflag() {
        return choosensliderAnswerflag;
    }

    public void setChoosensliderAnswerflag(boolean choosensliderAnswerflag) {
        this.choosensliderAnswerflag = choosensliderAnswerflag;
    }

    public String getAnswer_Type() {
        return Answer_Type;
    }

    public void setAnswer_Type(String answer_Type) {
        Answer_Type = answer_Type;
    }

    public boolean isAttachment_Allowed() {
        return Attachment_Allowed;
    }

    public void setAttachment_Allowed(boolean attachment_Allowed) {
        Attachment_Allowed = attachment_Allowed;
    }

    public String getAnswer_Colour() {
        return Answer_Colour;
    }

    public void setAnswer_Colour(String answer_Colour) {
        Answer_Colour = answer_Colour;
    }

    public String getAnswer_Label() {
        return Answer_Label;
    }

    public void setAnswer_Label(String answer_Label) {
        Answer_Label = answer_Label;
    }

    public int getIs_Draft_Answer_Id() {
        return Is_Draft_Answer_Id;
    }

    public void setIs_Draft_Answer_Id(int is_Draft_Answer_Id) {
        Is_Draft_Answer_Id = is_Draft_Answer_Id;
    }

    public String getIs_Draft_Answer_Text() {
        return Is_Draft_Answer_Text;
    }

    public void setIs_Draft_Answer_Text(String is_Draft_Answer_Text) {
        Is_Draft_Answer_Text = is_Draft_Answer_Text;
    }
}

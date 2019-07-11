package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

/**
 * Created by Hariharan on 21/8/17.
 */

public class QuestionQuestionListModel implements Serializable {

       public int Company_Id;
       public int Course_Id;
       public int Question_Id;
       public int Section_Id;
       public String Question_Text;
       public String Question_Description;
       public String Question_Image_Url;
       public int Question_Type;
       public int Is_Required;
       public String Question_Hint;
       public int Is_Hint_Available;
       public int No_Of_Correct_Answers;
       public int Display_Order;
       public int Created_By;
       public String Created_Date;
       public int Modified_By;
       public String Modified_Date;
       public int Publish_ID;
       public String Correct_Answer_Justification;
       public int Is_Prep_Test;
       public boolean IsMandatory;
       public boolean IsNegative;
       public float Negative_Mark;
       public boolean IsActive;
       public int Count_Of_Correct_Answers;
       public String Explanation;
       public String Hint;

    public String Min_Range_Title;
    public String Max_Range_Title;

    public String Question_Number_Title;

    public boolean Attachment_Allowed;
    public String Answer_Colour;
    public boolean Show_Scaling_Number;
    public boolean Is_Remark_Allowed;

    public String Draft_Remarks;
    public String Draft_Remark_Url;

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
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

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }

    public String getQuestion_Text() {
        return Question_Text;
    }

    public void setQuestion_Text(String question_Text) {
        Question_Text = question_Text;
    }

    public String getQuestion_Description() {
        return Question_Description;
    }

    public void setQuestion_Description(String question_Description) {
        Question_Description = question_Description;
    }

    public String getQuestion_Image_Url() {
        return Question_Image_Url;
    }

    public void setQuestion_Image_Url(String question_Image_Url) {
        Question_Image_Url = question_Image_Url;
    }

    public int getQuestion_Type() {
        return Question_Type;
    }

    public void setQuestion_Type(int question_Type) {
        Question_Type = question_Type;
    }

    public int getIs_Required() {
        return Is_Required;
    }

    public void setIs_Required(int is_Required) {
        Is_Required = is_Required;
    }

    public String getQuestion_Hint() {
        return Question_Hint;
    }

    public void setQuestion_Hint(String question_Hint) {
        Question_Hint = question_Hint;
    }

    public int getIs_Hint_Available() {
        return Is_Hint_Available;
    }

    public void setIs_Hint_Available(int is_Hint_Available) {
        Is_Hint_Available = is_Hint_Available;
    }

    public int getNo_Of_Correct_Answers() {
        return No_Of_Correct_Answers;
    }

    public void setNo_Of_Correct_Answers(int no_Of_Correct_Answers) {
        No_Of_Correct_Answers = no_Of_Correct_Answers;
    }

    public int getDisplay_Order() {
        return Display_Order;
    }

    public void setDisplay_Order(int display_Order) {
        Display_Order = display_Order;
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

    public int getPublish_ID() {
        return Publish_ID;
    }

    public void setPublish_ID(int publish_ID) {
        Publish_ID = publish_ID;
    }

    public String getCorrect_Answer_Justification() {
        return Correct_Answer_Justification;
    }

    public void setCorrect_Answer_Justification(String correct_Answer_Justification) {
        Correct_Answer_Justification = correct_Answer_Justification;
    }

    public int getIs_Prep_Test() {
        return Is_Prep_Test;
    }

    public void setIs_Prep_Test(int is_Prep_Test) {
        Is_Prep_Test = is_Prep_Test;
    }

    public boolean isMandatory() {
        return IsMandatory;
    }

    public void setMandatory(boolean mandatory) {
        IsMandatory = mandatory;
    }

    public boolean isNegative() {
        return IsNegative;
    }

    public void setNegative(boolean negative) {
        IsNegative = negative;
    }

    public float getNegative_Mark() {
        return Negative_Mark;
    }

    public void setNegative_Mark(float negative_Mark) {
        Negative_Mark = negative_Mark;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public int getCount_Of_Correct_Answers() {
        return Count_Of_Correct_Answers;
    }

    public void setCount_Of_Correct_Answers(int count_Of_Correct_Answers) {
        Count_Of_Correct_Answers = count_Of_Correct_Answers;
    }

    public String getExplanation() {
        return Explanation;
    }

    public void setExplanation(String explanation) {
        Explanation = explanation;
    }

    public String getHint() {
        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }

    public String getMin_Range_Title() {
        return Min_Range_Title;
    }

    public void setMin_Range_Title(String min_Range_Title) {
        Min_Range_Title = min_Range_Title;
    }

    public String getMax_Range_Title() {
        return Max_Range_Title;
    }

    public void setMax_Range_Title(String max_Range_Title) {
        Max_Range_Title = max_Range_Title;
    }

    public String getQuestion_Number_Title() {
        return Question_Number_Title;
    }

    public void setQuestion_Number_Title(String question_Number_Title) {
        Question_Number_Title = question_Number_Title;
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

    public boolean isShow_Scaling_Number() {
        return Show_Scaling_Number;
    }

    public void setShow_Scaling_Number(boolean show_Scaling_Number) {
        Show_Scaling_Number = show_Scaling_Number;
    }

    public boolean is_Remark_Allowed() {
        return Is_Remark_Allowed;
    }

    public void setIs_Remark_Allowed(boolean is_Remark_Allowed) {
        Is_Remark_Allowed = is_Remark_Allowed;
    }

    public String getDraft_Remarks() {
        return Draft_Remarks;
    }

    public void setDraft_Remarks(String draft_Remarks) {
        Draft_Remarks = draft_Remarks;
    }

    public String getDraft_Remark_Url() {
        return Draft_Remark_Url;
    }

    public void setDraft_Remark_Url(String draft_Remark_Url) {
        Draft_Remark_Url = draft_Remark_Url;
    }
}

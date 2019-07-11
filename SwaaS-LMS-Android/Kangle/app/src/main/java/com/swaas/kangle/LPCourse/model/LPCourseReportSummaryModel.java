package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 29-08-2017.
 */

public class LPCourseReportSummaryModel implements Serializable {

    private String Employee_Name;
    private String User_Name;
    private String Course_Name;
    private String Section_Name;
    private int Course_Section_User_Exam_Detail_Id;
    private String Question_Text;
    private boolean Is_Correct;
    private boolean Is_Qualified;
    private int Attempt_Number;
    private boolean Is_Max;
    private int Record_Status;
    private int Achieved_Percentage;
    private String Section_Exam_Start_Time;
    private String Section_Exam_End_Time;
    private String Formatted_Section_Exam_Start_Time;
    private String Formatted_Section_Exam_End_Time;
    private int Company_Id;
    private int User_Id;
    private int Section_Id;
    private int Publish_Id;
    private int Question_Count;
    private int Prep_Attempt_Number;
    private int Course_Section_User_Exam_Id;
    private int Question_Id;
    private int countx;
    private int No_Of_Correct_Answers;
    private int Question_Type;
    private float Negative_Mark;
    private String Explanation;
    private float Marks_Given;
    private float Marks_Allotted;
    private float Total_Marks;
    private String Answer_Text;

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getCourse_Name() {
        return Course_Name;
    }

    public void setCourse_Name(String course_Name) {
        Course_Name = course_Name;
    }

    public String getSection_Name() {
        return Section_Name;
    }

    public void setSection_Name(String section_Name) {
        Section_Name = section_Name;
    }

    public int getCourse_Section_User_Exam_Detail_Id() {
        return Course_Section_User_Exam_Detail_Id;
    }

    public void setCourse_Section_User_Exam_Detail_Id(int course_Section_User_Exam_Detail_Id) {
        Course_Section_User_Exam_Detail_Id = course_Section_User_Exam_Detail_Id;
    }

    public String getQuestion_Text() {
        return Question_Text;
    }

    public void setQuestion_Text(String question_Text) {
        Question_Text = question_Text;
    }

    public boolean is_Correct() {
        return Is_Correct;
    }

    public void setIs_Correct(boolean is_Correct) {
        Is_Correct = is_Correct;
    }

    public boolean is_Qualified() {
        return Is_Qualified;
    }

    public void setIs_Qualified(boolean is_Qualified) {
        Is_Qualified = is_Qualified;
    }

    public int getAttempt_Number() {
        return Attempt_Number;
    }

    public void setAttempt_Number(int attempt_Number) {
        Attempt_Number = attempt_Number;
    }

    public boolean is_Max() {
        return Is_Max;
    }

    public void setIs_Max(boolean is_Max) {
        Is_Max = is_Max;
    }

    public int getRecord_Status() {
        return Record_Status;
    }

    public void setRecord_Status(int record_Status) {
        Record_Status = record_Status;
    }

    public int getAchieved_Percentage() {
        return Achieved_Percentage;
    }

    public void setAchieved_Percentage(int achieved_Percentage) {
        Achieved_Percentage = achieved_Percentage;
    }

    public String getSection_Exam_Start_Time() {
        return Section_Exam_Start_Time;
    }

    public void setSection_Exam_Start_Time(String section_Exam_Start_Time) {
        Section_Exam_Start_Time = section_Exam_Start_Time;
    }

    public String getSection_Exam_End_Time() {
        return Section_Exam_End_Time;
    }

    public void setSection_Exam_End_Time(String section_Exam_End_Time) {
        Section_Exam_End_Time = section_Exam_End_Time;
    }

    public String getFormatted_Section_Exam_Start_Time() {
        return Formatted_Section_Exam_Start_Time;
    }

    public void setFormatted_Section_Exam_Start_Time(String formatted_Section_Exam_Start_Time) {
        Formatted_Section_Exam_Start_Time = formatted_Section_Exam_Start_Time;
    }

    public String getFormatted_Section_Exam_End_Time() {
        return Formatted_Section_Exam_End_Time;
    }

    public void setFormatted_Section_Exam_End_Time(String formatted_Section_Exam_End_Time) {
        Formatted_Section_Exam_End_Time = formatted_Section_Exam_End_Time;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public int getSection_Id() {
        return Section_Id;
    }

    public void setSection_Id(int section_Id) {
        Section_Id = section_Id;
    }

    public int getPublish_Id() {
        return Publish_Id;
    }

    public void setPublish_Id(int publish_Id) {
        Publish_Id = publish_Id;
    }

    public int getQuestion_Count() {
        return Question_Count;
    }

    public void setQuestion_Count(int question_Count) {
        Question_Count = question_Count;
    }

    public int getPrep_Attempt_Number() {
        return Prep_Attempt_Number;
    }

    public void setPrep_Attempt_Number(int prep_Attempt_Number) {
        Prep_Attempt_Number = prep_Attempt_Number;
    }

    public int getCourse_Section_User_Exam_Id() {
        return Course_Section_User_Exam_Id;
    }

    public void setCourse_Section_User_Exam_Id(int course_Section_User_Exam_Id) {
        Course_Section_User_Exam_Id = course_Section_User_Exam_Id;
    }

    public int getQuestion_Id() {
        return Question_Id;
    }

    public void setQuestion_Id(int question_Id) {
        Question_Id = question_Id;
    }

    public int getCountx() {
        return countx;
    }

    public void setCountx(int countx) {
        this.countx = countx;
    }

    public int getNo_Of_Correct_Answers() {
        return No_Of_Correct_Answers;
    }

    public void setNo_Of_Correct_Answers(int no_Of_Correct_Answers) {
        No_Of_Correct_Answers = no_Of_Correct_Answers;
    }

    public int getQuestion_Type() {
        return Question_Type;
    }

    public void setQuestion_Type(int question_Type) {
        Question_Type = question_Type;
    }

    public float getNegative_Mark() {
        return Negative_Mark;
    }

    public void setNegative_Mark(float negative_Mark) {
        Negative_Mark = negative_Mark;
    }

    public String getExplanation() {
        return Explanation;
    }

    public void setExplanation(String explanation) {
        Explanation = explanation;
    }

    public float getMarks_Given() {
        return Marks_Given;
    }

    public void setMarks_Given(float marks_Given) {
        Marks_Given = marks_Given;
    }

    public float getMarks_Allotted() {
        return Marks_Allotted;
    }

    public void setMarks_Allotted(float marks_Allotted) {
        Marks_Allotted = marks_Allotted;
    }

    public String getAnswer_Text() {
        return Answer_Text;
    }

    public void setAnswer_Text(String answer_Text) {
        Answer_Text = answer_Text;
    }

    public float getTotal_Marks() {
        return Total_Marks;
    }

    public void setTotal_Marks(float total_Marks) {
        Total_Marks = total_Marks;
    }
}

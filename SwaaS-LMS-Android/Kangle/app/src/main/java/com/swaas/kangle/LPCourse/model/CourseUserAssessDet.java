package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

/**
 * Created by Hariharan on 24/8/17.
 */

public class CourseUserAssessDet implements Serializable {

        public int User_Id;
        public String Company_Id;
        public int Course_ID;
        public int Publish_ID;
        public int Section_Id;
        public String Question_ID;
        public int Question_Type;
        public String User_Answer_Id;
        public int Count_of_User_Answers;
        public int Course_User_Assignment_Id;
        public int Couse_User_Section_Mapping_Id;
        public int Count_Of_User_Correct_Answers;
        public boolean Is_Correct;
        public float Negative_Mark;

        public int getUser_Id() {
                return User_Id;
        }

        public void setUser_Id(int user_Id) {
                User_Id = user_Id;
        }

        public String getCompany_Id() {
                return Company_Id;
        }

        public void setCompany_Id(String company_Id) {
                Company_Id = company_Id;
        }

        public int getCourse_ID() {
                return Course_ID;
        }

        public void setCourse_ID(int course_ID) {
                Course_ID = course_ID;
        }

        public int getPublish_ID() {
                return Publish_ID;
        }

        public void setPublish_ID(int publish_ID) {
                Publish_ID = publish_ID;
        }

        public int getSection_Id() {
                return Section_Id;
        }

        public void setSection_Id(int section_Id) {
                Section_Id = section_Id;
        }

        public String getQuestion_ID() {
                return Question_ID;
        }

        public void setQuestion_ID(String question_ID) {
                Question_ID = question_ID;
        }

        public int getQuestion_Type() {
                return Question_Type;
        }

        public void setQuestion_Type(int question_Type) {
                Question_Type = question_Type;
        }

        public String getUser_Answer_Id() {
                return User_Answer_Id;
        }

        public void setUser_Answer_Id(String user_Answer_Id) {
                User_Answer_Id = user_Answer_Id;
        }

        public int getCount_of_User_Answers() {
                return Count_of_User_Answers;
        }

        public void setCount_of_User_Answers(int count_of_User_Answers) {
                Count_of_User_Answers = count_of_User_Answers;
        }

        public int getCourse_User_Assignment_Id() {
                return Course_User_Assignment_Id;
        }

        public void setCourse_User_Assignment_Id(int course_User_Assignment_Id) {
                Course_User_Assignment_Id = course_User_Assignment_Id;
        }

        public int getCouse_User_Section_Mapping_Id() {
                return Couse_User_Section_Mapping_Id;
        }

        public void setCouse_User_Section_Mapping_Id(int couse_User_Section_Mapping_Id) {
                Couse_User_Section_Mapping_Id = couse_User_Section_Mapping_Id;
        }

        public int getCount_Of_User_Correct_Answers() {
                return Count_Of_User_Correct_Answers;
        }

        public void setCount_Of_User_Correct_Answers(int count_Of_User_Correct_Answers) {
                Count_Of_User_Correct_Answers = count_Of_User_Correct_Answers;
        }

        public boolean is_Correct() {
                return Is_Correct;
        }

        public void setIs_Correct(boolean is_Correct) {
                Is_Correct = is_Correct;
        }

        public float getNegative_Mark() {
                return Negative_Mark;
        }

        public void setNegative_Mark(float negative_Mark) {
                Negative_Mark = negative_Mark;
        }
}

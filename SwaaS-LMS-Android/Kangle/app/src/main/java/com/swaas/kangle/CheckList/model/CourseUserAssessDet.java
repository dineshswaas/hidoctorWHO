package com.swaas.kangle.CheckList.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 01-05-2018.
 */

public class CourseUserAssessDet implements Serializable {

        public int User_Id;
        public String Company_Id;
        public int Checklist_ID;
        public int Publish_ID;
        public int Section_Id;
        public String Question_ID;
        public int Question_Type;
        public String User_Answer_Id;
        public int Count_of_User_Answers;
        public int Checklist_User_Assignment_Id;
        public int Checklist_Section_User_Exam_Id;
        public int Couse_User_Section_Mapping_Id;
        public int Count_Of_User_Correct_Answers;
        public boolean Is_Correct;
        public float Negative_Mark;
        public String Offset_Value;
        public String Local_TimeZone;

        public String Question_Remarks;
        public String Question_Remark_Url;
        public int Checklist_Publish_Type;
        public int CheckList_Publish_Group_Id;

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
                return Checklist_ID;
        }

        public void setCourse_ID(int course_ID) {
                Checklist_ID = course_ID;
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
                return Checklist_User_Assignment_Id;
        }

        public void setCourse_User_Assignment_Id(int course_User_Assignment_Id) {
                Checklist_User_Assignment_Id = course_User_Assignment_Id;
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

        public int getChecklist_Section_User_Exam_Id() {
                return Checklist_Section_User_Exam_Id;
        }

        public void setChecklist_Section_User_Exam_Id(int checklist_Section_User_Exam_Id) {
                Checklist_Section_User_Exam_Id = checklist_Section_User_Exam_Id;
        }

        public String getQuestion_Remarks() {
                return Question_Remarks;
        }

        public void setQuestion_Remarks(String question_Remarks) {
                Question_Remarks = question_Remarks;
        }

        public String getQuestion_Remark_Url() {
                return Question_Remark_Url;
        }

        public void setQuestion_Remark_Url(String question_Remark_Url) {
                Question_Remark_Url = question_Remark_Url;
        }

        public String getOffset_Value() {
                return Offset_Value;
        }

        public void setOffset_Value(String offset_Value) {
                Offset_Value = offset_Value;
        }

        public String getLocal_TimeZone() {
                return Local_TimeZone;
        }

        public void setLocal_TimeZone(String local_TimeZone) {
                Local_TimeZone = local_TimeZone;
        }

        public int getChecklist_Publish_Type() {
                return Checklist_Publish_Type;
        }

        public void setChecklist_Publish_Type(int checklist_Publish_Type) {
                Checklist_Publish_Type = checklist_Publish_Type;
        }

        public int getCheckList_Publish_Group_Id() {
                return CheckList_Publish_Group_Id;
        }

        public void setCheckList_Publish_Group_Id(int checkList_Publish_Group_Id) {
                CheckList_Publish_Group_Id = checkList_Publish_Group_Id;
        }
}

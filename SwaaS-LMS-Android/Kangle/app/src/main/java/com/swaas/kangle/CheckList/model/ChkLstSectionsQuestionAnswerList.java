package com.swaas.kangle.CheckList.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 03-05-2018.
 */

public class ChkLstSectionsQuestionAnswerList implements Serializable{

    QuestionCourseListModel lstCourse;
    List<QuestionAndAnswerModel> questionanswerList;

    public List<QuestionAndAnswerModel> getQuestionanswerList() {
        return questionanswerList;
    }

    public void setQuestionanswerList(List<QuestionAndAnswerModel> questionanswerList) {
        this.questionanswerList = questionanswerList;
    }

    public QuestionCourseListModel getLstCourse() {
        return lstCourse;
    }

    public void setLstCourse(QuestionCourseListModel lstCourse) {
        this.lstCourse = lstCourse;
    }
}

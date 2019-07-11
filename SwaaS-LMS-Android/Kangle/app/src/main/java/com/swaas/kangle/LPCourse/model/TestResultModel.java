package com.swaas.kangle.LPCourse.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 30-08-2017.
 */

public class TestResultModel implements Serializable {

    public int Slno;
    public String TestAnswers;
    public int marksSecured;
    public String RecordDate;
    public int QuestionLoadCount;
    public String islastQuestion;
    public String istimeout;
    private String IsSynced;

    public int getSlno() {
        return Slno;
    }

    public void setSlno(int slno) {
        Slno = slno;
    }

    public String getTestAnswers() {
        return TestAnswers;
    }

    public void setTestAnswers(String testAnswers) {
        TestAnswers = testAnswers;
    }

    public int getMarksSecured() {
        return marksSecured;
    }

    public void setMarksSecured(int marksSecured) {
        this.marksSecured = marksSecured;
    }

    public String getRecordDate() {
        return RecordDate;
    }

    public void setRecordDate(String recordDate) {
        RecordDate = recordDate;
    }

    public int getQuestionLoadCount() {
        return QuestionLoadCount;
    }

    public void setQuestionLoadCount(int questionLoadCount) {
        QuestionLoadCount = questionLoadCount;
    }

    public String getIslastQuestion() {
        return islastQuestion;
    }

    public void setIslastQuestion(String islastQuestion) {
        this.islastQuestion = islastQuestion;
    }

    public String getIstimeout() {
        return istimeout;
    }

    public void setIstimeout(String istimeout) {
        this.istimeout = istimeout;
    }

    public String getIsSynced() {
        return IsSynced;
    }

    public void setIsSynced(String isSynced) {
        IsSynced = isSynced;
    }
}

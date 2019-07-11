package com.swaas.kangle.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 12-06-2017.
 */

public class DashBoardDetailsModel implements Serializable {

    public int Total_Count;
    public int Yet_to_Start;
    public int InProgress;
    public int Completed;
    public int Fail;
    public int Max_Attempt_Reached;
    public int Overdue;

    //Asset List
    public int Total_Assets;
    public int Read_Assets;
    public int Unread_Assets;
    public int Liked_Assets;

    //Checklist List
    public int Total_Checklist;
    public int Completed_Checklist;
    public int Yet_to_Start_Checklist;
    public int Expired_Checklist;

    //Task List
    public int Assigned_Task;
    public int Completed_Task;
    public int In_progress_Task;
    public int Review_Task;

    public int getTotal_Count() {
        return Total_Count;
    }

    public void setTotal_Count(int total_Count) {
        Total_Count = total_Count;
    }

    public int getYet_to_Start() {
        return Yet_to_Start;
    }

    public void setYet_to_Start(int yet_to_Start) {
        Yet_to_Start = yet_to_Start;
    }

    public int getInProgress() {
        return InProgress;
    }

    public void setInProgress(int inProgress) {
        InProgress = inProgress;
    }

    public int getCompleted() {
        return Completed;
    }

    public void setCompleted(int completed) {
        Completed = completed;
    }

    public int getFail() {
        return Fail;
    }

    public void setFail(int fail) {
        Fail = fail;
    }

    public int getMax_Attempt_Reached() {
        return Max_Attempt_Reached;
    }

    public void setMax_Attempt_Reached(int max_Attempt_Reached) {
        Max_Attempt_Reached = max_Attempt_Reached;
    }

    public int getOverdue() {
        return Overdue;
    }

    public void setOverdue(int overdue) {
        Overdue = overdue;
    }


    public int getTotal_Assets() {
        return Total_Assets;
    }

    public void setTotal_Assets(int total_Assets) {
        Total_Assets = total_Assets;
    }

    public int getRead_Assets() {
        return Read_Assets;
    }

    public void setRead_Assets(int read_Assets) {
        Read_Assets = read_Assets;
    }

    public int getUnread_Assets() {
        return Unread_Assets;
    }

    public void setUnread_Assets(int unread_Assets) {
        Unread_Assets = unread_Assets;
    }

    public int getLiked_Assets() {
        return Liked_Assets;
    }

    public void setLiked_Assets(int liked_Assets) {
        Liked_Assets = liked_Assets;
    }

    public int getTotal_Checklist() {
        return Total_Checklist;
    }

    public void setTotal_Checklist(int total_Checklist) {
        Total_Checklist = total_Checklist;
    }

    public int getCompleted_Checklist() {
        return Completed_Checklist;
    }

    public void setCompleted_Checklist(int completed_Checklist) {
        Completed_Checklist = completed_Checklist;
    }

    public int getYet_to_Start_Checklist() {
        return Yet_to_Start_Checklist;
    }

    public void setYet_to_Start_Checklist(int yet_to_Start_Checklist) {
        Yet_to_Start_Checklist = yet_to_Start_Checklist;
    }

    public int getExpired_Checklist() {
        return Expired_Checklist;
    }

    public void setExpired_Checklist(int expired_Checklist) {
        Expired_Checklist = expired_Checklist;
    }

    public int getAssigned_Task() {
        return Assigned_Task;
    }

    public void setAssigned_Task(int assigned_Task) {
        Assigned_Task = assigned_Task;
    }

    public int getCompleted_Task() {
        return Completed_Task;
    }

    public void setCompleted_Task(int completed_Task) {
        Completed_Task = completed_Task;
    }

    public int getIn_progress_Task() {
        return In_progress_Task;
    }

    public void setIn_progress_Task(int in_progress_Task) {
        In_progress_Task = in_progress_Task;
    }

    public int getReview_Task() {
        return Review_Task;
    }

    public void setReview_Task(int review_Task) {
        Review_Task = review_Task;
    }
}

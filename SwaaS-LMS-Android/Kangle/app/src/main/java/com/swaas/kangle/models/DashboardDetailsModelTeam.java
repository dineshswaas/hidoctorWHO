package com.swaas.kangle.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 12-11-2018.
 */

public class DashboardDetailsModelTeam implements Serializable {

    public int Total_Count_Team;
    public int Yet_to_Start_Team;
    public int InProgress_Team;
    public int Completed_Team;
    public int Fail_Team;
    public int Max_Attempt_Reached_Team;
    public int Overdue_Team;

    //Asset List
    public int Total_Assets_Team;
    public int Read_Assets_Team;
    public int Unread_Assets_Team;
    public int Liked_Assets_Team;

    //Checklist List
    public int Total_Checklist_Team;
    public int Completed_Checklist_Team;
    public int Yet_to_Start_Checklist_Team;
    public int Expired_Checklist_Team;

    //Task List
    public int Assigned_Task_Team;
    public int Completed_Task_Team;
    public int In_progress_Task_Team;
    public int Review_Task_Team;

    public int getTotal_Count_Team() {
        return Total_Count_Team;
    }

    public void setTotal_Count_Team(int total_Count_Team) {
        Total_Count_Team = total_Count_Team;
    }

    public int getYet_to_Start_Team() {
        return Yet_to_Start_Team;
    }

    public void setYet_to_Start_Team(int yet_to_Start_Team) {
        Yet_to_Start_Team = yet_to_Start_Team;
    }

    public int getInProgress_Team() {
        return InProgress_Team;
    }

    public void setInProgress_Team(int inProgress_Team) {
        InProgress_Team = inProgress_Team;
    }

    public int getCompleted_Team() {
        return Completed_Team;
    }

    public void setCompleted_Team(int completed_Team) {
        Completed_Team = completed_Team;
    }

    public int getFail_Team() {
        return Fail_Team;
    }

    public void setFail_Team(int fail_Team) {
        Fail_Team = fail_Team;
    }

    public int getMax_Attempt_Reached_Team() {
        return Max_Attempt_Reached_Team;
    }

    public void setMax_Attempt_Reached_Team(int max_Attempt_Reached_Team) {
        Max_Attempt_Reached_Team = max_Attempt_Reached_Team;
    }

    public int getOverdue_Team() {
        return Overdue_Team;
    }

    public void setOverdue_Team(int overdue_Team) {
        Overdue_Team = overdue_Team;
    }

    public int getTotal_Assets_Team() {
        return Total_Assets_Team;
    }

    public void setTotal_Assets_Team(int total_Assets_Team) {
        Total_Assets_Team = total_Assets_Team;
    }

    public int getRead_Assets_Team() {
        return Read_Assets_Team;
    }

    public void setRead_Assets_Team(int read_Assets_Team) {
        Read_Assets_Team = read_Assets_Team;
    }

    public int getUnread_Assets_Team() {
        return Unread_Assets_Team;
    }

    public void setUnread_Assets_Team(int unread_Assets_Team) {
        Unread_Assets_Team = unread_Assets_Team;
    }

    public int getLiked_Assets_Team() {
        return Liked_Assets_Team;
    }

    public void setLiked_Assets_Team(int liked_Assets_Team) {
        Liked_Assets_Team = liked_Assets_Team;
    }

    public int getTotal_Checklist_Team() {
        return Total_Checklist_Team;
    }

    public void setTotal_Checklist_Team(int total_Checklist_Team) {
        Total_Checklist_Team = total_Checklist_Team;
    }

    public int getCompleted_Checklist_Team() {
        return Completed_Checklist_Team;
    }

    public void setCompleted_Checklist_Team(int completed_Checklist_Team) {
        Completed_Checklist_Team = completed_Checklist_Team;
    }

    public int getYet_to_Start_Checklist_Team() {
        return Yet_to_Start_Checklist_Team;
    }

    public void setYet_to_Start_Checklist_Team(int yet_to_Start_Checklist_Team) {
        Yet_to_Start_Checklist_Team = yet_to_Start_Checklist_Team;
    }

    public int getExpired_Checklist_Team() {
        return Expired_Checklist_Team;
    }

    public void setExpired_Checklist_Team(int expired_Checklist_Team) {
        Expired_Checklist_Team = expired_Checklist_Team;
    }

    public int getAssigned_Task_Team() {
        return Assigned_Task_Team;
    }

    public void setAssigned_Task_Team(int assigned_Task_Team) {
        Assigned_Task_Team = assigned_Task_Team;
    }

    public int getCompleted_Task_Team() {
        return Completed_Task_Team;
    }

    public void setCompleted_Task_Team(int completed_Task_Team) {
        Completed_Task_Team = completed_Task_Team;
    }

    public int getIn_progress_Task_Team() {
        return In_progress_Task_Team;
    }

    public void setIn_progress_Task_Team(int in_progress_Task_Team) {
        In_progress_Task_Team = in_progress_Task_Team;
    }

    public int getReview_Task_Team() {
        return Review_Task_Team;
    }

    public void setReview_Task_Team(int review_Task_Team) {
        Review_Task_Team = review_Task_Team;
    }
}

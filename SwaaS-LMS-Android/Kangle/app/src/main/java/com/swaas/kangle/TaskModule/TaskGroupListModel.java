package com.swaas.kangle.TaskModule;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sayellessh on 22-11-2018.
 */

public class TaskGroupListModel implements Serializable {

    ArrayList<TaskListModel> lstTaskListReview;
    ArrayList<TaskListModel> lstTaskListOther;

    public ArrayList<TaskListModel> getLstTaskListReview() {
        return lstTaskListReview;
    }

    public void setLstTaskListReview(ArrayList<TaskListModel> lstTaskListReview) {
        this.lstTaskListReview = lstTaskListReview;
    }

    public ArrayList<TaskListModel> getLstTaskListOther() {
        return lstTaskListOther;
    }

    public void setLstTaskListOther(ArrayList<TaskListModel> lstTaskListOther) {
        this.lstTaskListOther = lstTaskListOther;
    }
}

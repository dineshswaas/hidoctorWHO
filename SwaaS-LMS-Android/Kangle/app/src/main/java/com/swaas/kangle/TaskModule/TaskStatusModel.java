package com.swaas.kangle.TaskModule;

import java.io.Serializable;

/**
 * Created by Sayellessh on 05-12-2018.
 */

public class TaskStatusModel implements Serializable{

    public String response;
    public int response_status;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getResponse_status() {
        return response_status;
    }

    public void setResponse_status(int response_status) {
        this.response_status = response_status;
    }
}

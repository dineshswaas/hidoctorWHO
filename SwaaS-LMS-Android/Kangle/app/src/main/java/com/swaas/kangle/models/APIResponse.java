package com.swaas.kangle.models;

import java.util.List;

/**
 * Created by Sayellessh on 05-04-2017.
 */

public class APIResponse<T> {

    T Result;
    private List<T> ResultList;
    private List<T> list;
    private int Status;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public T getResult() {
        return Result;
    }

    public void setResult(T result) {
        Result = result;
    }

    public List<T> getResultList() {
        return ResultList;
    }

    public void setResultList(List<T> resultList) {
        ResultList = resultList;
    }

    public List<T> getResultDetails() {
        return list;
    }

    public void setResultDetails(List<T> result) {
        this.list = result;
    }

}

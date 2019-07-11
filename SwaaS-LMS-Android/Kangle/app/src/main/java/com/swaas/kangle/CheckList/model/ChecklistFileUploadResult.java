package com.swaas.kangle.CheckList.model;

import java.io.Serializable;

/**
 * Created by Sayellessh on 17-05-2018.
 */

public class ChecklistFileUploadResult implements Serializable {

    public String FileName;
    public String FileUri;
    public String Type;
    public String FileName_GUID;
    public String Context1;
    public String Context2;
    public String Context3;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileUri() {
        return FileUri;
    }

    public void setFileUri(String fileUri) {
        FileUri = fileUri;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getFileName_GUID() {
        return FileName_GUID;
    }

    public void setFileName_GUID(String fileName_GUID) {
        FileName_GUID = fileName_GUID;
    }

    public String getContext1() {
        return Context1;
    }

    public void setContext1(String context1) {
        Context1 = context1;
    }

    public String getContext2() {
        return Context2;
    }

    public void setContext2(String context2) {
        Context2 = context2;
    }

    public String getContext3() {
        return Context3;
    }

    public void setContext3(String context3) {
        Context3 = context3;
    }
}

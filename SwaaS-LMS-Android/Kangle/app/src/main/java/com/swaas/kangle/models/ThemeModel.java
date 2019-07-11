package com.swaas.kangle.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class ThemeModel {
    @SerializedName("Variable_Name")
    @Expose
    private String variableName;
    @SerializedName("Variable_Value")
    @Expose
    private String variableValue;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableValue() {
        return variableValue;
    }

    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
    }

}

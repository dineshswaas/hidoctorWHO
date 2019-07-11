package com.swaas.kangle.TaskModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by barath on 11/27/2018.
 */

public class ChildUserListModel {

        @SerializedName("User_Name")
        @Expose
        private String userName;
        @SerializedName("User_Type_Name")
        @Expose
        private String userTypeName;
        @SerializedName("User_Id")
        @Expose
        private Integer userId;
        @SerializedName("User_Code")
        @Expose
        private String userCode;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserTypeName() {
            return userTypeName;
        }

        public void setUserTypeName(String userTypeName) {
            this.userTypeName = userTypeName;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }


}

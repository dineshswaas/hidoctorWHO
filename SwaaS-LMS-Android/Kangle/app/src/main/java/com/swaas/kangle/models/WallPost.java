package com.swaas.kangle.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 08-05-2017.
 */

public class WallPost implements Serializable {

    public String Company_Code;
    public int Company_Id;
    public String Content_Id;
    public String Employee_Name;
    public String Message;
    List<PostComment> PostComments;
    public int PostId;
    public String PostedBy;
    public String PostedByAvatar;
    public String PostedByName;
    public String PostedDate;
    public int User_Id;

    public int Post_Id;

    public String getCompany_Code() {
        return Company_Code;
    }

    public void setCompany_Code(String company_Code) {
        Company_Code = company_Code;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public String getContent_Id() {
        return Content_Id;
    }

    public void setContent_Id(String content_Id) {
        Content_Id = content_Id;
    }

    public String getEmployee_Name() {
        return Employee_Name;
    }

    public void setEmployee_Name(String employee_Name) {
        Employee_Name = employee_Name;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<PostComment> getPostComments() {
        return PostComments;
    }

    public void setPostComments(List<PostComment> postComments) {
        PostComments = postComments;
    }

    public int getPostId() {
        return PostId;
    }

    public void setPostId(int postId) {
        PostId = postId;
    }

    public String getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(String postedBy) {
        PostedBy = postedBy;
    }

    public String getPostedByAvatar() {
        return PostedByAvatar;
    }

    public void setPostedByAvatar(String postedByAvatar) {
        PostedByAvatar = postedByAvatar;
    }

    public String getPostedByName() {
        return PostedByName;
    }

    public void setPostedByName(String postedByName) {
        PostedByName = postedByName;
    }

    public String getPostedDate() {
        return PostedDate;
    }

    public void setPostedDate(String postedDate) {
        PostedDate = postedDate;
    }

    public int getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(int user_Id) {
        User_Id = user_Id;
    }

    public int getPost_Id() {
        return Post_Id;
    }

    public void setPost_Id(int post_Id) {
        Post_Id = post_Id;
    }
}

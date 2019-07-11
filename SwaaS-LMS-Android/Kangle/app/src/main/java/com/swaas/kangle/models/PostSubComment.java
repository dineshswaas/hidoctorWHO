package com.swaas.kangle.models;

import java.io.Serializable;

/**
 * Created by Sayellessh on 18-05-2017.
 */

public class PostSubComment implements Serializable {

    public int CommentedBy;
    public String CommentedByName;
    public String CommentedByAvatar;
    public String CommentedDate;
    public int CommentId;
    public String Message;
    public int PostId;
    public int Company_Id;

    public int getCommentedBy() {
        return CommentedBy;
    }

    public void setCommentedBy(int commentedBy) {
        CommentedBy = commentedBy;
    }

    public String getCommentedByName() {
        return CommentedByName;
    }

    public void setCommentedByName(String commentedByName) {
        CommentedByName = commentedByName;
    }

    public String getCommentedByAvatar() {
        return CommentedByAvatar;
    }

    public void setCommentedByAvatar(String commentedByAvatar) {
        CommentedByAvatar = commentedByAvatar;
    }

    public String getCommentedDate() {
        return CommentedDate;
    }

    public void setCommentedDate(String commentedDate) {
        CommentedDate = commentedDate;
    }

    public int getCommentId() {
        return CommentId;
    }

    public void setCommentId(int commentId) {
        CommentId = commentId;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getPostId() {
        return PostId;
    }

    public void setPostId(int postId) {
        PostId = postId;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }
}

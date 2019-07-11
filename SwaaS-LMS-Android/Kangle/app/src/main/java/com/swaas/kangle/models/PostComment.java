package com.swaas.kangle.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 08-05-2017.
 */

public class PostComment implements Serializable {

    public String Message;
    public int PostedBy;
    public String PostedByName;
    public String PostedByAvatar;
    public String PostedDate;
    public int PostId;
    public int Company_Id;
    List<PostSubComment> PostComments;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(int postedBy) {
        PostedBy = postedBy;
    }

    public String getPostedByName() {
        return PostedByName;
    }

    public void setPostedByName(String postedByName) {
        PostedByName = postedByName;
    }

    public String getPostedByAvatar() {
        return PostedByAvatar;
    }

    public void setPostedByAvatar(String postedByAvatar) {
        PostedByAvatar = postedByAvatar;
    }

    public String getPostedDate() {
        return PostedDate;
    }

    public void setPostedDate(String postedDate) {
        PostedDate = postedDate;
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

    public List<PostSubComment> getPostComments() {
        return PostComments;
    }

    public void setPostComments(List<PostSubComment> postComments) {
        PostComments = postComments;
    }
}

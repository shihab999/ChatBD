package com.mss.chatbd.Model;

public class Post {
    String postId,postText,postImg,postCreatorId,postCreateTime;

    public Post() {
    }

    public Post(String postId, String postText, String postImg, String postCreatorId, String postCreateTime) {
        this.postId = postId;
        this.postText = postText;
        this.postImg = postImg;
        this.postCreatorId = postCreatorId;
        this.postCreateTime = postCreateTime;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public String getPostCreatorId() {
        return postCreatorId;
    }

    public void setPostCreatorId(String postCreatorId) {
        this.postCreatorId = postCreatorId;
    }

    public String getPostCreateTime() {
        return postCreateTime;
    }

    public void setPostCreateTime(String postCreateTime) {
        this.postCreateTime = postCreateTime;
    }
}

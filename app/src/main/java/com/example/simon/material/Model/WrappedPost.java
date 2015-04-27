package com.example.simon.material.Model;

/**
 * Created by Simon on 2015/04/26.
 */
public class WrappedPost {
    PostType postType;
    String post;

    public WrappedPost() {}

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}

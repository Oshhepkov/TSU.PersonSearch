package com.github.oshhepkov.app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("postId")
    @Expose
    private Integer postId;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("spec")
    @Expose
    private String spec;
    @SerializedName("postDepartmentName")
    @Expose
    private String postDepartmentName;
    @SerializedName("departmentId")
    @Expose
    private Integer departmentId;
    @SerializedName("isMain")
    @Expose
    private Boolean isMain;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPostDepartmentName() {
        return postDepartmentName;
    }

    public void setPostDepartmentName(String postDepartmentName) {
        this.postDepartmentName = postDepartmentName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean isMain) {
        this.isMain = isMain;
    }

}

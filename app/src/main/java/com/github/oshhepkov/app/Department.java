package com.github.oshhepkov.app;

import com.google.gson.annotations.SerializedName;

public class Department {

    @SerializedName("Id")
    public String id;

    @SerializedName("Level")
    public String level;

    @SerializedName("Name")
    public String name;

    @SerializedName("ParentId")
    public String parentId;
}

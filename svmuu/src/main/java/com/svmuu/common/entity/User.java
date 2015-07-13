package com.svmuu.common.entity;

public class User {
    public String name;
    public String password;
    //以下是json解析出来掉
    public String uface;
    public String uid;
    public String money;
    public String fans;

    //是否有视频权限，针对自己的圈子
    public String video_live;
    //是否有聊天权限，针对自己的圈子
    public String chat_live;

}

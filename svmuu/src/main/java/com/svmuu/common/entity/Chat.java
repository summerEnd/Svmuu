package com.svmuu.common.entity;


public class Chat {


    public String uid;
    public String time_m;
    public String uname;
    public String type;
    public String uface;
    public String content;
    public String addtime;
    public String is_owner;
    public String is_admin;
    //1、普通粉丝；2 铁粉；3 年粉 4 季粉',
    public String fans_type;
    public String time_s;
    public String userLevel;
    public String msg_id;
    public String certify_status;
    Question question;

    public boolean isOwner(){
        return "1".equals(is_owner);
    }
}

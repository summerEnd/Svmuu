package com.svmuu.common.entity;


import com.svmuu.common.entity.notice.BaseSystemNotice;

public class Chat {


    public String uid;
    public String time_m;
    public String uname;
    public String type;
    public String uface;

    public String addtime;
    public String is_owner;
    public String is_admin;
    //1、普通粉丝；2 铁粉；3 年粉 4 季粉',
    public String fans_type;
    public String time_s;
    public String userLevel;
    public String msg_id;
    public String certify_status;
    public Question question;//提问，只有问答时有效
    public BaseSystemNotice systemNotice;//系统公告
    public String chatContent;//聊天的内容

    public boolean isOwner() {
        return "1".equals(is_owner);
    }
}

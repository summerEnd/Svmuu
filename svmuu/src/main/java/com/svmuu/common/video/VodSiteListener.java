package com.svmuu.common.video;

import com.gensee.entity.ChatMsg;
import com.gensee.entity.QAMsg;
import com.gensee.entity.VodObject;
import com.gensee.vod.VodSite;

import java.util.List;

/**
 * Created by Lincoln on 15/7/9.
 */
public class VodSiteListener implements VodSite.OnVodListener {
    @Override
    public void onChatHistory(String s, List<ChatMsg> list) {

    }

    @Override
    public void onQaHistory(String s, List<QAMsg> list) {

    }

    @Override
    public void onVodErr(int i) {

    }

    @Override
    public void onVodObject(String s) {

    }

    @Override
    public void onVodDetail(VodObject vodObject) {

    }
}

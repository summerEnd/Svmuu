package com.svmuu.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.svmuu.common.entity.Chat;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 聊天的管理类
 */
public class ChatManager {


    private Context context;
    private Callback callback;
    private String maxMsgId = "0";
    private Timer timer = new Timer();
    private String circleId;

    public ChatManager(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    /**
     * groupid 圈主id
     *
     * @param type    消息类型 1、普通消息 2、解盘消息    4、铁粉悄悄话  5、系统公告
     * @param content 消息内容
     */
    public void sendMessage(String type, String content) {
        SRequest request = new SRequest("sendmssage");
        request.put("quanzhu_id", getCircleId());
        request.put("type", type);
        request.put("content", content);
        HttpManager.getInstance().postMobileApi(context, request, new HttpHandler() {
            @Override
            public void onResultOk(int statusCOde, Header[] headers, Response response) throws JSONException {
                callback.onMessageAdded(JsonUtil.get(response.data, Chat.class));
            }
        });
    }

    /**
     * 获取新的消息
     */
    public void getNewMessages() {
        SRequest request = new SRequest("livemsg");
        request.put("quanzhu_id", circleId);
        request.put("max_msgid", getMaxMsgId());
        HttpManager.getInstance().postMobileApi(context, request, new HttpHandler(false) {
            @Override
            public void onResultOk(int statusCOde, Header[] headers, Response response) throws JSONException {
                ArrayList<Chat> newChats = JsonUtil.getArray(new JSONArray(response.data), Chat.class);
                if (newChats == null || newChats.size() == 0) {
                    return;
                }
                maxMsgId = newChats.get(newChats.size() - 1).msg_id;
                callback.onNewMessageLoaded(newChats);
            }
        });
    }

    /**
     * 获取最大消息id
     */
    public String getMaxMsgId() {
        int max = Integer.parseInt(maxMsgId);
        return (max ) + "";
    }

    public void setMaxMsgId(String maxMsgId) {
        this.maxMsgId = maxMsgId;
    }

    /**
     * 获取圈子id
     */
    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    /**
     * 更新消息列表
     *
     * @param duration 更新的时间间隔 单位毫秒
     */
    public void updateMessageList(long duration) {
        if (timer != null) {
            recycle();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateMsgHandler.sendEmptyMessage(0);
            }
        }, 0, duration);
    }

    public interface Callback {

        /**
         * 发送消息成功
         *
         * @param added 发送的消息
         */
        void onMessageAdded(Chat added);

        /**
         * 加载新的消息列表成功
         *
         * @param newMessages 新的消息列表
         */
        void onNewMessageLoaded(ArrayList<Chat> newMessages);
    }

    private Handler updateMsgHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            getNewMessages();
            return false;
        }
    });

    /**
     * 在destroy种调用
     */
    public void recycle() {
        try {
            timer.cancel();
        } catch (Exception e) {
        }
    }

}

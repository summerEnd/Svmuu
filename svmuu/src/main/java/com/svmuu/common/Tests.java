package com.svmuu.common;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.svmuu.common.http.HttpManager;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class Tests {
    public static final String IMAGE = "http://i9.hexunimg.cn/2012-02-28/138734088.jpg";
    public static final String IMAGE_01 = "http://a.hiphotos.baidu.com/image/pic/item/95eef01f3a292df570489c1fbe315c6034a87334.jpg";
    public static final String IMAGE_02 = "http://b.hiphotos.baidu.com/image/pic/item/b64543a98226cffc3fd15618ba014a90f703eaa5.jpg";

    void get() {

    }


    public static class GetAvailableLive {

        public void get() {
            final SRequest request = new SRequest("http://svmuu.gensee.com/integration/site/webcast/page");
            request.put("loginName", "admin@svmuu.com");
            request.put("password", "888888");
            request.put("status", "2");
            HttpManager.getInstance().post(request, new JsonHttpResponseHandler("UTF-8") {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        A a = JsonUtil.getArray(response.getJSONArray("list"), A.class).get(0);
                        getDetail(a.id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        void getDetail(String id) {
            SRequest request = new SRequest("http://svmuu.gensee.com/integration/site/webcast/setting/info");
            request.put("loginName", "admin@svmuu.com");
            request.put("password", "888888");
            request.put("webcastId", id);
            HttpManager.getInstance().post(request, new JsonHttpResponseHandler("UTF-8") {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        LiveInfo b = JsonUtil.get(response.toString(), LiveInfo.class);
                        onResult(b);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void onResult(LiveInfo info) {

        }
    }

    class A {
        String attendeeJoinUrl;
        String createdTime;
        String description;
        String id;
        String organizerJoinUrl;
        String panelistJoinUrl;
        String startTime;
        String status;
        String subject;


    }

    public class LiveInfo {
        public String aac;
        public String attendeeJoinUrl;
        public String attendeeToken;
        public String code;
        public String description;
        public String id;
        public String loginRequired;
        public String number;
        public String opened;
        public String organizerJoinUrl;
        public String organizerToken;
        public String panelistJoinUrl;
        public String panelistToken;
        public String plan;
        public String realtime;
        public String speakerInfo;
        public String startTime;
        public String subject;
        public String switchClient;
        public String telconf;
    }
}

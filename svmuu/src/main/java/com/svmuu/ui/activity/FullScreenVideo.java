package com.svmuu.ui.activity;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.gensee.view.GSVideoView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.svmuu.R;
import com.svmuu.common.LiveManager;
import com.svmuu.common.http.HttpManager;
import com.svmuu.ui.BaseActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class FullScreenVideo extends BaseActivity {

    private LiveManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_full_sceen_video);
        GSVideoView viewById = (GSVideoView) findViewById(R.id.gsView);
        get();
        manager = new LiveManager(this, viewById);
    }

    @Override
    public void onBackPressed() {
        if (manager.leaveCast()) {
            super.onBackPressed();
        }
    }

    void get() {
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
        final SRequest request = new SRequest("http://svmuu.gensee.com/integration/site/webcast/setting/info");
        request.put("loginName", "admin@svmuu.com");
        request.put("password", "888888");
        request.put("webcastId", id);
        HttpManager.getInstance().post(request, new JsonHttpResponseHandler("UTF-8") {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    SLog.debug(response);
                    B b=JsonUtil.get(response.toString(),B.class);
                    manager.startPlay(b.attendeeToken,"Lincoln",b.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    class B {
        String aac;
        String attendeeJoinUrl;
        String attendeeToken;
        String code;
        String description;
        String id;
        String loginRequired;
        String number;
        String opened;
        String organizerJoinUrl;
        String organizerToken;
        String panelistJoinUrl;
        String panelistToken;
        String plan;
        String realtime;
        String speakerInfo;
        String startTime;
        String subject;
        String switchClient;
        String telconf;
    }
}
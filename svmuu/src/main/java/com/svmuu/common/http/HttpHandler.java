package com.svmuu.common.http;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.WindowManager;

import com.sp.lib.common.support.net.client.SHttpProgressHandler;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.ui.pop.LoginActivity;
import com.svmuu.ui.pop.LoginDialog;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public abstract class HttpHandler extends SHttpProgressHandler {

    private boolean showToast;

    public HttpHandler() {
        this(true);
    }

    /**
     * @param showToast true 用toast弹出Response的message false 反之
     */
    public HttpHandler(boolean showToast) {
        this.showToast = showToast;
    }

    @Override
    public Dialog onCreateDialog() {
        return null;
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        dispatchResult(statusCode, headers, parseResponse(response));
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        super.onSuccess(statusCode, headers, response);
        throw new RuntimeException("wrong return type");
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        Response response = new Response();
        response.status = false;
        response.message = responseString;
        dispatchResult(statusCode, headers, response);
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        onException();
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onException();
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onException();
    }


    private void dispatchResult(int statusCOde, Header[] headers, Response response) {
        //如果不需要强制登录，注释掉下面这行
        mustLogin(response);

        if (showToast && !TextUtils.isEmpty(response.message)) {
            //免去“处理成功”的消息
            if (!"处理成功".equals(response.message)) {
                ContextUtil.toast(response.message);
            }
        }

        try {
            if (response.status) {
                onResultOk(statusCOde, headers, response);
            } else {
                onResultError(statusCOde, headers, response);
            }
        } catch (JSONException e) {
            //Json解析错误
            e.printStackTrace();
        }
    }

    /**
     * 是否强制登录，根据返回的code来判断是否需要用户登录
     */
    public void mustLogin(Response response) {
        if ("9090".equals(response.code)) {
            AppDelegate context = AppDelegate.getInstance();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 请求成功，并且Response的status为true时调用
     */
    public abstract void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException;

    /**
     * 请求成功，并且Response的status为false时调用。
     * 如：密码错误等等
     */
    public void onResultError(int statusCOde, Header[] headers, Response response) throws JSONException {
    }

    /**
     * 请求失败，如网络链接不通或json格式不正确等
     */
    public void onException() {
        ContextUtil.toast(R.string.http_request_fail);
    }

    /**
     * 解析一个{@link org.json.JSONObject JSONObject}
     */
    public Response parseResponse(JSONObject jsonResponse) {
        Response response = new Response();
        try {
            response.data = jsonResponse.getString("data");

            try {
                response.status = jsonResponse.getBoolean("status");
            } catch (JSONException e) {
                response.status = jsonResponse.getBoolean("success");
            }

            try {
                response.message = jsonResponse.getString("message");
            } catch (JSONException e) {
                response.message = jsonResponse.getString("msg");
            }

            if (jsonResponse.has("code")) {
                response.code = jsonResponse.getString("code");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}

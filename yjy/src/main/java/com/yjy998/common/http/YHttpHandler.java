package com.yjy998.common.http;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.sp.lib.common.support.net.client.SHttpProgressHandler;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.ContextUtil;
import com.yjy998.AppDelegate;
import com.yjy998.common.Constant;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Http请求返回结果处理
 */
public abstract class YHttpHandler extends SHttpProgressHandler {

    private boolean showToast;


    protected YHttpHandler(boolean showToast) {
        this.showToast = showToast;
    }

    protected YHttpHandler() {
        this(true);
    }

    @Override
    public Dialog onCreateDialog() {
        return null;
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        Response response = new Response();
        onStatusFailed(response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Response response = new Response();
        onStatusFailed(response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Response response = new Response();
        onStatusFailed(response);
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        Response responseObject = parseResponse(response);

        if (responseObject.status) {
            onStatusCorrect(responseObject);
            toast(responseObject);

        } else {
            String code = responseObject.code;
            if ("1079".equals(code)||"1090".equals(code)||"0000".equals(code)) {
                ContextUtil.toast(responseObject.message);
                //需要重新登录
                if (AppDelegate.getInstance().isUserLogin()) {
                    loginBackground();
                }
            } else {
                onStatusFailed(responseObject);
              toast(responseObject);
            }
        }
    }

    private void toast(Response responseObject) {
        if (showToast) {
            String message = responseObject.message;
            if (!TextUtils.isEmpty(message) && !"null".equals(message)) {
                ContextUtil.toast_debug(message);
            }
        }
    }


    /**
     * 后台自动登录
     */
    void loginBackground() {
        SharedPreferences sp = AppDelegate.getInstance().getSharedPreferences(Constant.PRE_LOGIN, Context.MODE_PRIVATE);
        String phone = sp.getString(Constant.PRE_LOGIN_PHONE, null);
        String password = sp.getString(Constant.PRE_LOGIN_PASSWORD, null);
        String rsa = sp.getString(Constant.PRE_LOGIN_PASSWORD_RSA, null);

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rsa)) {
            return;
        }

        SRequest request = new SRequest();
        request.put("login_name", phone);
        request.put("login_passwd", password);
        request.put("login_rsapwd", rsa);

        //登录
        YHttpClient.getInstance().login(AppDelegate.getInstance(), request, new YHttpHandler(false) {
            @Override
            protected void onStatusFailed(Response response) {
                super.onStatusFailed(response);
            }

            @Override
            protected void onStatusCorrect(Response response) {
                //继续请求,暂时不做
            }
        });
    }

    /**
     * 网络请求成功，并且返回的json status为true时调用这个方法
     */
    protected abstract void onStatusCorrect(Response response);

    /**
     * 所有的失败都会调用这个方法
     * 网络请求失败，或者请求结果的业务逻辑错误等
     */
    protected void onStatusFailed(Response response) {
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

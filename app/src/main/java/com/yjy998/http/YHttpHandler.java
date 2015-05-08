package com.yjy998.http;

import android.app.Dialog;
import android.text.TextUtils;

import com.sp.lib.common.support.net.client.SHttpProgressHandler;
import com.sp.lib.common.util.ContextUtil;

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

//        for (Header header : headers) {
//            SLog.debug("head:" + header);
//            if (CookiePreference.COOKIE.equals(header.getName())) {
//                //把cookie存起来
//                CookiePreference.get().setCookie(header.getValue());
//                SLog.debug("save Cookie:" + header.getValue());
//            }
//        }

        Response responseObject = parseResponse(response);
        if (responseObject.status) {
            onStatusCorrect(responseObject);
        } else {
            onStatusFailed(responseObject);
        }
        if (showToast) {
            String message = responseObject.message;
            if (!TextUtils.isEmpty(message) && !"null".equals(message)) {
                ContextUtil.toast_debug(message);
            }
        }
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

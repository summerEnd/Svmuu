package com.yjy998.http;

import android.app.Dialog;
import android.text.TextUtils;

import com.sp.lib.common.support.net.client.SHttpProgressHandler;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;

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
        showToast = true;
    }

    @Override
    public Dialog onCreateDialog() {
        return null;
    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        Response responseObject = parseResponse(response);
        if (responseObject.status) {
            onStatusCorrect(responseObject);
        } else {
            onStatusFailed(responseObject);
        }
        if (showToast) {
            String message = responseObject.message;
            if (!TextUtils.isEmpty(message)) {
                ContextUtil.toast_debug(message);
            }
        }
    }

    /**
     * 网络请求成功，并且返回的json status为true时调用这个方法
     */
    protected abstract void onStatusCorrect(Response response);

    protected void onStatusFailed(Response response) {
    }

    /**
     * 解析一个{@link org.json.JSONObject JSONObject}
     */
    public Response parseResponse(JSONObject jsonResponse) {
        Response response = new Response();
        try {
            response.data = jsonResponse.getString("data");
            response.status = jsonResponse.getBoolean("status");
            response.message = jsonResponse.getString("message");
            response.code = jsonResponse.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}

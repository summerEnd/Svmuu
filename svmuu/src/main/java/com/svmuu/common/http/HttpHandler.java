package com.svmuu.common.http;

import android.app.Dialog;
import android.text.TextUtils;

import com.sp.lib.common.support.net.client.SHttpProgressHandler;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.svmuu.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


public abstract class HttpHandler extends SHttpProgressHandler {
    @Override
    public Dialog onCreateDialog() {
        return null;
    }

    @Override
    public final void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        dispatchResult(statusCode, headers, JsonUtil.get(response.toString(), Response.class));
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

    public void onException() {
        ContextUtil.toast(R.string.http_request_fail);
    }

    private void dispatchResult(int statusCOde, Header[] headers, Response response) {

        if (!TextUtils.isEmpty(response.message)) {
            ContextUtil.toast(response.message);
        }

        if (response.status) {
            onResultOk(statusCOde, headers, response);
        } else {
            onResultError(statusCOde, headers, response);
        }
    }


    public abstract void onResultOk(int statusCOde, Header[] headers, Response response);

    public void onResultError(int statusCOde, Header[] headers, Response response) {
    }
}

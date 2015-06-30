package com.svmuu.common.http;

import android.app.Dialog;
import android.text.TextUtils;

import com.sp.lib.common.support.net.client.SHttpProgressHandler;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.svmuu.R;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public abstract class HttpHandler extends SHttpProgressHandler {

    private boolean showToast;
    public HttpHandler() {
        this(true);
    }

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

    public void onException() {
        ContextUtil.toast(R.string.http_request_fail);
    }

    private void dispatchResult(int statusCOde, Header[] headers, Response response) {

        if (showToast&&!TextUtils.isEmpty(response.message)) {
            ContextUtil.toast(response.message);
        }

        try {
            if (response.status) {
                onResultOk(statusCOde, headers, response);
            } else {
                onResultError(statusCOde, headers, response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public abstract void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException;

    public void onResultError(int statusCOde, Header[] headers, Response response) throws JSONException{
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

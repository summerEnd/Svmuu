package com.sp.lib.common.support.net.client;

import android.app.Dialog;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sp.lib.R;
import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.SLog;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class SHttpProgressHandler extends JsonHttpResponseHandler implements IHttpProgress {
    private Dialog mDialog;

    @Override
    public final void onStart() {
        super.onStart();
        mDialog = onCreateDialog();
        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        log("status:" + statusCode + "-->" + responseString);
        ContextUtil.toast(R.string.request_is_failed);

    }

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        log("status:" + statusCode + "-->" + errorResponse);
        ContextUtil.toast(R.string.request_is_failed);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        log("status:" + statusCode + "-->" + errorResponse);
        ContextUtil.toast(R.string.request_is_failed);
    }

    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        log(response);
    }

    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        log(response);
    }

    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        log(responseString);
    }

    final void log(Object o) {
        String log;
        if (o == null) {
            log = "null";
        } else if (o instanceof JSONObject) {
            try {
                log = ((JSONObject) o).toString(4);
            } catch (JSONException e) {
                log = String.valueOf(o);
            }
        } else {
            log = String.valueOf(o);
        }
        SLog.debug(log);
    }

}

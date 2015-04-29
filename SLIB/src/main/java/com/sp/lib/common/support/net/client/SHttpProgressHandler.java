package com.sp.lib.common.support.net.client;

import android.app.Dialog;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
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
    public  void onFinish() {
        super.onFinish();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        log(responseString);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        log(errorResponse);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        log(errorResponse);
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
        Log.i("--->",log);
    }

}

package com.svmuu.ui.activity.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.svmuu.AppDelegate;
import com.svmuu.R;
import com.svmuu.common.entity.User;
import com.svmuu.common.http.HttpHandler;
import com.svmuu.common.http.HttpManager;
import com.svmuu.common.http.Response;
import com.svmuu.ui.BaseActivity;
import com.svmuu.ui.pop.ProgressIDialog;

import org.apache.http.Header;
import org.json.JSONException;

public class SuggestionActivity extends BaseActivity {

    private TextView circle;
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        initialize();
    }

    private void initialize() {

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);
        circle = (TextView) findViewById(R.id.circle);
        content = (EditText) findViewById(R.id.content);
        User user = AppDelegate.getInstance().getUser();
        circle.setText(user.name+"("+user.uid+")");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.send) {
            SRequest request = new SRequest("getadvice");
            request.put("content", content.getText().toString());
            HttpManager.getInstance().postMobileApi(this, request, new HttpHandler() {
                @Override
                public void onResultOk(int statusCode, Header[] headers, Response response) throws JSONException {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SuggestionActivity.this);
                    builder.setTitle(R.string.warn);
                    builder.setMessage(response.message);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
                }

                @Override
                public Dialog onCreateDialog() {
                    return new ProgressIDialog(SuggestionActivity.this);
                }
            });

        }
    }
}

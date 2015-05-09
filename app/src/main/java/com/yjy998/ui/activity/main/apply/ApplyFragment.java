package com.yjy998.ui.activity.main.apply;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.entity.Apply;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;

public class ApplyFragment extends BaseFragment implements View.OnClickListener {


    private TextView tnCapitalText;
    private TextView tnMemberText;
    private ImageView triangleImage;
    private LinearLayout tnLayout;
    private TextView t9CapitalText;
    private TextView t9MemberText;
    private LinearLayout t9Layout;
    private TextView freshMemberText;
    private LinearLayout freshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_apply, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }


    private void initialize() {

        tnCapitalText = (TextView) findViewById(R.id.tnCapitalText);
        tnMemberText = (TextView) findViewById(R.id.tnMemberText);
        triangleImage = (ImageView) findViewById(R.id.triangleImage);
        tnLayout = (LinearLayout) findViewById(R.id.tnLayout);
        t9CapitalText = (TextView) findViewById(R.id.t9CapitalText);
        t9MemberText = (TextView) findViewById(R.id.t9MemberText);
        t9Layout = (LinearLayout) findViewById(R.id.t9Layout);
        freshMemberText = (TextView) findViewById(R.id.freshMemberText);
        freshLayout = (LinearLayout) findViewById(R.id.freshLayout);
        findViewById(R.id.t9Layout).setOnClickListener(this);
        findViewById(R.id.tnLayout).setOnClickListener(this);
        findViewById(R.id.freshLayout).setOnClickListener(this);
        get();
    }

    public void get() {
        SRequest request = new SRequest("http://mobile.yjy998.com/h5/index/tradeapplyinfo");
        YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler() {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONArray array = new JSONArray(response.data);
                    for (int i = 0; i < array.length(); i++) {
                        Apply object = JsonUtil.get(array.getString(i), Apply.class);
                        if (Apply.ID_T9.equals(object.prodId)) {
                            t9CapitalText.setText(object.trade);
                            t9MemberText.setText(object.attenders);

                        } else if (Apply.ID_TN.equals(object.prodId)) {
                            tnCapitalText.setText(object.trade);
                            tnMemberText.setText(object.attenders);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.t9Layout: {
                startActivity(new Intent(getActivity(), ApplyActivity.class)
                        .putExtra(ApplyActivity.EXTRA_APPLY, ApplyActivity._T9));
                break;
            }
            case R.id.tnLayout: {
                startActivity(new Intent(getActivity(), ApplyActivity.class)
                        .putExtra(ApplyActivity.EXTRA_APPLY, ApplyActivity._TN));
                break;
            }
            case R.id.freshLayout: {
                break;
            }
        }
    }

}

package com.yjy998.ui.activity.main.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.common.support.net.client.SRequest;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.common.util.NumberUtil;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.main.apply.ApplyActivity;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private TextView capitalText;
    private View layout;
    private HomeListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_home, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {

        capitalText = (TextView) findViewById(R.id.capitalText);

        findViewById(R.id.realGame).setOnClickListener(this);
        findViewById(R.id.newMember).setOnClickListener(this);
        findViewById(R.id.myGame).setOnClickListener(this);
        findViewById(R.id.TN).setOnClickListener(this);
        findViewById(R.id.T9).setOnClickListener(this);
        findViewById(R.id.safeLayout).setOnClickListener(this);
        findViewById(R.id.speedLayout).setOnClickListener(this);
        findViewById(R.id.proLayout).setOnClickListener(this);

        getCapital();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof HomeListener) {
            mCallback = (HomeListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.safeLayout: {
                break;
            }
            case R.id.speedLayout: {
                break;
            }
            case R.id.proLayout: {
                break;
            }

            case R.id.newMember: {
                startActivity(new Intent(getActivity(), NewMemberActivity.class));
                break;
            }

            case R.id.TN: {
                startActivity(new Intent(getActivity(), ApplyActivity.class)
                                .putExtra(ApplyActivity.EXTRA_APPLY, ApplyActivity._TN)
                );
                break;
            }
            case R.id.T9: {
                startActivity(new Intent(getActivity(), ApplyActivity.class)
                                .putExtra(ApplyActivity.EXTRA_APPLY, ApplyActivity._T9)
                );
                break;
            }
            default:
                //未处理的点击事件交给activity
                mCallback.onHomeFragmentClick(v);
        }
    }

    public interface HomeListener {
        public void onHomeFragmentClick(View v);
    }

    /**
     * 获取累计配资
     */
    public void getCapital() {
        SRequest request = new SRequest();
        request.setUrl("http://mobile.yjy998.com/h5/index/loanamount");
        YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                capitalText.setText("￥" + NumberUtil.formatStr(response.data));
            }
        });
    }
}

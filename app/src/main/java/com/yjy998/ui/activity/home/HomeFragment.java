package com.yjy998.ui.activity.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.common.util.ContextUtil;
import com.yjy998.R;
import com.yjy998.ui.activity.BaseFragment;


public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private TextView capitalText;
    private View layout;
    private HomeListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_home, container, false);
        initialize();
        return layout;
    }

    View findViewById(int id) {
        return layout.findViewById(id);
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

        ContextUtil.toast_debug(getResources().getResourceName(v.getId()));
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
            case R.id.realGame: {
                break;
            }
            case R.id.newMember: {
                break;
            }
            case R.id.myGame: {
                mCallback.onHomeFragmentClick(v);
                break;
            }
            case R.id.TN: {
                break;
            }
            case R.id.T9: {
                break;
            }
        }
    }

    public interface HomeListener {
        public void onHomeFragmentClick(View v);
    }
}

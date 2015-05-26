package com.yjy998.ui.activity.main.home;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.common.support.cache.CacheManager;
import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.widget.pager.BannerPager;
import com.sp.lib.widget.pager.TransformerB;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.common.util.NumberUtil;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.activity.main.apply.ApplyActivity;
import com.yjy998.ui.view.number.GrowNumber;
import com.yjy998.ui.view.number.MoneyGrow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private ArrayList<String> images = new ArrayList<String>();

    {
        //todo 下方仅用于测试，发布请注释
//        images.add("http://a.hiphotos.baidu.com/image/pic/item/eaf81a4c510fd9f9f7604cfb272dd42a2834a428.jpg");
//        images.add("http://e.hiphotos.baidu.com/image/pic/item/1b4c510fd9f9d72abcb25402d62a2834359bbbdf.jpg");
    }

    private TextView capitalText;
    private View layout;
    private HomeListener mCallback;
    private BannerPager bannerPager;
    private MoneyGrow capitalGrow;

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
        bannerPager = (BannerPager) findViewById(R.id.bannerPager);
        bannerPager.setDotDrawable(R.drawable.home_dot);
        findViewById(R.id.realGame).setOnClickListener(this);
        findViewById(R.id.newMember).setOnClickListener(this);
        findViewById(R.id.myGame).setOnClickListener(this);
        findViewById(R.id.TN).setOnClickListener(this);
        findViewById(R.id.T9).setOnClickListener(this);
        findViewById(R.id.safeLayout).setOnClickListener(this);
        findViewById(R.id.speedLayout).setOnClickListener(this);
        findViewById(R.id.proLayout).setOnClickListener(this);
        capitalGrow = new MoneyGrow(capitalText);
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
        images.clear();
        YHttpClient.getInstance().post(getActivity(), request, new YHttpHandler(false) {
            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONObject object = new JSONObject(response.data);
                    JSONArray imagesArray = object.getJSONArray("banners");
                    if (imagesArray != null && imagesArray.length() != 0) {
                        for (int i = 0; i < imagesArray.length(); i++) {
                            images.add(imagesArray.getString(i));
                        }
                    }
                    CacheManager.getInstance().write("banners", images);
                    setData(object.getString("loanamount"), images);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void onStatusFailed(Response response) {
                List<String> temp = (List<String>) CacheManager.getInstance().read("banners");
                if (temp!=null){
                    images.addAll(temp);
                }
                setData("0", images);
            }
        });
    }

    void setData(String loanAmount, List<String> images) {
        capitalGrow.setMax(NumberUtil.getFloat(loanAmount));
        capitalGrow.start();
        bannerPager.setImageUrls(images);
        bannerPager.setTransformer(new TransformerB(90));
        bannerPager.start(5000);
    }

}

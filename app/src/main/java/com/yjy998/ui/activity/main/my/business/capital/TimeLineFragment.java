package com.yjy998.ui.activity.main.my.business.capital;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.common.support.net.client.SRequest;
import com.sp.lib.common.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.http.Response;
import com.yjy998.common.http.YHttpClient;
import com.yjy998.common.http.YHttpHandler;
import com.yjy998.ui.activity.base.BaseFragment;
import com.yjy998.ui.view.chart.GLine;
import com.yjy998.ui.view.chart.GView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 分时线
 */
public class TimeLineFragment extends BaseFragment implements GView.OnPointTouchListener {


    List<Trend> list = new ArrayList<Trend>();
    TrendInfoWindow trendInfoWindow;
    float[] newPrice;
    float[] average;
    GView gView;
    GLine newLine = new GLine();
    GLine avLine = new GLine();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, null);
        gView = (GView) v.findViewById(R.id.gView);
        newLine.setDrawBelowColor(true);
        newLine.setStartColor(0xffB28B59);
        newLine.setEndColor(0xffEAE9E9);
        newLine.setLineColor(getResources().getColor(R.color.newPriceLineColor));
        newLine.setDrawToucheLine(true);
        gView.addLine(newLine);

        avLine.setDrawBelowColor(false);
        avLine.setLineColor(getResources().getColor(R.color.textColorRed));
        gView.addLine(avLine);

        gView.setOnPointTouchListener(this);
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void setStockCode(String code) {
        SRequest request = new SRequest();

        request.setUrl("http://interface.yjy998.com/yjy/quote/stock/" + code + "/trend_data");

        YHttpClient.getInstance().get(request, new YHttpHandler(false) {

            @Override
            protected void onStatusCorrect(Response response) {
                try {
                    JSONObject object = new JSONObject(response.data);
                    JSONArray array = object.getJSONArray("trendList");
                    addTrendListToChart(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void reset() {
        newPrice = new float[0];
        average = new float[0];
        newLine.setCreated(false);
        avLine.setCreated(false);
        newLine.setValues(newPrice);
        avLine.setValues(average);
        gView.invalidate();
    }

    /**
     * 将数据解析，并添加到图表中
     */
    private void addTrendListToChart(JSONArray array) throws JSONException {
        int length = array.length();
        if (length == 0) {
            length = 1;
        }
        newPrice = new float[length];
        average = new float[length];

        Trend firstTrend = JsonUtil.get(array.get(0).toString(), Trend.class);

        float max = 0;
        float firstPrice = firstTrend.newPrice;
        for (int i = 0; i < length; i++) {
            Trend trend = JsonUtil.get(array.getString(i), Trend.class);
            list.add(trend);
            newPrice[i] = trend.newPrice;
            average[i] = trend.averagePrice;

            max = Math.max(Math.abs(newPrice[i] - firstPrice), max);
        }
        newLine.setCreated(false);
        avLine.setCreated(false);
        newLine.setValues(newPrice);
        avLine.setValues(average);

        gView.setRangeY(firstPrice - max, firstPrice + max);
        gView.invalidate();
    }

    /**
     * 9.30-11.30
     * 13.00-15.00
     *
     * @param position
     */
    @Override
    public void onTouched(int position) {
        if (trendInfoWindow == null) {
            trendInfoWindow = new TrendInfoWindow(getActivity());
        }
        if (newPrice != null && newPrice.length > position && average != null && average.length > position) {
            String time;
            int h;
            int m;
            if (position < 30) {
                h = 9;
                m = 30 + position;
            } else if (position < 90) {
                h = 10;
                m = position - 30;
            } else if (position < 120) {
                h = 11;
                m = position - 90;
            } else {
                h = position / 60 + 11;
                m = position % 60;
            }

            trendInfoWindow.setText(newPrice[position] + "", average[position] + "", String.format("%d:%02d", h, m));
            trendInfoWindow.show(gView, position > 120);
        }

    }

    @Override
    public void onTouchCanceled() {
        if (trendInfoWindow != null) {
            trendInfoWindow.dismiss();
        }
    }


    class Trend {
        float newPrice;
        float averagePrice;
        float total;
    }
}

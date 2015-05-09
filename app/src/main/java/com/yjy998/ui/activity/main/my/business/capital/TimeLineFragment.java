package com.yjy998.ui.activity.main.my.business.capital;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    Toast toast;
    float[] newPrice;
    float[] average;
    GView gView;
    private TextView newPriceText;
    private TextView averagePriceText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, null);
        gView = (GView) v.findViewById(R.id.gView);
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void setStockCode(String code) {
        SRequest request = new SRequest();

        request.setUrl("http://interface.yjy998.com/yjy/quote/stock/" + code + "/trend_data");

        YHttpClient.getInstance().get(request, new YHttpHandler() {

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


    /**
     * 将数据解析，并添加到图表中
     */
    private void addTrendListToChart(JSONArray array) throws JSONException {
        int length = array.length();
        newPrice = new float[length];
        average = new float[length];

        Trend firstTrend = JsonUtil.get(array.get(0).toString(), Trend.class);

        float max = Math.max(firstTrend.newPrice, firstTrend.averagePrice);
        float min = Math.min(firstTrend.newPrice, firstTrend.averagePrice);

        for (int i = 0; i < length; i++) {
            Trend trend = JsonUtil.get(array.getString(i), Trend.class);
            list.add(trend);
            newPrice[i] = trend.newPrice;
            average[i] = trend.averagePrice;

            max = Math.max(newPrice[i], max);
            max = Math.max(average[i], max);

            min = Math.min(newPrice[i], min);
            min = Math.min(average[i], min);
        }


        GLine newLine = new GLine();
        newLine.setValues(newPrice);
        newLine.setDrawBelowColor(true);
        newLine.setStartColor(0xffB28B59);
        newLine.setEndColor(0xffEAE9E9);
        newLine.setLineColor(getResources().getColor(R.color.newPriceLineColor));
        newLine.setDrawToucheLine(true);
        gView.addLine(newLine);

        GLine avLine = new GLine();
        avLine.setValues(average);
        avLine.setDrawBelowColor(false);
        avLine.setLineColor(getResources().getColor(R.color.textColorRed));
        gView.addLine(avLine);

        float delta = (max - min) * 0.3f;
        gView.setRangeY(min - delta, max + delta);
        gView.setOnPointTouchListener(this);
    }

    @Override
    public void onTouched(int position) {
        if (toast == null) {
            toast = new Toast(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.toast_view, null);
            newPriceText = (TextView) view.findViewById(R.id.newPriceText);
            averagePriceText = (TextView) view.findViewById(R.id.averagePriceText);
            toast.setView(view);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,30);
        }
        if (newPrice != null && average != null) {

            newPriceText.setText(getString(R.string.new_price_s, newPrice[position] + ""));
            averagePriceText.setText(getString(R.string.average_price_s, average[position] + ""));
            toast.show();
        }

    }


    class Trend {
        float newPrice;
        float averagePrice;
        float total;
    }
}

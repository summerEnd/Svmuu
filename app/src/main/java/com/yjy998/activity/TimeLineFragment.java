package com.yjy998.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sp.lib.support.net.client.SRequest;
import com.sp.lib.support.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.http.YJYHttpClient;
import com.yjy998.common.http.YJYHttpHandler;
import com.yjy998.view.TimeLineCover;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 分时线
 */
public class TimeLineFragment extends BaseFragment implements View.OnClickListener {

    /**
     * The chart view that displays the data.
     */
    private GraphicalView mChartView;
    XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
    XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private int TIME_UNIT = 30;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_chart_container, null);
        mChartView = ChartFactory.getLineChartView(getActivity(), dataSet, mRenderer);
        mChartView.setOnClickListener(this);
        mChartView.setBackgroundColor(Color.LTGRAY);
        layout.addView(mChartView);
        layout.addView(new TimeLineCover(getActivity()) {
            @Override
            protected void onSelect(float touchPoint) {

            }
        });
        return layout;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initRender();
        SRequest request = new SRequest();
        request.setUrl("https://yjy998.com/yjy/quote/stock/601899/trend_data");
        YJYHttpClient.get(request, new YJYHttpHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray array = data.getJSONArray("trendList");
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
    @SuppressWarnings("deprecation")
    private void addTrendListToChart(JSONArray array) throws JSONException {
        XYSeries newPrice = new XYSeries(getString(R.string.newest_price));
        XYSeries average = new XYSeries(getString(R.string.average_price));
        int length = array.length();
        int amLength = Math.min(length, 120);

        //9:30-11:30
        for (int i = 0; i < amLength; i++) {
            Trend trend = JsonUtil.get(array.getString(i), Trend.class);
            newPrice.add(i, trend.newPrice);
            average.add(i, trend.averagePrice);
        }

        if (length >= 120) {
            for (int i = 120; i < length; i++) {
                Trend trend = JsonUtil.get(array.getString(i), Trend.class);
                newPrice.add(i, trend.newPrice);
                average.add(i, trend.averagePrice);
            }
        }

        dataSet.addSeries(newPrice);
        dataSet.addSeries(average);
        XYSeriesRenderer newPriceRender = createSeriesRender(getResources().getColor(R.color.newPriceLineColor));
        XYSeriesRenderer averageRender = createSeriesRender(getResources().getColor(R.color.averagePriceLineColor));
        newPriceRender.setFillBelowLine(true);
        newPriceRender.setFillBelowLineColor(getResources().getColor(R.color.fillBelowColor));
        mRenderer.addSeriesRenderer(newPriceRender);
        mRenderer.addSeriesRenderer(averageRender);
        mChartView.repaint();
    }

    /**
     * 初始化render参数
     */
    private void initRender() {

        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[]{0, 0, 0, 0});
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setPointSize(3);
        mRenderer.setXLabels(0);
        mRenderer.setShowGrid(true);

        //边界颜色
        mRenderer.setMarginsColor(Color.GRAY);
        //设置起始时间为9:30
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 30);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        //中午停盘时间
        int noonSpace = 120 / TIME_UNIT;
        //结束时间
        int end = 240 / TIME_UNIT;

        for (int i = 0; i <= end; i++) {
            if (i == noonSpace) {
                mRenderer.addXTextLabel(i * TIME_UNIT, "11:30/13:00");
                calendar.add(Calendar.MINUTE, 90 + TIME_UNIT);

            } else {
                mRenderer.addXTextLabel(i * TIME_UNIT, sdf.format(calendar.getTime()));
                calendar.add(Calendar.MINUTE, TIME_UNIT);
            }
        }
    }

    private XYSeriesRenderer createSeriesRender(int color) {
        XYSeriesRenderer render = new XYSeriesRenderer();
        render.setPointStyle(PointStyle.POINT);
        render.setFillPoints(true);
        render.setDisplayChartValues(false);
        render.setDisplayChartValuesDistance(10);
        render.setColor(color);

        return render;
    }

    @Override
    public void onClick(View v) {
        SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
        if (seriesSelection != null) {
            // display information of the clicked point
            Toast.makeText(
                    getActivity(),
                    "Chart element in series index " + seriesSelection.getSeriesIndex()
                            + " data point index " + seriesSelection.getPointIndex() + " was clicked"
                            + " closest point value X=" + seriesSelection.getXValue() + ", Y="
                            + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
        }
    }

    class Trend {
        float newPrice;
        float averagePrice;
        float total;
    }
}

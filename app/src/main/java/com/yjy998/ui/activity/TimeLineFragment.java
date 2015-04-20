package com.yjy998.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.yjy998.R;
import com.yjy998.common.Constant;
import com.yjy998.view.TimeLineCover;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.PanListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 分时线
 */
public class TimeLineFragment extends BaseFragment {

    private GraphicalView mChartView;
    XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
    XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    /**
     * x轴时间间隔，单位：分
     */
    private int TIME_UNIT = 30;
    List<Trend> list = new ArrayList<Trend>();

    float coverX;
    private TextView mText;
    ChartCover mCover;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, null);
        mChartView = ChartFactory.getLineChartView(getActivity(), dataSet, mRenderer);
        mChartView.setBackgroundColor(Color.LTGRAY);
        mCover = new ChartCover(getActivity());

        mChartView.addPanListener(new PanListener() {
            @Override
            public void panApplied() {
                mCover.onSelect(coverX);
            }
        });
        mText = (TextView) v.findViewById(R.id.chartInfo);

        ViewGroup layout = (ViewGroup) v.findViewById(R.id.chartContainer);
        layout.addView(mChartView);
        layout.addView(mCover);
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initRender();
//        SRequest request = new SRequest();
//        request.setUrl("https://yjy998.com/yjy/quote/stock/601899/trend_data");
//        YJYHttpClient.get(request, new YJYHttpHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                try {
//                    JSONObject data = response.getJSONObject("data");
//                    JSONArray array = data.getJSONArray("trendList");
//                    addTrendListToChart(array);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        try {
            JSONObject data = new JSONObject(Constant.DATA);
            JSONArray array = data.getJSONObject("data").getJSONArray("trendList");
            addTrendListToChart(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将数据解析，并添加到图表中
     */
    @SuppressWarnings("deprecation")
    private void addTrendListToChart(JSONArray array) throws JSONException {
        XYSeries newPrice = new XYSeries(getString(R.string.newest_price));
        XYSeries average = new XYSeries(getString(R.string.average_price));
        for (int i = 0; i < array.length(); i++) {
            Trend trend = JsonUtil.get(array.getString(i), Trend.class);
            list.add(trend);
            newPrice.add(i, format(trend.newPrice));
            average.add(i, format(trend.averagePrice));
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

    float format(float value) {
//        float v = new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return value;
    }

    /**
     * 初始化render参数
     */
    private void initRender() {
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);

        mRenderer.setLabelsTextSize(15);
        mRenderer.setXLabelsColor(Color.RED);
        mRenderer.setXLabels(0);
        mRenderer.setXAxisMin(0);
        mRenderer.setXAxisMax(255);
        //顺序上左下右
        mRenderer.setMargins(new int[]{0, 20, 0, 0});
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setPointSize(3);
        mRenderer.setClickEnabled(true);
        mRenderer.setSelectableBuffer(20);
        mRenderer.setShowGrid(true);
        //边界颜色
        mRenderer.setMarginsColor(Color.WHITE);
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
        render.setColor(color);
        return render;
    }


    private class ChartCover extends TimeLineCover {
        public ChartCover(Context context) {
            super(context);
        }

        @Override
        public int getChartHeight() {

            return super.getChartHeight();
        }

        @Override
        protected float onSelect(float touchX) {
            //上左右下margin
            int margins[] = mRenderer.getMargins();
//            mRenderer.getChartHeight()
            float xInChart = touchX - margins[1];
            int startPosition = (int) mRenderer.getXAxisMin();

            //当前屏幕坐标点数
            int axisPointNumber = (int) (mRenderer.getXAxisMax() - mRenderer.getXAxisMin());
            //当前屏幕x轴宽度
            float axisWidth = getWidth() - margins[1];

            int offset = (int) ((xInChart / axisWidth) * axisPointNumber);

            int position = startPosition + offset;

            position = Math.max(0, position);
            position = Math.min(list.size() - 1, position);

            Trend trend = list.get(position);
            String string = getContext().getString(R.string.chartInfo, trend.newPrice, trend.averagePrice, position);
            mText.setText(string);
            SLog.debug_format(string);

            /**
             * 点击的position转化为屏幕坐标,
             */
            coverX = ((position - startPosition) / (float) axisPointNumber) * axisWidth + margins[1];

            return coverX;
        }
    }

    class Trend {
        float newPrice;
        float averagePrice;
        float total;
    }
}

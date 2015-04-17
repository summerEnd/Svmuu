package com.yjy998.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sp.lib.common.util.ContextUtil;
import com.sp.lib.common.util.JsonUtil;
import com.sp.lib.common.util.SLog;
import com.yjy998.R;
import com.yjy998.common.Constant;
import com.yjy998.view.TimeLineCover;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.PanListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 分时线
 */
public class TimeLineFragment extends BaseFragment {

    /**
     * The chart view that displays the data.
     */
    private GraphicalView mChartView;
    XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
    XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private int TIME_UNIT = 30;
    List<Trend> list = new ArrayList<Trend>();
    /**
     * 右侧边距
     */
    private final int LEFT_MARGIN = 15;
    float coverX;
    private TextView mText;
    ChartCover mCover;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_chart_container, null);
        mChartView = ChartFactory.getLineChartView(getActivity(), dataSet, mRenderer);
        mChartView.setBackgroundColor(Color.LTGRAY);
        mCover = new ChartCover(getActivity());

        mChartView.addPanListener(new PanListener() {
            @Override
            public void panApplied() {
                mCover.onSelect(coverX);
            }
        });

        mText = new TextView(getActivity());
        layout.addView(mChartView);
        layout.addView(mCover);
        layout.addView(mText, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM));
        return layout;
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
        int length = array.length();
        int amLength = Math.min(length, 120);
        //9:30-11:30
        for (int i = 0; i < array.length(); i++) {
            Trend trend = JsonUtil.get(array.getString(i), Trend.class);
            list.add(trend);
            newPrice.add(i, format(trend.newPrice));
            average.add(i, format(trend.averagePrice));
        }

//        if (length >= 120) {
//            for (int i = 120; i < length; i++) {
//                Trend trend = JsonUtil.get(array.getString(i), Trend.class);
//                list.add(trend);
//                newPrice.add(i, format(trend.newPrice));
//                average.add(i, format(trend.averagePrice));
//            }
//        }
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
//        BigDecimal decimal = new BigDecimal(value);
//        float v = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
//        Log.i("value","v:"+value);
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

        mRenderer.setMargins(new int[]{LEFT_MARGIN, 15, 0, 0});
        mRenderer.setZoomButtonsVisible(false);
        mRenderer.setPointSize(3);
        mRenderer.setClickEnabled(true);
        mRenderer.setSelectableBuffer(20);
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
        render.setColor(color);
        return render;
    }


    private class ChartCover extends TimeLineCover {
        public ChartCover(Context context) {
            super(context);
        }

        @Override
        protected void onSelect(float touchX) {
            coverX = touchX;
            float xInChart = touchX - LEFT_MARGIN;
            int startX = (int) mRenderer.getXAxisMin();

            //当前屏幕坐标点数
            int axisPointNumber = (int) (mRenderer.getXAxisMax() - mRenderer.getXAxisMin());
            //当前屏幕x轴宽度
            float axisWidth = getWidth() - LEFT_MARGIN;

            int offset = (int) ((xInChart / axisWidth) * axisPointNumber);

            int position = startX + offset;

            position = Math.max(0, position);
            position = Math.min(list.size() - 1, position);

            Trend trend = list.get(position);
            String o = "position:" + position + " newPrice:" + trend.newPrice + " avePrice:" + trend.averagePrice;
            SLog.debug_format(o);
            mText.setText(o);
        }
    }

    class Trend {
        float newPrice;
        float averagePrice;
        float total;
    }
}

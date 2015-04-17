package com.yjy998.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;

import com.sp.lib.support.net.client.SRequest;
import com.sp.lib.support.util.JsonUtil;
import com.yjy998.R;
import com.yjy998.common.http.YJYHttpClient;
import com.yjy998.common.http.YJYHttpHandler;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
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

public class ChartActivity extends YJYActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        getFragmentManager().beginTransaction().add(R.id.chartContainer, new TimeLineFragment()).commit();
    }


}

package com.yjy998.ui.activity.test;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.yjy998.R;
import com.yjy998.common.Constant;
import com.yjy998.ui.activity.YJYActivity;
import com.yjy998.ui.view.chart.GLine;
import com.yjy998.ui.view.chart.GView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChartActivity extends YJYActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        View v = getLayoutInflater().inflate(R.layout.test, null);
        setContentView(v);
        GView gView = findView(R.id.gView);

        try {
            JSONObject data = new JSONObject("");
            JSONArray array = data.getJSONObject("data").getJSONArray("trendList");
            int length = array.length();
            int displayLength=200;
            float[] newPrices = new float[displayLength];
            float[] averagePrices = new float[displayLength];
            JSONObject first = array.getJSONObject(0);
            float max = Math.max((float) first.getDouble("newPrice"), (float) first.getDouble("averagePrice"));
            float min = Math.min((float) first.getDouble("newPrice"), (float) first.getDouble("averagePrice"));

            for (int i = 0; i < displayLength; i++) {
                JSONObject object = array.getJSONObject(i);
                newPrices[i] = (float) object.getDouble("newPrice");
                averagePrices[i] = (float) object.getDouble("averagePrice");
                max = Math.max(newPrices[i], max);
                max = Math.max(averagePrices[i], max);

                min = Math.min(newPrices[i], min);
                min = Math.min(averagePrices[i], min);

            }

            GLine newLine = new GLine();
            newLine.setValues(newPrices);
            newLine.setDrawBelowColor(true);
            newLine.setStartColor(0xffB28B59);
            newLine.setEndColor(0xffEAE9E9);
            newLine.setLineColor(Color.YELLOW);
            gView.addLine(newLine);

            GLine avLine = new GLine();
            avLine.setValues(averagePrices);
            avLine.setDrawBelowColor(false);
            avLine.setLineColor(Color.RED);
            gView.addLine(avLine);

            float delta = (max - min) * 0.3f;
            gView.setRangeY(min - delta, max + delta);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

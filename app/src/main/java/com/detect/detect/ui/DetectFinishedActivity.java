package com.detect.detect.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.detect.detect.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yu on 18/1/18.
 */

public class DetectFinishedActivity extends BaseActivity {
    private LineChart mChart;
    private ArrayList<int[]> waveDataList;
    private static final boolean isTestMode = true;
    private ListView waveDataLv;

    @Override
    protected void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detect_finish;
    }

    @Override
    public void initView() {
        //

        Intent intent = getIntent();
        if (intent != null) {
            waveDataList = (ArrayList<int[]>) intent.getSerializableExtra("WAVE_DATA");
            if (isTestMode) {
                addTestData();
            }
        }
        ArrayList<Integer> allWaveDataList = new ArrayList<>();
        for (int[] waveArr : waveDataList) {
            for (int wave : waveArr) {
                allWaveDataList.add(wave);
            }
        }
        waveDataLv = findViewById(R.id.wave_data_lv);
        waveDataLv.setAdapter(new WaveDatasAdapter(this, allWaveDataList));

        mChart = findViewById(R.id.line_char);
        mChart.setViewPortOffsets(0, 0, 0, 0);
        mChart.setBackgroundColor(Color.parseColor("#A9A9A9"));

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setMaxHighlightDistance(300);

//        XAxis x = mChart.getXAxis();
//        x.setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setLabelCount(40);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.WHITE);

        YAxis y = mChart.getAxisLeft();
//        y.setTypeface(mTfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);

        mChart.getAxisRight().setEnabled(false);

        // add data
        setData(1000, 4096);

        mChart.getLegend().setEnabled(false);

        mChart.animateXY(2000, 2000);
        mChart.setVisibleXRange(0.0f, 40.0f);


        for (IDataSet set : mChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        mChart.invalidate();


        // dont forget to refresh the drawing
        mChart.invalidate();

    }

    private void addTestData() {
        waveDataList.remove(2);
        int[] ints = new int[1000];
        for (int i = 0; i < 1000; i++) {
            int round = (int) (Math.random() * 50000);
            ints[i] = round;
        }
        waveDataList.add(ints);
    }

    private static final String TAG = "DetectFinishedActivity";

    private void setData(int count, float range) {
        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();
        ArrayList<Entry> yVals3 = new ArrayList<>();
        int[] intArr1 = waveDataList.get(0);
        int[] intArr2 = waveDataList.get(1);
        int[] intArr3 = waveDataList.get(2);
        for (int i = 0; i < intArr1.length; i++) {
            yVals1.add(new Entry(i, intArr1[i]));
        }

        for (int i = 0; i < intArr2.length; i++) {
            yVals2.add(new Entry(i, intArr2[i]));
        }
        for (int i = 0; i < intArr3.length; i++) {
            yVals3.add(new Entry(i, intArr3[i]));
        }
//        ArrayList<Entry> yVals = new ArrayList<Entry>();
//        for (int i = 0; i < count; i++) {
//            float mult = (range + 1);
//            float val = (float) (Math.random() * mult) + 20;// + (float)
//            // ((mult *
//            // 0.1) / 10);
//            Log.d(TAG, "setData: val: " + val);
//            yVals.add(new Entry(i, val));
//        }

        LineDataSet set1, set2, set3;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() >= 3) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) mChart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");
            set2 = new LineDataSet(yVals2, "DataSet 2");
            set3 = new LineDataSet(yVals3, "DataSet 3");
            getSetList(set1);
            getSetList(set2);
            getSetList(set3);

            // create a data object with the datasets
            LineData data = new LineData(set1, set2, set3);
//            data.setValueTypeface(mTfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            mChart.setData(data);
        }
    }

    private void getSetList(LineDataSet set) {
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        //set1.setDrawFilled(true);
        set.setDrawCircles(false);
        set.setLineWidth(1.8f);
        set.setCircleRadius(4f);
        set.setCircleColor(Color.WHITE);
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setColor(Color.WHITE);
        set.setFillColor(Color.WHITE);
        set.setFillAlpha(100);
        set.setDrawHorizontalHighlightIndicator(false);
        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return -10;
            }
        });
    }
}

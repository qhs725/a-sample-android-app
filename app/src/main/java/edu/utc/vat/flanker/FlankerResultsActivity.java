/**
 * rg 1/26/16
 */

package edu.utc.vat.flanker;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.utc.vat.CallNative;
import edu.utc.vat.R;
import edu.utc.vat.TestingActivity;

public class FlankerResultsActivity extends TestingActivity implements View.OnClickListener {

    private static final int PF1 = 0;
    private static final int PF2 = 1;
    private static final int DEFAULT = 0;
    private static int STATE = DEFAULT;

    private Toast newToast;

    private double T = 0.;
    private double yMax = 0., yMin = 0.;
    private int gCount;

    private XYSeries xSeries = new XYSeries("X");
    private XYSeries ySeries = new XYSeries("Y");
    private XYSeries zSeries = new XYSeries("Z");

    private LinearLayout layout;
    private Button pf1Button, pf2Button;
    private View currentChart;

    int[] responses = {0, 0, 0};
    double responseTime = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        setContentView(R.layout.flanker_results);
        layout = (LinearLayout) findViewById(R.id.flanker_container);

        pf1Button = (Button) findViewById(R.id.pf1);
        pf2Button = (Button) findViewById(R.id.pf2);

        pf1Button.setOnClickListener(this);
        pf2Button.setOnClickListener(this);

        boolean loadFlag = loadResults();
        if (!loadFlag)
            Log.e("FlankerResults", "Couldn't load files to view");

        Log.i("FlankerResults", "calling openPlot from onCreate");
        openChart();
        Log.i("FlankerResults", "returning to onCreate from openPlot");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DialogFragment uploadData = new UploadFlankerDialogFragment();
        CallNative.SetFlankerFlag(false);
        uploadData.show(getFragmentManager(), "uploadData");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pf1:
                STATE = PF1;
                layout.removeAllViews();
                openChart();
                break;
            case R.id.pf2:
                STATE = PF2;
                layout.removeAllViews();
                openPlot();
                break;
        }
    }

    private boolean loadResults() {
        Log.i("FlankerResults", "loadResults 1");
        int ct = 0;
        // First, scan g.dat for gyroscope data
        Scanner scanGyro;
        try {
            scanGyro = new Scanner(new File("/data/data/edu.utc.vat/files/g.dat"));
        } catch (FileNotFoundException e) {
            Log.e("FlankerResults", "g.dat not accessible");
            return false;
        }
        Log.i("FlankerResults", "loadResults 2");
        gCount = CallNative.CountGyro();
        scanGyro.useDelimiter("\n");
        ct = 0;
        Log.i("FlankerResults", "loadResults 3");
        try {
            while (scanGyro.hasNext()) {
                Scanner scanLine;
                scanLine = new Scanner(scanGyro.next());
                scanLine.useDelimiter(",");
                float x, y, z;
                double t;
                if (ct > 0) {
                    x = Float.parseFloat(scanLine.next());
                    y = Float.parseFloat(scanLine.next());
                    z = Float.parseFloat(scanLine.next());
                    if (ct == 1) {
                        T = Double.parseDouble(scanLine.next()) - 1000000.f;
                        t = T - T;
                    } else {
                        t = Double.parseDouble(scanLine.next()) - 1000000.f - T;
                    }
                    try {
                        xSeries.add(t / 1000.f, x);
                        ySeries.add(t / 1000.f, y);
                        zSeries.add(t / 1000.f, z);
                    } catch (Exception e) {
                        Log.getStackTraceString(e);
                    }
                    yMax = Math.max(yMax, getMax(x, y, z));
                    yMin = Math.min(yMin, getMin(x, y, z));
                }
                ct++;
            }
        } catch (Exception e) {
            Log.getStackTraceString(e);
        }
        Log.i("ViewResults", "Gyro data lines count %d" + gCount);
        Log.i("ViewResults", "Gyro values count %d" + ct);

        Scanner scanF;
        try {
            scanF = new Scanner(new File("/data/data/edu.utc.vat/files/f.dat"));
        } catch (FileNotFoundException e) {
            Log.e("ViewResults", "f.dat not accessible");
            return false;
        }
        scanF.useDelimiter("\n");
        ct = 0;
        while (scanF.hasNext()) {
            Scanner scanLine;
            scanLine = new Scanner(scanF.next());
            scanLine.useDelimiter(",");
            int s, r;
            float rt;
            if (ct > 0) {
                s = Integer.parseInt(scanLine.next());
                r = Integer.parseInt(scanLine.next());
                rt = Float.parseFloat(scanLine.next());
                if (r > 0) {
                    responses[0]++;
                    responseTime += rt;
                } else if (r < 0)
                    responses[1]++;
                else
                    responses[2]++;
            }
            ct++;
        }
        Log.i("FlankerView","Flanker count "+ct);
        responseTime = responseTime/1.0e6;
        responseTime = responseTime/((double)responses[0]);
        Log.i("FlankerView","Flanker time " + responseTime);

        //TODO: READ FLANKER DATA AND COMPUTE METRICS TO DISPLAY

        Log.i("FlankerResults", "returning from loadResults");
        return true;
    }

    private void openChart() {
        Log.i("FlankerResults","calling openChart");
        showToast("Average response time " + responseTime);
        String[] code = new String[] {
                "Correct", "Incorrect" , "No Response"
        };
        double[] distribution = {33.33, 33.33, 33.33};
        distribution[0] = ((double) (responses[0])) / 16.;
        distribution[1] = ((double) responses[1]) / 16.;
        distribution[2] = ((double) responses[2]) / 16.;
        int[] colors = {Color.GREEN, Color.RED, Color.BLUE};
        CategorySeries results = new CategorySeries("Flanker Results");
        for (int i = 0; i < distribution.length; i++) {
            results.add(code[i], distribution[i]);
        }
        DefaultRenderer renderer = new DefaultRenderer();
        for (int i = 0; i < distribution.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(colors[i]);
            seriesRenderer.setDisplayChartValues(true);
            renderer.addSeriesRenderer(seriesRenderer);
        }
        renderer.setChartTitle("Flanker Results");
        renderer.setChartTitleTextSize(48.f);
        renderer.setZoomButtonsVisible(false);
        renderer.setLabelsTextSize(40.f);
        renderer.setLegendTextSize(32.f);

        currentChart = ChartFactory.getPieChartView(getBaseContext(), results, renderer);

        layout.addView(currentChart);

        Log.i("FlankerResults", "returning from openChart");
    }

    private void openPlot() {
        showToast("Average response time " + responseTime);
        Log.i("FlankerResults","calling openPlot");
        XYMultipleSeriesDataset data = new XYMultipleSeriesDataset();
        data.addSeries(xSeries);
        data.addSeries(ySeries);
        data.addSeries(zSeries);

        XYSeriesRenderer xRender = new XYSeriesRenderer();
        xRender.setColor(Color.RED);
        xRender.setPointStyle(PointStyle.CIRCLE);
        xRender.setFillPoints(true);
        xRender.setLineWidth(1);
        xRender.setDisplayChartValues(false);

        XYSeriesRenderer yRender = new XYSeriesRenderer();
        yRender.setColor(Color.GREEN);
        yRender.setPointStyle(PointStyle.CIRCLE);
        yRender.setFillPoints(true);
        yRender.setLineWidth(1);
        yRender.setDisplayChartValues(false);

        XYSeriesRenderer zRender = new XYSeriesRenderer();
        zRender.setColor(Color.BLUE);
        zRender.setPointStyle(PointStyle.CIRCLE);
        zRender.setFillPoints(true);
        zRender.setLineWidth(1);
        zRender.setDisplayChartValues(false);

        XYMultipleSeriesRenderer multiRender = new XYMultipleSeriesRenderer();
        multiRender.setXLabels(0);
        multiRender.setLabelsColor(Color.WHITE);

        multiRender.setChartTitle("\n" + exerciseName + ":\n\nGyroscopic Rotation vs Time");

        multiRender.setXTitle("Time");
        multiRender.setYTitle("Sensor Values");
        float axisTextSize = multiRender.getAxisTitleTextSize();
        multiRender.setAxisTitleTextSize(48.f);
        multiRender.setChartTitleTextSize(48.f);
        multiRender.setLabelsTextSize(32.f);
        multiRender.setLegendTextSize(40.f);
        int[] margins = new int[]{230, 90, 70, 90};
        multiRender.setMargins(margins);

        multiRender.setYAxisMin(yMin - 2.);
        multiRender.setYAxisMax(yMax + 2.);

        multiRender.addSeriesRenderer(xRender);
        multiRender.addSeriesRenderer(yRender);
        multiRender.addSeriesRenderer(zRender);

        currentChart = ChartFactory.getLineChartView(getBaseContext(), data, multiRender);

        layout.addView(currentChart);

        Log.i("FlankerResults", "returning from openPlot");
    }

    private double getMax(float x, float y, float z) {
        double xd = (double) x;
        double yd = (double) y;
        double zd = (double) z;
        double mx = Math.max(xd, zd);
        return Math.max(yd, mx);
    }

    private double getMin(float x, float y, float z) {
        double xd = (double) x;
        double yd = (double) y;
        double zd = (double) z;
        return Math.min(xd, Math.min(yd, zd));
    }

    void showToast(String message) {
        if (newToast != null) {
            newToast.cancel();
        }
        newToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        newToast.show();
    }

}

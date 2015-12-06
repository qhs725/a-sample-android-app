/**
 * Sports Injury Prevention Screening -- SIPS
 * v0.01.1b (12/3/15)
 * rg 12/2/15.
 */

package edu.utc.vat.post.test;

import android.app.DialogFragment;

import android.graphics.Color;

import android.os.Bundle;

import android.view.View;

import android.widget.LinearLayout;
import android.widget.Button;

import android.util.Log;

import edu.utc.vat.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class ViewResultsActivity extends TestingActivity implements View.OnClickListener {

    private static final int ACCELERATION = 0;
    private static final int ROTATION = 1;
    private static final int MAGNETIC = 2;
    private static final int DEFAULT = 0;
    private static int STATE = DEFAULT;

    private int aCount, gCount, cCount;
    private int jumpCount = 0, jumpMax = 0;
    private float T = 0.f;
    private double yaMax = 0., yaMin = 0.;
    private double yrMax = 0., yrMin = 0.;
    private double ymMax = 0., ymMin = 0.;

    private XYSeries axSeries = new XYSeries("X");
    private XYSeries aySeries = new XYSeries("Y");
    private XYSeries azSeries = new XYSeries("Z");
    private XYSeries rxSeries = new XYSeries("X");
    private XYSeries rySeries = new XYSeries("Y");
    private XYSeries rzSeries = new XYSeries("Z");
    private XYSeries mxSeries = new XYSeries("X");
    private XYSeries mySeries = new XYSeries("Y");
    private XYSeries mzSeries = new XYSeries("Z");

    private LinearLayout layout;
    private Button accelButton, gyroButton, compassButton;
    private View currentChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_results);
        layout = (LinearLayout) findViewById(R.id.chart_container);

        accelButton = (Button) findViewById(R.id.accel);
        gyroButton = (Button) findViewById(R.id.gyro);
        compassButton = (Button) findViewById(R.id.compass);

        accelButton.setOnClickListener(this);
        gyroButton.setOnClickListener(this);
        compassButton.setOnClickListener(this);

        boolean loadFlag = loadResults();
        if (!loadFlag)
            Log.e("ViewResults","Couldn't load files to view");

        openChart();

        //DialogFragment uploadData = new edu.utc.vat.post.test.UploadDataDialogFragment();
        //uploadData.show(getFragmentManager(), "uploadData");
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
        DialogFragment uploadData = new ViewUploadDataDialogFragment();
        uploadData.show(getFragmentManager(), "uploadData");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accel:
                STATE = ACCELERATION;
                layout.removeAllViews();
                openChart();
                break;
            case R.id.gyro:
                STATE = ROTATION;
                layout.removeAllViews();
                openChart();
                break;
            case R.id.compass:
                STATE = MAGNETIC;
                layout.removeAllViews();
                openChart();
                break;
        }
    }


    private boolean loadResults() {
        // First, scan a.dat for accelerometer data
        Scanner scanAccel;
        try {
            scanAccel = new Scanner(new File("/data/data/edu.utc.vat/files/a.dat"));
        } catch (FileNotFoundException e) {
            Log.e("ViewResults","a.dat not accessible");
            return false;
        }
        aCount = CallNative.CountAccel();
        scanAccel.useDelimiter("\n");
        int ct = 0;
        while(scanAccel.hasNext()) {
            Scanner scanLine;
            scanLine = new Scanner(scanAccel.next());
            scanLine.useDelimiter(",");
            float x, y, z, t;
            // a.dat -- ignoring first line; header
            if (ct > 0) {
                x = Float.parseFloat(scanLine.next());
                y = Float.parseFloat(scanLine.next());
                z = Float.parseFloat(scanLine.next());
                if (ct == 1) {
                    T = Float.parseFloat(scanLine.next()) - 1000000.f;
                    t = T-T;
                } else {
                    t = Float.parseFloat(scanLine.next()) - 1000000.f - T;
                }
                axSeries.add(t/1000.f, x);
                aySeries.add(t/1000.f, y);
                azSeries.add(t/1000.f, z);
                jumpHeight(x, y, z);
                yaMax = Math.max(yaMax, getMax(x, y, z));
                yaMin = Math.min(yaMin, getMin(x, y, z));
            }
            ct++;
        }
        Log.i("ViewResults","Accel data lines count %d" + aCount);
        Log.i("ViewResults","Accel values count %d" + ct);

        // Second, scan g.dat for gyroscope data
        Scanner scanGyro;
        try {
            scanGyro = new Scanner(new File("/data/data/edu.utc.vat/files/g.dat"));
        } catch (FileNotFoundException e) {
            Log.e("ViewResults","g.dat not accessible");
            return false;
        }
        gCount = CallNative.CountGyro();
        scanGyro.useDelimiter("\n");
        ct = 0;
        while(scanGyro.hasNext()) {
            Scanner scanLine;
            scanLine = new Scanner(scanGyro.next());
            scanLine.useDelimiter(",");
            float x, y, z, t;
            // g.dat -- ignoring first line; header
            if (ct > 0) {
                x = Float.parseFloat(scanLine.next());
                y = Float.parseFloat(scanLine.next());
                z = Float.parseFloat(scanLine.next());
                if (ct == 1) {
                    T = Float.parseFloat(scanLine.next()) - 1000000.f;
                    t = T-T;
                } else {
                    t = Float.parseFloat(scanLine.next()) - 1000000.f - T;
                }
                rxSeries.add(t/1000.f, x);
                rySeries.add(t/1000.f, y);
                rzSeries.add(t/1000.f, z);
                yrMax = Math.max(yrMax, getMax(x, y, z));
                yrMin = Math.min(yrMin, getMin(x, y, z));
            }
            ct++;
        }
        Log.i("ViewResults","Gyro data lines count %d" + gCount);
        Log.i("ViewResults","Gyro values count %d" + ct);

        // Third, scan c.dat for magnetic field data
        Scanner scanCompass;
        try {
            scanCompass = new Scanner(new File("/data/data/edu.utc.vat/files/c.dat"));
        } catch (FileNotFoundException e) {
            Log.e("ViewResults","c.dat not accessible");
            return false;
        }
        cCount = CallNative.CountCompass();
        scanCompass.useDelimiter("\n");
        ct = 0;
        while(scanCompass.hasNext()) {
            Scanner scanLine;
            scanLine = new Scanner(scanCompass.next());
            scanLine.useDelimiter(",");
            float x, y, z, t = 0.f;
            // c.dat -- ignoring first line; header
            if (ct > 0) {
                x = Float.parseFloat(scanLine.next());
                y = Float.parseFloat(scanLine.next());
                z = Float.parseFloat(scanLine.next());
                if (ct == 1) {
                    T = Float.parseFloat(scanLine.next()) - 1000000.f;
                    t = T-T;
                } else {
                    t = Float.parseFloat(scanLine.next()) - 1000000.f - T;
                }
                mxSeries.add(t/1000.f, x);
                mySeries.add(t/1000.f, y);
                mzSeries.add(t/1000.f, z);
                ymMax = Math.max(ymMax, getMax(x, y, z));
                ymMin = Math.min(ymMin, getMin(x, y, z));
            }
            ct++;
        }
        Log.i("ViewResults","Compass data lines count %d" + cCount);
        Log.i("ViewResults","Compass values count %d" + ct);

        Log.i("ViewResults","loadResults complete");
        return true;
    }


    private void openChart() {
        XYMultipleSeriesDataset data = new XYMultipleSeriesDataset();
        switch (STATE) {
            case ACCELERATION:
                data.addSeries(axSeries);
                data.addSeries(aySeries);
                data.addSeries(azSeries);
                break;
            case ROTATION:
                data.addSeries(rxSeries);
                data.addSeries(rySeries);
                data.addSeries(rzSeries);
                break;
            case MAGNETIC:
                data.addSeries(mxSeries);
                data.addSeries(mySeries);
                data.addSeries(mzSeries);
                break;
        }

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
        switch (STATE) {
            case ACCELERATION:
                if (exercise == SINGLE_LEG_JUMP) {
                    int inches = (int) getJumpHeight(aCount, JUMP_TESTING_TIME);
                    multiRender.setChartTitle(exerciseName + "\n" + Integer.toString(inches) + " inch vertical jump" + "\n\nAcceleration vs Time");
                } else
                    multiRender.setChartTitle("\n" + exerciseName + ":\n\nAcceleration vs Time");
                break;
            case ROTATION:
                multiRender.setChartTitle("\n" + exerciseName + ":\n\nGyroscopic Rotation vs Time");
                break;
            case MAGNETIC:
                multiRender.setChartTitle("\n" + exerciseName + ":\n\nMagnetic Field vs Time");
                break;
        }
        multiRender.setXTitle("Time");
        multiRender.setYTitle("Sensor Values");
        float axisTextSize = multiRender.getAxisTitleTextSize();
        multiRender.setAxisTitleTextSize(48.f);
        multiRender.setChartTitleTextSize(48.f);
        multiRender.setLabelsTextSize(32.f);
        multiRender.setLegendTextSize(40.f);
        int[] margins = new int[] {230, 90, 70, 90};
        multiRender.setMargins(margins);

        switch (STATE) {
            case ACCELERATION:
                multiRender.setYAxisMin(yaMin-2.);
                multiRender.setYAxisMax(yaMax+2.);
                break;
            case ROTATION:
                multiRender.setYAxisMin(yrMin-2.);
                multiRender.setYAxisMax(yrMax+2.);
                break;
            case MAGNETIC:
                multiRender.setYAxisMin(ymMin-2.);
                multiRender.setYAxisMax(ymMax+2.);
                break;
        }

        multiRender.addSeriesRenderer(xRender);
        multiRender.addSeriesRenderer(yRender);
        multiRender.addSeriesRenderer(zRender);

        currentChart = ChartFactory.getLineChartView(getBaseContext(), data, multiRender);

        layout.addView(currentChart);
    }


    private void jumpHeight(float x, float y, float z) {
        double accelMag;
        double xd = (double) x;
        double yd = (double) y;
        double zd = (double) z;
        accelMag = Math.sqrt(xd*xd+yd*yd+zd*zd);
        if (accelMag < 9.0) {
            jumpCount++;
            if (jumpCount > jumpMax)
                jumpMax = jumpCount;
        } else {
            jumpCount = 0;
        }
    }

    private float getJumpHeight(int totalCount, float testTime) {
        double h;
        float dt = testTime / ((float) totalCount);
        float t = dt * (float) jumpMax;
        h = 6.*(32.174*((t/2.)*(t/2.)));
        return (float) h; // height in inches
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

}

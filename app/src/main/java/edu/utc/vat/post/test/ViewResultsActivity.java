/**
 * UTC Virt Athletic Assistant (aka Sports Injury Prevention Screening -- SIPS)
 * v0.01.1 (12/3/15)
 * rg 12/2/15.
 */


package edu.utc.vat.post.test;


import android.app.DialogFragment;

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

    private LinearLayout layout;
    private Button accelButton, gyroButton, compassButton;

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

        if (!loadResults())
            Log.e("ViewResults","Couldn't load files to view");

        DialogFragment uploadData = new edu.utc.vat.post.test.UploadDataDialogFragment();
        uploadData.show(getFragmentManager(), "uploadData");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accel:
                STATE = ACCELERATION;
                break;
            case R.id.gyro:
                STATE = ROTATION;
                break;
            case R.id.compass:
                STATE = MAGNETIC;
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
        int aCount = CallNative.CountAccel();
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
                t = Float.parseFloat(scanLine.next()) - 1000000.f;
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
        int gCount = CallNative.CountGyro();
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
                t = Float.parseFloat(scanLine.next()) - 1000000.f;
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
        int cCount = CallNative.CountCompass();
        scanCompass.useDelimiter("\n");
        ct = 0;
        while(scanCompass.hasNext()) {
            Scanner scanLine;
            scanLine = new Scanner(scanCompass.next());
            scanLine.useDelimiter(",");
            float x, y, z, t;
            // c.dat -- ignoring first line; header
            if (ct > 0) {
                x = Float.parseFloat(scanLine.next());
                y = Float.parseFloat(scanLine.next());
                z = Float.parseFloat(scanLine.next());
                t = Float.parseFloat(scanLine.next()) - 1000000.f;
            }
            ct++;
        }
        Log.i("ViewResults","Compass data lines count %d" + cCount);
        Log.i("ViewResults","Compass values count %d" + ct);


        Log.i("ViewResults","loadResults complete");
        return true;
    }

    private void openChart() {

    }

}

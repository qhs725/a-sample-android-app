/**
 * UTC Virt Athletic Assistant (aka Sports Injury Prevention Screening -- SIPS)
 * v0.01.1 (12/3/15)
 * rg 12/2/15.
 */

package edu.utc.vat.post.test;


import android.os.Bundle;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Button;

import edu.utc.vat.R;
import edu.utc.vat.TestingActivity;

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

        loadResults();
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


    private void loadResults() {

    }

    private void openChart() {

    }

}

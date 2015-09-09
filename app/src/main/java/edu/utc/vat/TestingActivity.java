/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/9/15
 */

package edu.utc.vat;

import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import android.os.Bundle;

import android.widget.TextView;

import java.util.HashMap;



public class TestingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int NO_EXERCISE_SELECTED = 0;
    private static final int ONE_LEG_SQUAT_HOLD = 1;
    private static final int ONE_LEG_JUMP_BALANCE = 2;
    private static int exercise = NO_EXERCISE_SELECTED;

    public static final int STOPPED = 0;
    public static final int COUNTDOWN = 2;
    public static final int TESTING = 1;
    public int status;
    private TextView testStatus;
    private String statusMessage;
    private HashMap<Integer, String> statusList = new HashMap<Integer, String>();

    private TextView currentExercise;
    private String exerciseName;
    private HashMap<Integer, String> exerciseList = new HashMap<Integer, String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        completeExerciseList();
        completeStatusList();
        exerciseName = exerciseList.get(exercise);
        statusMessage = statusList.get(-1);
        currentExercise = (TextView) findViewById(R.id.testing_exercise);
        testStatus = (TextView) findViewById(R.id.testing_status);
        currentExercise.setText(exerciseName);
        testStatus.setText(statusMessage);

        //TODO: set OnClickListeners

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_testing, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) { return true; }
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View view) {

        switch (view.getId()) {

        }
    }


    public void completeExerciseList () {
        exerciseList.put(1, "One Leg Squat Hold Test");
        exerciseList.put(2, "One Leg Jump Balance Test");
    }

    public void completeStatusList () {
        statusList.put(-1, "Enter NAME, etc...");
        statusList.put(3, "Press START to begin...");
        statusList.put(2, "Countdown to test...");
        statusList.put(1, "Testing...");
        statusList.put(0, "Finished...");
    }

    /**
     * Intent creation also passes exercise
     * */
    public static Intent createIntent(Context context, int e) {
        exercise = e;
        return new Intent(context, TestingActivity.class);
    }
}

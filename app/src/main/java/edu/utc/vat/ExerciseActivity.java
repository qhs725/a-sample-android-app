/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/9/15
 */

package edu.utc.vat;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;

import android.content.Context;
import android.content.Intent;

import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;

import android.os.Bundle;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import android.util.Log;

import android.os.StrictMode;

import java.util.HashMap;


public class ExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int NO_EXERCISE_SELECTED = 0;
    private static final int ONE_LEG_SQUAT_HOLD = 1;
    private static final int ONE_LEG_JUMP_BALANCE = 2;
    private static int exercise = NO_EXERCISE_SELECTED;

    public static final int STOPPED = 0;
    public static final int COUNTDOWN = 2;
    public static final int TESTING = 1;
    public static final int READY = 3;
    public static final int VOID = -1;
    public static final int UPLOADING = 4;
    public int status;
    private TextView testStatus;
    private String statusMessage;
    private HashMap<Integer, String> statusList = new HashMap<Integer, String>();

    private TextView currentExercise;
    private String exerciseName;
    private HashMap<Integer, String> exerciseList = new HashMap<Integer, String>();

    private TextView timerClock;
    private int timerTime;
    private String timerString;

    private EditText getUserInfo;
    private String userInfo;

    private Button resetButton;
    private Button startButton;
    private Button instructionsButton;

    private final long DEFAULT_COUNTDOWN_TIME = 5;
    private final long DEFAULT_TESTING_TIME = 20;

    private Toast concurrentToast;

    //TODO: create break for testing timer w/ jump test, i.e. if balanced prior to max/default time

    private Timer timer = new Timer(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        status = VOID;
        completeExerciseList();
        completeStatusList();
        exerciseName = exerciseList.get(exercise);
        statusMessage = statusList.get(status);
        currentExercise = (TextView) findViewById(R.id.testing_exercise);
        testStatus = (TextView) findViewById(R.id.testing_status);
        timerClock = (TextView) findViewById(R.id.timer);
        timerTime = 0;
        timerString = timerToString(timerTime);
        currentExercise.setText(exerciseName);
        testStatus.setText(statusMessage);
        timerClock.setText(timerString);
        getUserInfo = (EditText) findViewById(R.id.SessionInfo);
        resetButton = (Button) findViewById(R.id.TestingResetButton);
        startButton = (Button) findViewById(R.id.TestingStartButton);
        instructionsButton = (Button) findViewById(R.id.TestingInstructionsButton);
        resetButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        instructionsButton.setOnClickListener(this);

        timer.setCountDownTime(DEFAULT_COUNTDOWN_TIME);
        timer.setTestingTime(DEFAULT_TESTING_TIME);
        timer.initTimer();

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
        /*
        int id = view.getId();
        if (id == R.id.TestingStartButton) {
            if (getUserInfo.getText().toString().trim().length() > 0) {
                //timer.passUserInfo(userInfo); //TODO: pass to native
                status = READY;
            }
            if (status != READY) {
                Toast.makeText(this, "Please enter your NAME, etc...",
                        Toast.LENGTH_SHORT).show();
                resetButton.performClick();
            } else {
                userInfo = getUserInfo.getText().toString().trim();
                Toast.makeText(this, userInfo, Toast.LENGTH_SHORT).show();
                timer.countDown(); //TODO: RETURN BOOLEAN, TRUE --> UPLOAD PROMPT?
            }
            status = READY;
        }*/
        int id2 = view.getId();
        if (id2 == R.id.TestingResetButton) {
            status = STOPPED;
            //TODO: kill timer if running
            Log.i("RESET","Clicked reset button");
            getUserInfo.setText("");
            getUserInfo.setOnClickListener(new View.OnClickListener() {
                   public void onClick(View view) {
                       getUserInfo.requestFocus();
                       InputMethodManager inputManager = (InputMethodManager)
                               getSystemService(Context.INPUT_METHOD_SERVICE);
                       inputManager.showSoftInput(getUserInfo,
                               InputMethodManager.SHOW_IMPLICIT);
                   }
               }
            );
        }
        if (id2 == R.id.TestingInstructionsButton) {
            Log.i("INSTRUCTIONS","Toast called");
            Toast.makeText(this, "Instructions", Toast.LENGTH_LONG).show();
            //showToast("ENTER FIRST EXERCISE INSTRUCTION HERE...");
            //showToast("ENTER SECOND EXERCISE INSTRUCTION HERE...");
        }
        if (id2 == R.id.SessionInfo) {
            getUserInfo.setText("");
            getUserInfo.requestFocus();
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(getUserInfo, InputMethodManager.SHOW_IMPLICIT);
        }

        /*
        switch (id) {
            case R.id.TestingStartButton:
                if (getUserInfo.getText().toString().trim().length() > 0) {
                    //timer.passUserInfo(userInfo); //TODO: pass to native
                    status = READY;
                }
                if (status != READY) {
                    Toast.makeText(this, "Please enter your NAME, etc...",
                            Toast.LENGTH_SHORT).show();
                    resetButton.performClick();
                    break;
                } else {
                    userInfo = getUserInfo.getText().toString().trim();
                    Toast.makeText(this, userInfo, Toast.LENGTH_SHORT).show();
                    timer.countDown(); //TODO: RETURN BOOLEAN, TRUE --> UPLOAD PROMPT?
                }
                status = READY;
                break;

            case R.id.TestingResetButton:
                status = STOPPED;
                //TODO: kill timer if running
                Log.i("RESET","Clicked reset button");
                getUserInfo.setText("");
                getUserInfo.setOnClickListener(new View.OnClickListener() {
                       public void onClick(View view) {
                           getUserInfo.requestFocus();
                           InputMethodManager inputManager = (InputMethodManager)
                                   getSystemService(Context.INPUT_METHOD_SERVICE);
                           inputManager.showSoftInput(getUserInfo,
                                   InputMethodManager.SHOW_IMPLICIT);
                       }
                   }
                );
                break;

            case R.id.TestingInstructionsButton:
                Log.i("INSTRUCTIONS","Toast called");
                Toast.makeText(this, "Instructions", Toast.LENGTH_LONG).show();
                //showToast("ENTER FIRST EXERCISE INSTRUCTION HERE...");
                //showToast("ENTER SECOND EXERCISE INSTRUCTION HERE...");
                break;

            case R.id.SessionInfo:
                getUserInfo.setText("");
                getUserInfo.requestFocus();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(getUserInfo, InputMethodManager.SHOW_IMPLICIT);
                break;

        }*/
    }


    /**
     * onPause()
     */
    public void onPause() {
        super.onPause();

    }


    /**
     * onResume()
     */
    public void onResume() {
        super.onResume();

    }


    /**
     * onDestroy()
     */
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * Methods for updating UI with time and status from timer
     *
     */
    public void statusUpdate(int status) {
        Log.i("update", "statusUpdate");
        String statusUpdate  = statusList.get(status);
        testStatus.setText(statusUpdate);

        if(status == STOPPED) {
            Upload();
        }
    }
    public void timerUpdate(long time) {
        Log.i("update","timerUpdate");
        time = time/1000;
        timerTime = (int)time;
        if (time <= 60);
        else {
            //TODO: add support for times > 60s
        }
        timerString = timerToString(timerTime);
        timerClock.setText(timerString);
    }


    public int Upload() {
        status = UPLOADING;
        String statusUpdate = statusList.get(status);
        testStatus.setText(statusUpdate);
        Log.i("TESTING", "Upload method call");
        DialogFragment uploadData = new UploadDataDialogFragment();
        uploadData.show(getFragmentManager(), "uploadData");
        Log.i("TESTING", "Upload method return");
        return 0;
    }


    /**
     * Fill hash maps with strings for view corresponding to constant ints
     */
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
        statusList.put(4, "Uploading...");
    }


    /**
     * Intent creation also passes exercise
     * */
    public static Intent createIntent(Context context, int e) {
        exercise = e;
        return new Intent(context, TestingActivity.class);
    }


    private String timerToString(int time) {
        String string = new String();
        StringBuilder stringBuilder = new StringBuilder();
        if (time < 10) {
            stringBuilder.append("00:0");
            stringBuilder.append(time);
            string = stringBuilder.toString();
        } else {
            stringBuilder.append("00:");
            stringBuilder.append(time);
            string = stringBuilder.toString();
        }
        return string;
    }


    void showToast(String message) {
        if(concurrentToast != null) {
            concurrentToast.cancel();
        }
        concurrentToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        concurrentToast.show();
    }

}




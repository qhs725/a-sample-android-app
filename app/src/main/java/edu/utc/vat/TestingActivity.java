/**
 * Sports Injury Prevention Screening -- SIPS
 * v0.01.1b (12/?/15)
 * rg 9/9/15
 * <p/>
 * TODO: Get status updating appropriately
 * <p/>
 * TODO: Add fragments for displaying timer and Exercise instructions
 * TODO: Move FLAG_KEEP_SCREEN_ON to timer fragment, so, screen only remains on during testing
 */

package edu.utc.vat;


import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import edu.utc.vat.flanker.FlankerActivity;
import edu.utc.vat.flanker.FlankerResultsActivity;
import edu.utc.vat.post.test.ViewDialogFragment;
import edu.utc.vat.post.test.ViewResultsActivity;
import edu.utc.vat.util.adapters.listSelections;


public class TestingActivity extends BaseActivity implements View.OnClickListener {

    private static final int NO_EXERCISE_SELECTED = 0;
    private static final int ONE_LEG_SQUAT_HOLD = 1;
    public static final int SINGLE_LEG_JUMP = 2;
    public static final int FLANKER = 3;
    public static int exercise = NO_EXERCISE_SELECTED;

    public static final int STOPPED = 0;
    public static final int COUNTDOWN = 2;
    public static final int TESTING = 1;
    public static final int READY = 3;
    public static final int VOID = -1;
    public int status;
    private TextView testStatus;
    private String statusMessage;
    private HashMap<Integer, String> statusList = new HashMap<Integer, String>();

    private TextView currentExercise;
    public String exerciseName;
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
    private final long DEFAULT_TESTING_TIME = 30;
    public final long JUMP_TESTING_TIME = 5;
    private final long LEG_BALANCE_TESTING_TIME = 30;

    private static final String CLASS_NAME = "LoginActivity";
    private Timer timer;
    private JSONObject task = new JSONObject();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        try {
            task = listSelections.getSelectedTask();
            if (!task.getString("type").equals("flanker"))
                initNavDrawer();
        } catch (JSONException e) {
            e.printStackTrace();
        }


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

        try {
            currentExercise.setText(exerciseName != null ? exerciseName : task.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        testStatus.setText(statusMessage);
        timerClock.setText(timerString);
        getUserInfo = (EditText) findViewById(R.id.SessionInfo);
        resetButton = (Button) findViewById(R.id.TestingResetButton);
        startButton = (Button) findViewById(R.id.TestingStartButton);
        instructionsButton = (Button) findViewById(R.id.TestingInstructionsButton);
        resetButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        instructionsButton.setOnClickListener(this);
        timer = new Timer(this);

        timer.setCountDownTime(DEFAULT_COUNTDOWN_TIME);
        timer.setTestingTime(DEFAULT_TESTING_TIME);
        timer.initTimer();

        switch (exercise) {
            case ONE_LEG_SQUAT_HOLD: {
                timer.setTestingTime(LEG_BALANCE_TESTING_TIME);
                break;
            }
            case SINGLE_LEG_JUMP: {
                timer.setTestingTime(JUMP_TESTING_TIME);
                break;
            }
        }

        if (CallNative.FlankerCheck() == true) {
            Log.i("TESTING", "GO TO FLANKER RESULTS DIALOG .. NO .. ??");
            //Upload(); //TODO: Create alternate Flanker upload dialog fragment
        } else try {
            if (exercise == FLANKER || task.getString("type").equals("flanker")) {
                startFlanker();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.TestingStartButton: {

                if (status != COUNTDOWN && status != TESTING) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                    userInfo = getUserInfo.getText().toString().trim();
                    UserAccount.setSessionInfo(userInfo);//add user input to UserAccount
                    timer.countDown();
                    //Log.i("Testing", "Good--3");
                    status = COUNTDOWN;
                    break;
                } else {
                    status = READY;
                    showSnackbar(R.id.TestScroll, "TEST IN PROGRESS, PRESS RESET TO CANCEL");
                    //showToast("TEST IN PROGRESS, PRESS RESET TO CANCEL");
                    break;
                }
            }
            case R.id.TestingResetButton: {
                getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                timer.stopTimer();
                timerUpdate(0);
                status = VOID;
                statusUpdate(status);
                getUserInfo.setText("");
                getUserInfo.setOnClickListener(new View.OnClickListener() {
                                                   public void onClick(View view) {
                                                       InputMethodManager inputManager = (InputMethodManager)
                                                               getSystemService(Context.INPUT_METHOD_SERVICE);
                                                       inputManager.showSoftInput(getUserInfo,
                                                               InputMethodManager.SHOW_IMPLICIT);
                                                   }
                                               }
                );
                break;
            }
            case R.id.TestingInstructionsButton: {
                //TODO: Activity or fragment with instructions
                showToast("Exercise instructions are under development.");
                break;
            }
            case R.id.SessionInfo: {
                getUserInfo.setText("");
                getUserInfo.requestFocus();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(getUserInfo, InputMethodManager.SHOW_IMPLICIT);
                break;
            }
        }
    }


    /**
     * onPause()
     */
    public void onPause() {
        resetButton.performClick();
        super.onPause();
    }


    /**
     * onResume()
     */
    public void onResume() {
        if (status != VOID)
            resetButton.performClick();
        //if (CallNative.FlankerCheck())
        //    launchViewer();
        super.onResume();
    }


    /**
     * onDestroy()
     */
    public void onDestroy() {
        resetButton.performClick();
        if (CallNative.FlankerCheck())
            launchViewerF();
        super.onDestroy();
    }

    /**
     * Methods for updating UI with time and status from timer
     */
    public void statusUpdate(int status) {
        Log.i("update", "statusUpdate");
        String statusUpdate = statusList.get(status);
        testStatus.setText(statusUpdate);
        if (status == STOPPED) {
            Log.e("testing", "STATUS == STOPPED");
        }
    }

    public void timerUpdate(long time) {
        Log.i("update", "timerUpdate");
        time = time / 1000;
        timerTime = (int) time;
        if (time <= 60) ;
        else {
            showToast("Currently there is no support for tests over 60 seconds.");
        }
        timerString = timerToString(timerTime);
        timerClock.setText(timerString);
    }

    public int Upload() {
        status = READY;
        String statusUpdate = statusList.get(status);
        testStatus.setText(statusUpdate);
        DialogFragment uploadData = new edu.utc.vat.post.test.UploadDataDialogFragment();
        uploadData.show(getFragmentManager(), "uploadData");
        statusUpdate(status);
        return 0;
    }

    public int Viewer() {
        DialogFragment viewResults = new ViewDialogFragment();
        viewResults.show(getFragmentManager(), "viewResults");
        return 0;
    }


    /**
     * Fill hash maps with strings for view corresponding to constant ints
     */
    public void completeExerciseList() {
        exerciseList.put(1, "SINGLE LEG BALANCE TEST");
        exerciseList.put(2, "SINGLE LEG JUMP TEST");
        exerciseList.put(3, "FLANKER");
    }

    public void completeStatusList() {
        statusList.put(-1, "Add notes or press START...");
        statusList.put(3, "Press START to begin...");
        statusList.put(2, "Countdown to test...");
        statusList.put(1, "Testing...");
        statusList.put(0, "Finished...");
        statusList.put(4, "Uploading...");
    }


    /**
     * Intent creation also passes exercise
     */
    public static Intent createIntent(Context context, int e) {
        exercise = e;
        return new Intent(context, TestingActivity.class);
    }

    public void launchViewerF() {
        Log.i("TESTING", "launching viewer activity");
        startActivity(new Intent(this, FlankerResultsActivity.class));
        Log.i("TESTING", "launched viewer activity");
    }

    public void launchViewer() {
        Log.i("TESTING", "launching viewer activity");
        startActivity(new Intent(this, ViewResultsActivity.class));
        Log.i("TESTING", "launched viewer activity");
    }

    private void startFlanker() {
        startActivity(new Intent(this, FlankerActivity.class));
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

}





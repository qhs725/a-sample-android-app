/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/9/15
 *
 * TODO: Add fragments for displaying timer and Exercise instructions.
 */

package edu.utc.vat;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;
import com.ibm.mobile.services.push.IBMPush;
import java.util.HashMap;
import bolts.Continuation;
import bolts.Task;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.StrictMode;
import java.lang.Object;
import java.util.UUID;


public class TestingActivity extends BaseActivity implements View.OnClickListener {

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

    public BlueMixApplication blApplication = null;
    private static final String CLASS_NAME = "LoginActivity";
    private String uUserID = null;

    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
        timer = new Timer(this);

        timer.setCountDownTime(DEFAULT_COUNTDOWN_TIME);
        timer.setTestingTime(DEFAULT_TESTING_TIME);
        timer.initTimer();

        UUID uuid = UUID.randomUUID();
        UserAccount.setSessionID(uuid.toString());

        //use application class to maintain global state
        blApplication = (BlueMixApplication) getApplication();
        initServices(); //Initialize Bluemix connection
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.TestingStartButton: {
                if (getUserInfo.getText().toString().trim().length() > 0) {
                    status = READY;
                }
                if (status != READY) {
                    Toast.makeText(this, "Please enter your NAME, etc...",
                        Toast.LENGTH_SHORT).show();
                    resetButton.performClick();
                    break;
                } else {
                    if (status != COUNTDOWN && status != TESTING) {
                        userInfo = getUserInfo.getText().toString().trim();
                        CallNative.PassID(UserAccount.getSessionID() + "," + uUserID + "," + userInfo);
                        timer.countDown();
                        status = COUNTDOWN;
                        break;
                    } else {
                        showToast("TEST IN PROGRESS, PLEASE RESET TO START NEW TEST");
                        break;
                    }
                }
            }
            case R.id.TestingResetButton: {
                timer.stopTimer();
                timerUpdate(0);
                status = VOID;
                statusUpdate(status);
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
            }
            case R.id.TestingInstructionsButton: {
                showToast("ENTER EXERCISE INSTRUCTIONS HERE... \n INSTRUCTION 1..\n " +
                        "INSTRUCTION 2..\n INSTRUCTION 3..\n INSTRUCTION 4..\n" +
                        " ...\n INSTRUCTION N");
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
        super.onResume();
    }


    /**
     * onDestroy()
     */
    public void onDestroy() {
        resetButton.performClick();
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
            //check if network connection is available
            if (isNetworkAvailable()) {
                createSession(); //Create Session Object and upload
            } else {
                concurrentToast = Toast.makeText(this, "No internet connection found", Toast.LENGTH_LONG);
                concurrentToast.show();
                return;
            }
        }
    }
    public void timerUpdate(long time) {
        Log.i("update","timerUpdate");
        time = time/1000;
        timerTime = (int)time;
        if (time <= 60);
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
        DialogFragment uploadData = new UploadDataDialogFragment();
        uploadData.show(getFragmentManager(), "uploadData");
        statusUpdate(status);
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


    //TODO: is this deprecated?
    public void createSession() {
        //Call to upload session data files if any exist
      //  Session.getSensorData();
    }


    public  void initServices(){
        if (UserAccount.getIdToken() != null) {

            // set ID TOKEN so that all subsequent Service calls
            // will contain the ID TOKEN in the header
            Log.d(CLASS_NAME, "Setting the Google ID token: \n"
                    + UserAccount.getIdToken());

            Log.d(CLASS_NAME, "Setting the Google Access token for all future IBM Bluemix Mobile Cloud Service calls: \n"
                    + UserAccount.getAccessToken());

            // set the access token so that all subsequent calls to IBM Bluemix Mobile Cloud Services
            // will contain the access token in the header
            // Note: Kicking off a Bolts Asynchronous task to initialize services, chained with
            // an additional Bolts Task, in series with first one, in order to register the device
            // with the IBM Push service
            IBMBluemix.setSecurityToken(IBMBluemix.IBMSecurityProvider.GOOGLE, UserAccount.getAccessToken())
                    .continueWithTask(
                            new Continuation<IBMCurrentUser, Task<String>>() {
                                @Override
                                public Task<String> then(Task<IBMCurrentUser> user) throws Exception {
                                    // if setting the security token has failed...
                                    if (user.isFaulted()) {
                                        Log.e(CLASS_NAME, "There was an error setting the Google security token: " + user.getError().getMessage());
                                        user.getError().printStackTrace();
                                        // clear the security token
                                        return null;
                                    }

                                    // if setting the security token succeeds...
                                    Log.i(CLASS_NAME, "Set the Google security token successfully. Retrieved IBMCurrentUser: "
                                            + user.getResult().getUuid());

                                    // Save the IBMCurrentUser unique User Id
                                    uUserID = user.getResult().getUuid();

                                    //Add uUserID to User Account
                                    UserAccount.setuUserID(uUserID);

                                    // initialize IBM Bluemix Mobile Cloud Services
                                    blApplication.initializeBluemixServices();
                                    Log.i(CLASS_NAME, "Done initializing IBM Bluemix Services");

                                    Log.i(CLASS_NAME, "Done refreshing Session list.");

                                    // retrieve instance of the IBM Push service
                                    if(push == null) {
                                        push = IBMPush.getService();
                                    }

                                    Log.i(CLASS_NAME, "Registering device with the IBM Push service.");
                                    // register the device with the IBM Push service


                                    return push.register(deviceAlias, consumerID);
                                }

                            }).continueWith(new Continuation<String, Void>() {
                public Void then(Task<String> deviceIdTask) {
                    if (deviceIdTask.isFaulted()) {
                        Log.e(CLASS_NAME, "Device not registered with IBM Push service successfully.");
                        Log.e(CLASS_NAME, "Exception : " + deviceIdTask.getError().getMessage());
                        deviceIdTask.getError().printStackTrace();
                        return null;
                    }

                    Log.i(CLASS_NAME, "Device registered with IBM Push service successfully. Device Id: "
                            + deviceIdTask.getResult());

                    return null;
                }
            });
        } else {
            Log.e(CLASS_NAME, "Did not receive an expected authentication token. Finishing activity.");
            finish();
        }
    }
}




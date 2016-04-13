/**
 * Sports Injury Prevention Screening
 * v0.01.1b (12/3/15)
 * rg 09.08.15
 */

package edu.utc.vat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;

import bolts.Continuation;
import bolts.Task;
import edu.utc.vat.bluetooth.BtActivity;
import edu.utc.vat.post.test.ViewResultsActivity;


public class MainActivity extends BaseActivity {

    final MainActivity self = this;

    private static final int NO_EXERCISE_SELECTED = 0;
    private static final int ONE_LEG_SQUAT_HOLD = 1;
    private static final int ONE_LEG_JUMP_BALANCE = 2;
    private static final int FLANKER = 3;
    private static int exercise = NO_EXERCISE_SELECTED;
    private Intent intent;

    public static final String CLASS_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        initNavDrawer();

        findViewById(R.id.MainMenuButton1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise = ONE_LEG_SQUAT_HOLD;
                startActivity(TestingActivity.createIntent(self, exercise));
            }
        });

        findViewById(R.id.MainMenuButton2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise = ONE_LEG_JUMP_BALANCE;
                startActivity(TestingActivity.createIntent(self, exercise));
            }
        });

        //--LAUNCHING TESTING ACTIVITY DIRECTLY FOLLOWED BY FLANKER ACTIVITY--
        findViewById(R.id.MainMenuButton3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise = FLANKER;
                startActivity(TestingActivity.createIntent(self, exercise));
            }
        });

        /*findViewById(R.id.MainMenuButton4).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Bluetooth pairing tests are currently under development.");
                //startBluetooth();
            }
        });*/


        findViewById(R.id.MainMenuButton5).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(GroupListActivity.createIntent(self));
            }
        });

        if (CallNative.FlankerCheck() == false) {
            CallNative.InstantiateSensorsHandler();
            CallNative.FlankerInit(); //TODO: WHAT DOES THIS DO ... WHEN TO CALL 2nd TIME ...
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    /**
     * onResume()
     */
    public void onResume() {
        //if (CallNative.FlankerCheck())
        //    launchViewer();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        IBMBluemix.clearSecurityToken().continueWith(
                new Continuation<IBMCurrentUser, Void>() {
                    @Override
                    public Void then(Task<IBMCurrentUser> task) throws Exception {
                        if (task.isFaulted()) {
                            Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                            return null;
                        }
                        IBMCurrentUser user = task.getResult();
                        Log.i(CLASS_NAME, "Successfully logged out of user: " + user.getUuid());
                        return null;
                    }
                });
        Log.i(CLASS_NAME, "Finishing Main Activity. Returning to Login Screen.");
        finish();
        super.onBackPressed();
    }

    private void startBluetooth() {
        startActivity(new Intent(this, BtActivity.class));
    }

    public void launchViewer() {
        startActivity(new Intent(this, ViewResultsActivity.class));
    }

}

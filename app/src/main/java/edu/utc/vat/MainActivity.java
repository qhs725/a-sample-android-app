/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 09.08.15
 * TODO: once InternalData is deprecated apache .jar should be removed
 */

package edu.utc.vat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.content.Context;

import com.ibm.mobile.services.cloudcode.IBMCloudCode;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMQuery;
import com.ibm.mobile.services.push.IBMPush;

import java.util.List;

import bolts.Continuation;
import bolts.Task;
import edu.utc.vat.util.GoogleTokenManager;




public class MainActivity extends BaseActivity implements OnClickListener {

    private com.google.android.gms.common.SignInButton mGetGoogleTokenButton;

    final MainActivity self = this;

    private static final int NO_EXERCISE_SELECTED = 0;
    private static final int ONE_LEG_SQUAT_HOLD = 1;
    private static final int ONE_LEG_JUMP_BALANCE = 2;
    private static int exercise = NO_EXERCISE_SELECTED;
    private Intent intent;
    private boolean bluemixServicesInitialized = false;
    public IBMPush push;
    public IBMCloudCode myCloudCodeService;
    public String deviceAlias = "TargetDevice";
    public String consumerID = "MBaaSListApp";
    private String uUserID = null;
    private Context context;

    public static final String CLASS_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        // get the Google ID TOKEN from LoginActivity
        Bundle extras = intent.getExtras();
        if (extras != null) {
                if (extras.getString("GOOGLE_ID_TOKEN") != null) {
                    UserAccount.setIdToken(extras.getString("GOOGLE_ID_TOKEN"));
                    UserAccount.setAccessToken(extras.getString("GOOGLE_OAUTH_TOKEN"));
                    UserAccount.setName(extras.getString("GOOGLE_NAME"));
                    UserAccount.setEmail(extras.getString("GOOGLE_EMAIL"));
                    UserAccount.setPicture(extras.getString("GOOGLE_PICTURE"));

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

                                            // initialize IBM Bluemix Mobile Cloud Services
                                            initializeBluemixServices();
                                            Log.i(CLASS_NAME, "Done initializing IBM Bluemix Services");
                                            // refresh the list
                                            listItems();
                                            Log.i(CLASS_NAME, "Done refreshing Item list.");

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
                    //If there is no Google Token look if user is guest
                    if(extras.getBoolean("IS_GUEST")) {
                        Log.i(CLASS_NAME, "Logging in as GUEST");
                    }
                    else{
                    Log.e(CLASS_NAME, "Did not receive an expected authentication token. Finishing activity.");
                    finish();
                }
            }
    } else {
        Log.i(CLASS_NAME, "Did not receive an extras bundle as expected from the Intent. Finsihing activity.");
        finish();
    }

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


        /*
        findViewById(R.id.MainMenuButton3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               // exercise = 0;
                startActivity(ExerciseListActivity.createIntent(self));
            }
        });
        */

        CallNative.InstantiateSensorsHandler();
        CallNative.IO();

    }

    @Override
    public void onClick(View v) {
        final Context context = self;
        Intent intent = null;

        switch (v.getId()) {

            case R.id.get_google_token_button:
                intent = new Intent(context, GoogleTokenManager.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public void onBackPressed(){
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

}

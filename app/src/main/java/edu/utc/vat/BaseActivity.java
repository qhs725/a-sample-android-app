package edu.utc.vat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

public class BaseActivity extends AppCompatActivity {

    private Intent intent;
    public static final String CLASS_NAME = "LoginActivity";
    private boolean bluemixServicesInitialized = false;
    public IBMPush push;
    public IBMCloudCode myCloudCodeService;
    public String deviceAlias = "VAT_user_device";
    public String consumerID = "utc-vat-app";
    private String uUserID = null;
    private Context context;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_testing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_exercise:
                intent = new Intent(this, ExerciseListActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Log.e(CLASS_NAME, "Did not receive an expected authentication token. Finishing activity.");
            finish();
        }
    }


    public void initializeBluemixServices() {
        Log.d(CLASS_NAME, "Entering initializeBluemixServices() method.");

        if(!bluemixServicesInitialized) {
            Log.i(CLASS_NAME, "IBM Bluemix Mobile Cloud Service SDKs have not been previously initialized...initializing.");
            // initialize the IBM Data Service
            IBMData.initializeService();
            // register Item Specialization here
            Item.registerSpecialization(Item.class);

            // initialize and retrieve an instance of the IBM CloudCode service
            IBMCloudCode.initializeService();
        } else {
            Log.i(CLASS_NAME, "IBM Bluemix Mobile Cloud Service SDKs have been previously initialized...skipping.");
        }

        if(myCloudCodeService == null) {
            myCloudCodeService = IBMCloudCode.getService();
        }

        if(!bluemixServicesInitialized) {
            // initialize IBM Push service
            IBMPush.initializeService();
        }
        // retrieve instance of the IBM Push service
        if(push == null) {
            push = IBMPush.getService();
        }

        bluemixServicesInitialized = true;
        Log.d(CLASS_NAME, "Exiting initializeBluemixServices() method.");
    }

    /**
     * Refreshes itemList from data service.
     *
     * An IBMQuery is used to find all the list items
     */
    public void listItems() {
        try {
            IBMQuery<Item> query = IBMQuery.queryForClass(Item.class);
            /**
             * IBMQueryResult is used to receive array of objects from server.
             *
             * onResult is called when it successfully retrieves the objects associated with the
             * query, and will reorder these items based on creation time.
             *
             * onError is called when an error occurs during the query.
             */
            query.find().continueWith(new Continuation<List<Item>, Void>() {
                @Override
                public Void then(Task<List<Item>> task) throws Exception {
                    // Log error message, if the save task fail.
                    if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                        return null;
                    }

                    final String uEmail = UserAccount.getEmail();
                    final List<Item> objects = task.getResult();

                    // If the result succeeds
                    if (!isFinishing()) {
                        //Add code here
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }  catch (IBMDataException error) {
            Log.e(CLASS_NAME, "Exception : " + error.getMessage());
        }
    }

}



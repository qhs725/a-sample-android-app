package edu.utc.vat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;
import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;
import com.ibm.mobile.services.push.IBMPush;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class IBMDataTest extends BaseActivity {

    //TO BE DELETED SOON\\



    public BlueMixApplication blApplication = null;

    private String uUserID = null;
    private Context context = this;
    private static final String CLASS_NAME = "LoginActivity";
    private  EditText sessionToAdd;
    private Button submitbtn;
    private String sessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ibmdata_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // use application class to maintain global state
        blApplication = (BlueMixApplication) getApplication();
        initServices();



        submitbtn = (Button) findViewById(R.id.submit);
        sessionToAdd = (EditText) findViewById(R.id.sessionToAdd);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionName = sessionToAdd.getText().toString(); //Get name of session.
                createSession(view); //Call to create session object
            }
        });



        // hide the keyboard until needed
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Send a notification to all devices whenever the Sessions are modified (create, update, or delete)
     */

    private void updateOtherDevices() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("key1", "value1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Call the node.js application hosted in the IBM Cloud Code service
        // with a POST call, passing in a non-essential JSONObject
        // The URI is relative to, appended to, the BlueMix context root
        myCloudCodeService.post("notifyOtherDevices", jsonObj).continueWith(new Continuation<IBMHttpResponse, Void>() {

            @Override
            public Void then(Task<IBMHttpResponse> task) throws Exception {
                int responseCode;
                if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                    return null;
                }

                responseCode = task.getResult().getHttpResponseCode();
                InputStream is = task.getResult().getInputStream();
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    String responseString = "";
                    String myString = "";
                    while ((myString = in.readLine()) != null)
                        responseString += myString;

                    in.close();
                    Log.i(CLASS_NAME, "Response Body: " + responseString);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(CLASS_NAME, "Response Status from notifyOtherDevices: " + responseCode);
                if (responseCode == 401) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    IBMBluemix.clearSecurityToken();
                    finish();
                }
                return null;
            }

        });
    }

    /**
     * called on done and will add session to list.
     *
     * @param  v edittext View to get session from.
     * @throws IBMDataException
     */
    public void createSession(View  v) {

        String toAdd = sessionName; //Test session (name only from EditView)
        Log.i(CLASS_NAME, "Session : " + toAdd + " has been received from EditView");

        Session session = new Session();

        session.getSensorData(session);
        if (!toAdd.equals("")) {
            Log.i(CLASS_NAME, "Session : value from EditView is not null");
            session.setName(toAdd);

            session.setUserId(uUserID);
            /**
             * IBMObjectResult is used to handle the response from the server after
             * either creating or saving an object.
             *
             * onResult is called if the object was successfully saved
             * onError is called if an error occurred saving the object
             */
            session.save().continueWith(new Continuation<IBMDataObject, Void>() {
                @Override
                public Void then(Task<IBMDataObject> task) throws Exception {
                    // Log error message, if the save task fail.
                    if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                        return null;
                    }

                    Log.i(CLASS_NAME, "Successfully saved a new session!");
                    // If the result succeeds, load the list
                    if (!isFinishing()) {
                        updateOtherDevices();
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);

            //set text field back to empty after session added
            sessionToAdd.setText("");
        }
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

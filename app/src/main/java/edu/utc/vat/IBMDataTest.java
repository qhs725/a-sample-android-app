package edu.utc.vat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.mobile.services.data.IBMDataException;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMQuery;

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

    public List<Session> sessionList;
    public BlueListApplication blApplication = null;
    public ArrayAdapter<Session> lvArrayAdapter = null;
    int listSessionPosition = 0;
    public ListView SessionsLV = null;
    public ActionMode mActionMode = null;
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
        blApplication = (BlueListApplication) getApplication();
        blApplication.initServices();

        sessionList = blApplication.getSessionList();
        if(sessionList.size() == 0){
            Session s = new Session();
            s.setName("List Was Null");
            s.setUserId(UserAccount.getName());
            sessionList.add(s);
        }

        submitbtn = (Button) findViewById(R.id.submit);
        sessionToAdd = (EditText) findViewById(R.id.sessionToAdd);
        // String toAdd = sessionToAdd.getText().toString();
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionName = sessionToAdd.getText().toString();
                createSession(view);
            }
        });

        // set up the array adapter for items list view
       // ListView sessionsLV = (ListView)findViewById(R.id.sessionsList);
       // lvArrayAdapter = new ArrayAdapter<Session>(this, R.layout.list_session_1, sessionList);
        // SessionsLV.setAdapter(lvArrayAdapter);

/* Refresh the list. */
        listSessions();



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
     * Send a notification to all devices whenever the BlueList is modified (create, update, or delete)
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

    public void listSessions() {
        try {
            IBMQuery<Session> query = IBMQuery.queryForClass(Session.class);
            /**
             * IBMQueryResult is used to receive array of objects from server.
             *
             * onResult is called when it successfully retrieves the objects associated with the
             * query, and will reorder these sessions based on creation time.
             *
             * onError is called when an error occurs during the query.
             */
            query.find().continueWith(new Continuation<List<Session>, Void>() {
                @Override
                public Void then(Task<List<Session>> task) throws Exception {
                    // Log error message, if the save task fail.
                    if (task.isFaulted()) {
                        Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                        return null;
                    }

                    final String uEmail = UserAccount.getEmail();
                    final List<Session> objects = task.getResult();

                    // If the result succeeds, load the list
                    if (!isFinishing()) {
                        //clear local sessionList, as we'll be reordering & repopulating from DataService.
                        sessionList.clear();
                        Log.d(CLASS_NAME, "Clearing session list to re-load from IBMData for username: " + uEmail);
                        for(Session session:objects) {
                            if(session.getUserId() != null && session.getUserId().equals(uUserID)) {
                                sessionList.add(session);
                                Log.d(CLASS_NAME, "Added session to list.");
                            }
                        }
                        sortSessions(sessionList);

                        // tells the view to refresh itself, since the underlying data has changed.
                        lvArrayAdapter.notifyDataSetChanged();
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }  catch (IBMDataException error) {
            Log.e(CLASS_NAME, "Exception : " + error.getMessage());
        }
    }

    /**
     * sort a list of Sessions
     * @param List<Session> theList
     */
    private void sortSessions(List<Session> theList) {
        //sort collection by case insensitive alphabetical order
        Collections.sort(theList, new Comparator<Session>() {
            @Override
            public int compare(Session lhs,
                               Session rhs) {
                String lhsName = lhs.getName();
                String rhsName = rhs.getName();
                return lhsName.compareToIgnoreCase(rhsName);
            }
        });
    }
    /**
     * on return from other activity, check result code to determine behavior
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
		/*if an edit has been made, notify that the data set has changed.*/
            case BlueListApplication.EDIT_ACTIVITY_RC:
                updateOtherDevices();
                sortSessions(sessionList);
                lvArrayAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * called on done and will add item to list.
     *
     * @param  v edittext View to get item from.
     * @throws IBMDataException
     */
    public void createSession(View v) {

        String toAdd =sessionName; //Test session (name only from EditView)
        Log.i(CLASS_NAME, "Session : " + toAdd + " has been received from EditView");
        Session session = new Session();
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
                        listSessions();
                        updateOtherDevices();
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);

            //set text field back to empty after item added
           sessionToAdd.setText("");
        }
    }

    /**
     * will delete an session from the list
     *
     * @param  Session session to be deleted
     */
    public void deleteSession(Session session) {
        sessionList.remove(listSessionPosition);
        //This will attempt to delete the item on the server
        session.delete().continueWith(new Continuation<IBMDataObject, Void>() {
            //Called if the object was successfully deleted
            @Override
            public Void then(Task<IBMDataObject> task) throws Exception {
                // Log error message, if the delete task fail.
                if (task.isFaulted()) {
                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                    return null;
                }

                // If the result succeeds, reload the list
                if (!isFinishing()) {
                    updateOtherDevices();
                    lvArrayAdapter.notifyDataSetChanged();
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
        lvArrayAdapter.notifyDataSetChanged();
    }


}

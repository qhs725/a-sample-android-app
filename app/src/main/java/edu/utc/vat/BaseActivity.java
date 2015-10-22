package edu.utc.vat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ibm.mobile.services.cloudcode.IBMCloudCode;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;
import com.ibm.mobile.services.core.http.IBMHttpResponse;
import com.ibm.mobile.services.data.IBMData;
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

public class BaseActivity extends AppCompatActivity {

    private Intent intent;
    private static final String CLASS_NAME = "LoginActivity";
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
                intent = new Intent(this, IBMDataTest.class);
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
                        //If result succeeds then add Items to List. Code below is from sample
                        /*
                        //clear local itemList, as we'll be reordering & repopulating from DataService.
                        itemList.clear();
                        Log.d(CLASS_NAME, "Clearing item list to re-load from IBMData for username: " + uEmail);
                        for(Item item:objects) {
                            if(item.getUserId() != null && item.getUserId().equals(uUserID)) {
                                itemList.add(item);
                                Log.d(CLASS_NAME, "Added item to list.");
                            }
                        }
                        sortItems(itemList);

                        // tells the view to refresh itself, since the underlying data has changed.
                        lvArrayAdapter.notifyDataSetChanged();
                        */
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }  catch (IBMDataException error) {
            Log.e(CLASS_NAME, "Exception : " + error.getMessage());
        }
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
                if(responseCode == 401) {
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
                break;
        }
    }

    /**
     * called on done and will add item to list.
     *
     * @param  v edittext View to get item from.
     * @throws IBMDataException
     */
    public void createItem(View v) {

    }

    /**
     * will delete an item from the list
     *
     * @param  Item item to be deleted
     */
    public void deleteItem(Item item) {

    }

    /**
     * Will call new activity for editing item on list
     * @parm String name - name of the item.
     */
    public void updateItem(String name) {

    }

    /**
     * sort a list of Items
     * @param List<Item> theList
     */
    private void sortItems(List<Item> theList) {

    }

}



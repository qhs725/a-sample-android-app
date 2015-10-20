package edu.utc.vat;

import android.content.Intent;
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

/**
 * Sports Injury Prevention Screening -- SIPS
 * v0.01.1b (12.?.15)
 */

package edu.utc.vat;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;
import com.ibm.mobile.services.push.IBMPush;

import bolts.Continuation;
import bolts.Task;
import edu.utc.vat.forms.SportInjuryForm;
import edu.utc.vat.util.DBHelper;
import edu.utc.vat.util.dataUploadService;


public class BaseActivity extends AppCompatActivity {

    private Intent intent;
    private static final String CLASS_NAME = "LoginActivity";
    public IBMPush push;
    public String deviceAlias = "VAT_user_device";
    public String consumerID = "utc-vat-app";
    private static boolean isNetwork;
    private DBHelper db = new DBHelper(BlueMixApplication.getAppContext());

    private Toast newToast;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_testing, menu);
        return true;
    }


    /**
     * This is for handling the action bar menu items
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //respond to menu item selection
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_exercise:
                //intent = new Intent(this, SportInjuryForm.class);//Temporary position
                //startActivity(intent);

                Intent upload = new Intent(BlueMixApplication.getAppContext(), dataUploadService.class);
                BlueMixApplication.getAppContext().startService(upload);
                return true;

            case R.id.logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                db.deleteActiveUser();
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
        //setContentView(R.layout.activity_testing);
        setContentView(R.layout.activity_main);
        isNetworkAvailable();
    }


    /**
     * Checks network availability and sets isNetwork
     * TODO: CHECK -- Are both of these necessary??
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return isNetwork;
    }

    //Allows retrieval of isNetwork from other classes
    public static boolean getisNetwork() {
        return BaseActivity.isNetwork;
    }


    /**
     * showToast method for simplifying Toasts to one line of code
     */
    void showToast(String message) {
        if (newToast != null) {
            newToast.cancel();
        }
        newToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        newToast.show();
    }


    /**
     * Displays a Snackbar widget(alternative to toasts). Must include the layout to appear at and message to show.
     */
    void showSnackbar(int v, String message) {
        Snackbar.make(findViewById(v), message, Snackbar.LENGTH_LONG).show();
    }

    void changeTheme() {
        setTheme(android.R.style.Theme_DeviceDefault_NoActionBar_TranslucentDecor);
    }

}



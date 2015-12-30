/**
 * UTC Virt Athletic Trainer (aka Sports Injury Prevention Screening -- SIPS)
 * v0.01.1 (12.3.15)
 */


package edu.utc.vat;


import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.Menu;

import android.view.MenuItem;

import com.ibm.mobile.services.cloudcode.IBMCloudCode;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;
import com.ibm.mobile.services.push.IBMPush;

import java.io.File;

import bolts.Continuation;
import bolts.Task;
import edu.utc.vat.forms.SportInjuryForm;


public class BaseActivity extends AppCompatActivity {

    private Intent intent;
    private static final String CLASS_NAME = "LoginActivity";
    public IBMPush push;
    public IBMCloudCode myCloudCodeService;
    public String deviceAlias = "VAT_user_device";
    public String consumerID = "utc-vat-app";
    private String uUserID = null;
    private Context context;
    private static boolean isNetwork;

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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_exercise:
                intent = new Intent(this, SportInjuryForm.class);//Temporary position
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

        isNetworkAvailable();

    }
    //Check if session data files exist
    public Boolean isDataFiles() {
        Context context = BlueMixApplication.getAppContext();
        File fileList[] = null;
        Log.i(CLASS_NAME, "Checking for Session data files...");

        //Get files directory and get names of all files
        try {
            fileList = (new File(context.getFilesDir() + "/")).listFiles();
        }
        catch(Exception err){
            Log.e(CLASS_NAME, "ERROR: " + err.getMessage());
        }


        //Look at each file in the directory
        for (int i=0; i < fileList.length -1; i++)
        {
            Log.i("Files", "FileName:" + fileList[i].getName());
            String filenameArray[] = fileList[i].getName().split("\\.");
            String extension = filenameArray[filenameArray.length-1];

            //Check if file extension matches data file
            if(extension.equals("csv")){
                Log.i(CLASS_NAME, "Session Files found");
                return true; //some file exists that has data file extension, return true
            }
        }
        Log.i(CLASS_NAME, "No Session Files found stored in memory.");
        return false; //false if no files found that matches extension
    }

    //Check if network is available
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return isNetwork;
    }
    public static boolean getisNetwork() {
        return BaseActivity.isNetwork;
    }

}



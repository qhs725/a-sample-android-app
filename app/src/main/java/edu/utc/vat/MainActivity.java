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




public class MainActivity extends BaseActivity {

    private com.google.android.gms.common.SignInButton mGetGoogleTokenButton;

    final MainActivity self = this;

    private static final int NO_EXERCISE_SELECTED = 0;
    private static final int ONE_LEG_SQUAT_HOLD = 1;
    private static final int ONE_LEG_JUMP_BALANCE = 2;
    private static int exercise = NO_EXERCISE_SELECTED;
    private Intent intent;


    public static final String CLASS_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();




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

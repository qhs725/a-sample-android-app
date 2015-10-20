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

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;

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

    private Context context;

    public static final String CLASS_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
}

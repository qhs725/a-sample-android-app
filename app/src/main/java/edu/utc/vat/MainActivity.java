/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 09.08.15
 * TODO: once InternalData is deprecated apache .jar should be removed
 */

package edu.utc.vat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.content.Context;

import edu.utc.vat.util.GoogleTokenManager;




public class MainActivity extends AppCompatActivity implements OnClickListener {

    private com.google.android.gms.common.SignInButton mGetGoogleTokenButton;

    final MainActivity self = this;

    private static final int NO_EXERCISE_SELECTED = 0;
    private static final int ONE_LEG_SQUAT_HOLD = 1;
    private static final int ONE_LEG_JUMP_BALANCE = 2;
    private static int exercise = NO_EXERCISE_SELECTED;

    private Context context;


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

        mGetGoogleTokenButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.get_google_token_button);
        mGetGoogleTokenButton.setOnClickListener(this);

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}

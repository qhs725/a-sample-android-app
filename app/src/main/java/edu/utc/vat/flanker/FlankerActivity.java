/**
 * UTC Virtual Athletic Trainer
 * v0.01.1 (12/3/15)
 * 10/16/15
 * TODO: Call upload data from onEnd ..
 */

package edu.utc.vat.flanker;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.WindowManager;
import android.util.Log;

import edu.utc.vat.CallNative;
import edu.utc.vat.MainActivity;
import edu.utc.vat.TestingActivity;

public class FlankerActivity extends Activity {

    FlankerView fox;
    Context me;

    @Override protected void onCreate(Bundle penguin) {
        super.onCreate(penguin);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
        fox = new FlankerView(this);
        setContentView(fox);
        me = this;
    }

    @Override protected void onPause() {
        super.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        fox.onResume();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    public void onEnd() {
        Log.i("FLANKER","onEnd -- 1 exiting ...");
        CallNative.WriteOff();
        CallNative.StopSensors();
        CallNative.CloseFiles();
        //CallNative.FlankerOn();
        Log.i("FLANKER","onEnd -- 2 exiting ...");
        NavUtils.navigateUpFromSameTask(this);
        Log.i("FLANKER","onEnd -- 3 exiting ...");
        //onDestroy();
    }
}

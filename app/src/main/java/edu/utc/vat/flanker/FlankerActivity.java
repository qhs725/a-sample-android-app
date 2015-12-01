/**
 * UTC Virtual Athletic Trainer v0.01.1 (12/3/15)
 * 10/16/15
 * TODO: Call upload data from onEnd ..
 */

package edu.utc.vat.flanker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.WindowManager;

import edu.utc.vat.MainActivity;

public class FlankerActivity extends Activity {

    FlankerView fox;

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
        NavUtils.navigateUpFromSameTask(this);
    }
}

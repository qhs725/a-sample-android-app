/**
 * UTC Virtual Athletic Trainer v0.00
 * 10/16/15
 */

package edu.utc.vat.flanker;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

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
        fox = new FlankerView(getApplication());
        setContentView(fox);
    }

    @Override protected void onPause() {
        super.onPause();
        fox.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        fox.onResume();
    }
}

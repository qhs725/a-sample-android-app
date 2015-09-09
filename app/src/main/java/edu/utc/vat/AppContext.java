/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/8/15
 *
 */

package edu.utc.vat;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        AppContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AppContext.context;
    }

}

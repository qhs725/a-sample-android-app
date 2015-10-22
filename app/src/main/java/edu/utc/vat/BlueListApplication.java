/*
 * Copyright 2014 IBM Corp. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.utc.vat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.ibm.mobile.services.cloudcode.IBMCloudCode;
import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;
import com.ibm.mobile.services.core.internal.IBMLogger;
import com.ibm.mobile.services.data.IBMData;
import com.ibm.mobile.services.push.IBMPush;
import com.ibm.mobile.services.push.IBMPushNotificationListener;
import com.ibm.mobile.services.push.IBMSimplePushNotification;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import bolts.Continuation;
import bolts.Task;

public final class BlueListApplication extends Application {
	public static final int EDIT_ACTIVITY_RC = 1;
	public static IBMPush push = null;
	private Activity mActivity;
	private static final String CLASS_NAME = BlueListApplication.class.getSimpleName();
	private static final String APP_ID = "applicationID";
	private static final String APP_SECRET = "applicationSecret";
	private static final String APP_ROUTE = "applicationRoute";
	private static final String PROPS_FILE = "google_bluemix.properties";
	public Properties appProperties = null;

    public BaseActivity IBMFunctions= new BaseActivity();
    private boolean bluemixServicesInitialized = false;
    public IBMCloudCode myCloudCodeService;
    public String deviceAlias = "VAT_user_device";
    public String consumerID = "utc-vat-app";
    public String uUserID = null;


	private IBMPushNotificationListener notificationListener = null;
	private List<Item> itemList;
    private List<Session> sessionList;

	public BlueListApplication() {
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.d(CLASS_NAME, "Activity created: " + activity.getLocalClassName());
                mActivity = activity;

                // if this is the LoginActivity dont attempt to initialize IBM Bluemix
                // if("LoginActivity".equals(activity.getLocalClassName())){
                //	return;
                // }

                //Define IBMPushNotificationListener behavior on push notifications
                notificationListener = new IBMPushNotificationListener() {
                    @Override
                    public void onReceive(final IBMSimplePushNotification message) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Class<? extends Activity> actClass = mActivity.getClass();
                                if (actClass == MainActivity.class) {
                                    // Update the Grocery List


                                    ((BaseActivity) mActivity).listItems();
                                    Log.e(CLASS_NAME, "Notification message received: " + message.toString());
                                    //present the message when sent from Push notification console.
                                    if (!message.getAlert().contains("SessionList was updated")) {
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                new AlertDialog.Builder(mActivity)
                                                        .setTitle("Push notification received")
                                                        .setMessage(message.getAlert())
                                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                            }
                                                        })
                                                        .show();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                };
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity = activity;
                Log.d(CLASS_NAME, "Activity started: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mActivity = activity;
                Log.d(CLASS_NAME, "Activity resumed: " + activity.getLocalClassName());
                if (push != null) {
                    push.listen(notificationListener);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.d(CLASS_NAME, "Activity saved instance state: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (push != null) {
                    push.hold();
                }
                Log.d(CLASS_NAME, "Activity paused: " + activity.getLocalClassName());
                if (activity != null && activity.equals(mActivity))
                    mActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(CLASS_NAME, "Activity stopped: " + activity.getLocalClassName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.d(CLASS_NAME, "Activity destroyed: " + activity.getLocalClassName());
            }
        });
	}

	/**
	 * (non-Javadoc)
	 * Called when the application is starting, before any activity, service, 
	 * or receiver objects (excluding content providers) have been created.
	 * 
	 * @see Application#onCreate()
	 *
	 */
	@Override
	public void onCreate() {
		super.onCreate();


		itemList = new ArrayList<Item>();
        sessionList = new ArrayList<Session>();
		// Read from properties file
		appProperties = new Properties();
		Context context = getApplicationContext();
		try {
			AssetManager assetManager = context.getAssets();					
			appProperties.load(assetManager.open(PROPS_FILE));
			Log.i(CLASS_NAME, "Found configuration file: " + PROPS_FILE);
		} catch (FileNotFoundException e) {
			Log.e(CLASS_NAME, "The bluelist.properties file was not found.", e);
		} catch (IOException e) {
			Log.e(CLASS_NAME, "The bluelist.properties file could not be read properly.", e);
		}
		Log.i(CLASS_NAME, "Application ID is: " + appProperties.getProperty(APP_ID));

		IBMLogger.addLogCategory("DEBUG", "TRACE");
		// initialize the IBM core backend-as-a-service
		IBMBluemix.initialize(this, appProperties.getProperty(APP_ID), appProperties.getProperty(APP_SECRET), appProperties.getProperty(APP_ROUTE));

	}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

	/**
	 * returns the itemList, an array of Item objects.
	 * @return itemList
	 */
	public List<Item> getItemList() {
		return itemList;
	}

    /**
     * returns the sessionsList, an array of Item objects.
     * @return sessionList
     */
    public List<Session> getSessionList() {
        return sessionList;
    }

	public Properties getApplicationSettings() {
		return appProperties;
	}





    public  void initServices(){
        if (UserAccount.getIdToken() != null) {

            // set ID TOKEN so that all subsequent Service calls
            // will contain the ID TOKEN in the header
            Log.d(CLASS_NAME, "Setting the Google ID token: \n"
                    + UserAccount.getIdToken());

            Log.d(CLASS_NAME, "Setting the Google Access token for all future IBM Bluemix Mobile Cloud Service calls: \n"
                    + UserAccount.getAccessToken());

            // set the access token so that all subsequent calls to IBM Bluemix Mobile Cloud Services
            // will contain the access token in the header
            // Note: Kicking off a Bolts Asynchronous task to initialize services, chained with
            // an additional Bolts Task, in series with first one, in order to register the device
            // with the IBM Push service
            IBMBluemix.setSecurityToken(IBMBluemix.IBMSecurityProvider.GOOGLE, UserAccount.getAccessToken())
                    .continueWithTask(
                            new Continuation<IBMCurrentUser, Task<String>>() {
                                @Override
                                public Task<String> then(Task<IBMCurrentUser> user) throws Exception {
                                    // if setting the security token has failed...
                                    if (user.isFaulted()) {
                                        Log.e(CLASS_NAME, "There was an error setting the Google security token: " + user.getError().getMessage());
                                        user.getError().printStackTrace();
                                        // clear the security token
                                        return null;
                                    }

                                    // if setting the security token succeeds...
                                    Log.i(CLASS_NAME, "Set the Google security token successfully. Retrieved IBMCurrentUser: "
                                            + user.getResult().getUuid());

                                    // Save the IBMCurrentUser unique User Id
                                    uUserID = user.getResult().getUuid();

                                    // initialize IBM Bluemix Mobile Cloud Services
                                    initializeBluemixServices();
                                    Log.i(CLASS_NAME, "Done initializing IBM Bluemix Services");
                                    // refresh the list
                                    IBMFunctions.listItems();
                                    Log.i(CLASS_NAME, "Done refreshing Item list.");

                                    // retrieve instance of the IBM Push service
                                    if(push == null) {
                                        push = IBMPush.getService();
                                    }

                                    Log.i(CLASS_NAME, "Registering device with the IBM Push service.");
                                    // register the device with the IBM Push service
                                    return push.register(deviceAlias, consumerID);
                                }

                            }).continueWith(new Continuation<String, Void>() {
                public Void then(Task<String> deviceIdTask) {
                    if (deviceIdTask.isFaulted()) {
                        Log.e(CLASS_NAME, "Device not registered with IBM Push service successfully.");
                        Log.e(CLASS_NAME, "Exception : " + deviceIdTask.getError().getMessage());
                        deviceIdTask.getError().printStackTrace();
                        return null;
                    }

                    Log.i(CLASS_NAME, "Device registered with IBM Push service successfully. Device Id: "
                            + deviceIdTask.getResult());
                    return null;
                }
            });
        } else {
            Log.e(CLASS_NAME, "Did not receive an expected authentication token. Finishing activity.");
            IBMFunctions.finish();
        }
    }

    public void initializeBluemixServices() {
        Log.d(CLASS_NAME, "Entering initializeBluemixServices() method.");

        if(!bluemixServicesInitialized) {
            Log.i(CLASS_NAME, "IBM Bluemix Mobile Cloud Service SDKs have not been previously initialized...initializing.");
            // initialize the IBM Data Service
            IBMData.initializeService();
            // register Item Specialization here. TO BE REMOVED
            Item.registerSpecialization(Item.class);

            // register the Session Specialization
            Session.registerSpecialization(Session.class);

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
}
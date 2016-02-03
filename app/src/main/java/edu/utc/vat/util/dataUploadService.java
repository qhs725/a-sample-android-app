/**
 * Sports Injury Prevention Screening -- SIPS
 * v0.01.1b (12.3.15)
 * TODO: Clean all commented code
 * TODO: Clean unnecessary inline comments and appropriately comment methods
 */

package edu.utc.vat.util;

import android.app.IntentService;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import android.util.Log;

import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;

import edu.utc.vat.BlueMixApplication;
import edu.utc.vat.CallNative;
import edu.utc.vat.UserAccount;

public class dataUploadService extends IntentService {

    public static final String LOG_NAME = "Session";
    private static Context context = BlueMixApplication.getAppContext();
    private static final String EXT = "csv";
    private static JSONObject session_json;
    private static JSONObject obj;
    private static JSONObject user_json;
    // create a handler to post messages to the main thread
    private Handler mHandler;


    private static final String SERVER_IP = "http://utc-vat.mybluemix.net/upload";
    private static Socket mSocket = null;

    //default constructor
    public dataUploadService() {
        super("dataUploadService");
    }

    public dataUploadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        //  android.os.Debug.waitForDebugger(); //For debugging only
        mHandler = new Handler(getMainLooper());


        //Packaging
        //TODO: 1. Search for all dat files in internal storage
        //TODO: 2. Traverse files using first line for JSON keys and the rest as the values to them
        //TODO: 3. If f.dat exists then package it up as a nested json object
        //TODO: 4. Add Google User ID, Access Token, and any other needed data to the JSON object
        //Uploading/Saving
        //TODO: 5. Upload JSON if there is an internet connection, else save the file to internal storage in JSON format (can't be CSV)
        //TODO: 6. Delete .dat files
        //TODO: 7. Look for remaining files if there is an Internet connection, delete on successful upload

        //Check if network is available
        if (!isNetwork()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BlueMixApplication.getAppContext(), "No internet connection found", Toast.LENGTH_LONG).show();
                }
            });
            return; //return if no internet connection
        }

        user_json = new JSONObject();
        obj = new JSONObject();

        //Check if intent was passed an extra like form answers
        try {
            if (workIntent.hasExtra("jsonObject")) {
                JSONObject temp = new JSONObject(workIntent.getStringExtra("jsonObject"));

                JSONObject name = new JSONObject();


                //Processing JSON
                String given_name = UserAccount.getGivenName() != null ? UserAccount.getGivenName() : "null";
                String family_name = UserAccount.getFamilyName() != null ? UserAccount.getFamilyName() : "null";
                name.put("given_name", given_name);
                name.put("family_name", family_name);
                user_json.put("id", UserAccount.getGoogleUserID());
                user_json.put("idToken", UserAccount.getIdToken());
                user_json.put("accessToken", UserAccount.getAccessToken());
                user_json.put("name", name);
                obj.put("user", user_json);
                obj.put("body", temp);

                Log.d("JSON: ", obj.toString());
                if (temp.has("type")) {
                    String type = temp.getString("type");
                    if (type.equals("form")) {
                        upload_json_post(obj, "http://utc-vat.mybluemix.net/upload/form", mHandler);
                    }
                }

                return; //End service after uploading
            } else if (workIntent.hasExtra("flanker_json")) {
                user_json = new JSONObject(workIntent.getStringExtra("flanker_json"));
                Log.d("JSON_FLANKER: ", user_json.toString());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        while (CallNative.CheckData() == false) {
            Log.d(LOG_NAME, " File is still being written to");
        }

    }

    //Uploads json via Socket.io ti specified destination
    public void upload_json(JSONObject json, String destination, Handler mHandler) {
        Log.d(LOG_NAME, "JSON: " + json.toString());
        // Log.d(LOG_NAME, "Destination: " + destination);

        try {
            mSocket = IO.socket(destination);
            mSocket.connect();
        } catch (URISyntaxException e) {
        }

        //Send json object
        mSocket.emit("data", json);

        if (mHandler != null) {
            try {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BlueMixApplication.getAppContext(), "Submission complete", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (NullPointerException e) {
            }
        }
    }


    //Uploads json via post request to specified destination. Alternate upload for non-sensor data
    public void upload_json_post(JSONObject json, String destination, Handler mHandler) {
        HttpResponse httpresponse;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost(destination);
        StringEntity se = null;
        String line = "";

        try {
            //convert json to string and add to post request
            se = new StringEntity(json.toString());
            se.setContentType("application/json;charset=UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
            post.setEntity(se);

            //send post request
            httpresponse = httpclient.execute(post);

            //read response
            InputStreamReader isr = new InputStreamReader(httpresponse.getEntity().getContent());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BlueMixApplication.getAppContext(), "Submission complete", Toast.LENGTH_LONG).show();
                    }
                });
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }
    }

    //Retrieves list of names of files that exist in internal storage
    private String[] filesList(){
        String[] list = null;

        return list;
    }

    //Combines data files into JSON format
    private void packageData() {

    }

    //Saves data to single file in internal storage
    private void saveData(String data) {

    }


    //Returns true if there is a network connection
    private boolean isNetwork(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


    /*
    private void getSensorData() {
        ArrayList<String> dataFileNames = new ArrayList<String>();
        int numColumns = 0;
        String[] userInfo = null;
        InputStream file = null;

        //Get files directory and get names of all files within
        File fileFinder = new File(context.getFilesDir() + "/");
        File fileList[] = fileFinder.listFiles();
        Log.i("dataUpload", "Size: " + fileList.length);

        //Look at each file in the directory
        for (int i = 0; i < fileList.length; i++) {
            Log.i("dataUpload", "FileName:" + fileList[i].getName());
            String filenameArray[] = fileList[i].getName().split("\\.");
            String extension = filenameArray[filenameArray.length - 1];

            //Check if file extension is txt (CHANGE TO 'dat' when files are working)
            if (extension.equals(EXT)) {
                //dataFileNames[num] = fileList[i].getName();
                dataFileNames.add(fileList[i].getName());
                Log.i("dataUpload", "Found Data file:" + fileList[i].getName());
            } else if (fileList[i].getName().equals("f.dat")) {
                dataFileNames.add(fileList[i].getName());
                Log.i("dataUpload", "Found Flanker Data file:" + fileList[i].getName());
            }
        }

        //Data files should be in dataFileNames[] at this point
        session_json = new JSONObject(); //Single object upload for multiple files

        //Go through each data file, create object keys, add data values
        for (int i = 0; i < dataFileNames.size(); i++) {

            try {
                //Get current file in dataFileNames
                file = context.openFileInput(dataFileNames.get(i));

                String[] keyNames = new String[20];

                //check if file has data in it
                if (file != null) {
                    Log.i(LOG_NAME, "Data file is not null");
                    InputStreamReader stream = new InputStreamReader(file);
                    BufferedReader reader = new BufferedReader(stream);
                    String lineRow = "";


                    //Get first line to retrieve user/session based info to add to the Session Object.
                    //Skip for Flanker
                    if (dataFileNames.get(i).equals("f.dat")) {
                    } else {
                        if ((lineRow = reader.readLine()) != null) {
                            userInfo = lineRow.split(",");

                            //Add info from first line to Session Object
                            session_json.put("SESSIONID", (userInfo[0] != null) ? userInfo[0] : "null");
                            session_json.put("USERID", (userInfo[1] != null) ? userInfo[1] : "null");
                            session_json.put("USERINPUT", (userInfo[2] != null) ? userInfo[2] : "null");
                            session_json.put("type", "appsensor");
                        }
                    }
                    //Get second line to determine key names to sort data before adding it to the Session Object.
                    if ((lineRow = reader.readLine()) != null) {
                        keyNames = lineRow.split(",");
                        numColumns = keyNames.length;
                        for (int t = 0; t < keyNames.length; t++) {
                            Log.d(LOG_NAME, "Keynames: " + keyNames[t]);
                        }
                    }

                    //Create list of lists to dynamically load file's sensor data
                    List<List<String>> group = new ArrayList<List<String>>();
                    for (int u = 0; u < numColumns; u++) {
                        List<String> tempList = new ArrayList<String>();
                        group.add(tempList);
                    }

                    //Loop through file line by line
                    while ((lineRow = reader.readLine()) != null) {
                        String[] RowData = lineRow.split(",");

                        //Add value in each 'column' of the file to the respective ArrayList in the group List
                        for (int y = 0; y < numColumns; y++) {
                            try {
                                if (y >= RowData.length) {
                                } else {
                                    if (RowData[y] == "" || RowData[y] == null) {
                                        RowData[y] = "null";
                                    }
                                    group.get(y).add(RowData[y]);
                                    //Log.i(LOG_NAME, "Added Data: " + RowData[y] + " to position " + y);
                                }
                            } catch (Exception e) {
                                Log.e(LOG_NAME, "ERROR: " + e.getMessage());
                            }
                        }
                    }

                    //Added each column to the Session object
                    for (int t = 0; t < numColumns; t++) {
                        List data = group.get(t);
                        session_json.put(keyNames[t].toUpperCase(), (data != null) ? data : "");
                    }
                    group.clear(); //reset for next file
                }


                //Call to upload file (INDIVIDUAL)
                if (dataFileNames.get(i).equals("f.dat")) {
                    obj.put("type", "flanker");

                    obj.put("user", user_json);
                    obj.put("body", session_json);
                    upload_json(obj, SERVER_IP, null);
                } else {
                    upload_json(session_json, SERVER_IP, null);


                    //TODO: Delete file after receiving response from server?
                    this.deleteFile(dataFileNames.get(i));

                }
            } catch (FileNotFoundException e) {
                Log.e("dataUpload", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("dataUpload", "Can not read file: " + e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

        //mSocket.disconnect();

        if (file != null) {
            try {
                file.close(); //close file once done
            } catch (FileNotFoundException e) {
                Log.e("dataUpload", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("dataUpload", "Can not read file: " + e.toString());
            }
        }
        Log.i("dataUpload", "Data upload complete");
    }
}
*/
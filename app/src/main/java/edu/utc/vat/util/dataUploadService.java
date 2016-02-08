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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private static final String EXT = "dat";

    private JSONObject obj = new JSONObject();
    private JSONObject appsensor = new JSONObject();
    private JSONObject flanker = new JSONObject();
    private JSONObject body = new JSONObject();
    private JSONObject user_json = new JSONObject();
    private JSONObject name = new JSONObject();

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

        /**Packaging
        * TODO: ✓ 1. Search for all dat files in internal storage
        * TODO: ✓ 2. Traverse files using first line for JSON keys and the rest as the values to them
        * TODO: ✓ 3. If f.dat exists then package it up as a nested json object
        * TODO: ✓ 4. Add Google User ID, Access Token, and any other needed data to the JSON object
        * Uploading/Saving
        * TODO: 5. Upload JSON if there is an internet connection, else save the file to internal storage in JSON format (can't be CSV)
        * TODO: 5.1. give unique name to each file.
        * TODO: 6. Delete .dat files
        * TODO: 7. Look for remaining files if there is an Internet connection, delete on successful upload
        */

        //Check if C++ has finished writing to files
        while (CallNative.CheckData() == false) {
            Log.d(LOG_NAME, " File is still being written to");
        }

        try {
            getUserJSON();

            if (workIntent.hasExtra("formData")) { //Form data
                JSONObject temp = new JSONObject(workIntent.getStringExtra("formData"));

                //formatting JSON
                obj.put("body", temp);

                if (temp.has("type")) {
                    String type = temp.getString("type");
                    if (type.equals("form")) {
                        mHandler = new Handler(getMainLooper());
                        upload_json_post(obj, "http://utc-vat.mybluemix.net/upload/form", mHandler);
                    }
                }
            }
            else{
                packageData();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return; //End service
    }

   public void socketConnect(String destination) {
       try {
           mSocket = IO.socket(destination);
           mSocket.connect();
       } catch (URISyntaxException e) {
       }
   }

    //Uploads json via Socket.io ti specified destination
    public void upload_json(JSONObject json, Handler mHandler) {
        Log.d(LOG_NAME, "JSON being uploaded: " + json.toString());

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


    //Retrieves list of names of dat files that exist in internal storage
    private ArrayList<String> getFilesNames(String ext, int namesOnly) {
        ArrayList<String> dataFileNames = new ArrayList<String>();
        //Get files directory and get names of all files within
        File fileFinder = new File(context.getFilesDir() + "/");
        File list[] = fileFinder.listFiles();

        for (int i = 0; i < list.length; i++) {
            String filenameArray[] = list[i].getName().split("\\.");
            String extension = filenameArray[filenameArray.length - 1];
            if (extension.equals(ext) && namesOnly == 0) {
                dataFileNames.add(list[i].getName());
                Log.d("dataUpload", "Found Data file:" + list[i].getName());
            }
            else if(extension.equals(ext) && namesOnly == 1){
                dataFileNames.add(filenameArray[0]);
                Log.d("dataUpload", "Found Data file:" + list[i].getName());
            }
        }
        return dataFileNames;
    }


    //Combines data files into JSON format
    private void packageData() {

        //get list of dat files in internal storage
        ArrayList<String> fileNames = getFilesNames(EXT, 0);

        if(!fileNames.isEmpty()) {
            obj = new JSONObject();

            try {
                getUserJSON();

                for (int i = 0; i < fileNames.size(); i++) {
                    int numColumns = 0;
                    String[] userInfo = null;
                    InputStream file = null;
                    BufferedReader reader = null;
                    String lineRow = "";
                    String[] keyNames = new String[20];

                    //Get current file in dataFileNames
                    file = context.openFileInput(fileNames.get(i));
                    InputStreamReader stream = new InputStreamReader(file);
                    reader = new BufferedReader(stream);

                    //Get first line to determine key names to sort data before adding it to the JSON Object.
                    if ((lineRow = reader.readLine()) != null) {
                        keyNames = lineRow.split(",");
                        numColumns = keyNames.length;
                        for (int t = 0; t < keyNames.length; t++) {
                            Log.d(LOG_NAME, "Keynames: " + keyNames[t]);
                        }
                    }

                    //Create list of lists to dynamically load file data
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

                            if (y >= RowData.length) {
                            } else {
                                if (RowData[y] == "" || RowData[y] == null) {
                                    RowData[y] = "null";
                                }
                                group.get(y).add(RowData[y]);
                            }
                        }
                    }

                    //Added each column to the Session object
                    for (int t = 0; t < numColumns; t++) {
                        List data = group.get(t);
                        if (fileNames.get(i).equals("f.dat")) {
                            flanker.put(keyNames[t].toUpperCase(), (data != null) ? data : "");
                        } else
                            appsensor.put(keyNames[t].toUpperCase(), (data != null) ? data : "");
                    }
                    group.clear(); //reset for next file
                    context.deleteFile(fileNames.get(i));
                }

                //Form JSON object order
                if (flanker.has("STIMULUS")) {
                    body.put("flanker", flanker);
                }
                if (appsensor.has("ACCELX")) {
                    body.put("appsensor", appsensor);
                    if(UserAccount.getSessionInfo() != "") {
                        body.put("tasknotes", UserAccount.getSessionInfo());
                        UserAccount.setSessionInfo("");
                    }
                }
                obj.put("body", body);

                if (isNetwork()) {
                    socketConnect(SERVER_IP);
                    upload_json(obj, null);
                }
                else
                    saveData(obj + "");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(isNetwork()) {
            socketConnect(SERVER_IP);
            uploadFiles();//upload any remaining files
        }
    }




    //Saves string input to file. The file name is generated using numbers to keep track of existing files
    private void saveData(String data) {
        ArrayList<String> fileNames = getFilesNames("json", 1);
        int num =0;

        for(String name : fileNames) {
            int nameNumber = Integer.parseInt(name);

            if(num <=  nameNumber){
                num = nameNumber + 1;
            }
        }
        String filename = num +".json";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Converts each json file in internal storage to a JSON object and calls the function to send them to the server
    //Files are uploaded by highest number to lowest
    private void uploadFiles(){
        ArrayList<String> fileNames = getFilesNames("json", 0);
        for(int r = fileNames.size()-1; r >= 0; r--) {
            try {
                String fileStr = loadJSONFromFile(fileNames.get(r));

                if(fileStr != null) {
                    obj = new JSONObject(fileStr);
                    upload_json(obj, null);
                    Thread.sleep(500);//slight delay between files. Only 1 at most gets through without this
                }

                context.deleteFile(fileNames.get(r)); //delete uploaded file TODO: delete after confirmation from server?
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        mSocket.disconnect(); //disconnect when finished uploading

    }


    //Returns entire file as string
    private String loadJSONFromFile(String name) {
        String json = null;
        try {
            InputStream is = context.openFileInput(name);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    //Returns true if there is a network connection
    private boolean isNetwork() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        Boolean isNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        mHandler = new Handler(getMainLooper());
        if (!isNetwork) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BlueMixApplication.getAppContext(), "No internet connection found", Toast.LENGTH_LONG).show();
                }
            });
        }
        return isNetwork;
    }


    //Adds user json object whith important info to main obj json
    private void getUserJSON() throws JSONException {
        String given_name = UserAccount.getGivenName() != null ? UserAccount.getGivenName() : "null";
        String family_name = UserAccount.getFamilyName() != null ? UserAccount.getFamilyName() : "null";
        name.put("given_name", given_name);
        name.put("family_name", family_name);
        user_json.put("id", UserAccount.getGoogleUserID());
        user_json.put("idToken", UserAccount.getIdToken());
        user_json.put("accessToken", UserAccount.getAccessToken());
        user_json.put("name", name);
        obj.put("user", user_json);
    }
}
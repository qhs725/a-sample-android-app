package edu.utc.vat;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


public class Session {

    public static final String LOG_NAME = "Session";
    private static final String NAME = "name";
    private static final String USERID = "userId";
    private static final String SESSIONID = "sessionId";
    private static final String USERINPUT = "userInput";
    private static Context context = BlueMixApplication.getAppContext();
    private static final String EXT = "txt";
    private static JSONObject session_json;

    // private static final String SERVER_IP ="http://192.168.0.105:3000/upload";
    private static final String SERVER_IP = "http://utc-vat.mybluemix.net/upload";
    private static Socket mSocket = null;


    public static void sessionUpload(JSONObject sessJSON) {//Send Session to NodeServer

        //Attempt to connect to server
        try {
            mSocket = IO.socket(SERVER_IP);
            mSocket.connect();
        } catch (URISyntaxException e) {
        }


        //Send Session json object
        mSocket.emit("data", sessJSON);
    }


    public static void getSensorData() {
        //TODO: Create Asynck task/service for uploading files and to to periodically check for internet and to upload as soon as possible. Possibly add in WIFI only option in settings.

        //TODO: Change code to see each file as a unique exercise to upload. Line 1 will contain data provided from user. Line 2 will contain mapping for the following lines. The following lines will contain sensor data in the order shown by line 2.


        if (!BaseActivity.getisNetwork() || CallNative.CheckData() == false) {
            //Display Toast to warn user there is no detected internet connection
            Toast.makeText(BlueMixApplication.getAppContext(), "No internet connection found", Toast.LENGTH_LONG).show();

            return; //return if no internet connection
        }
        //Check if C++ is still writing to file
        if(!CallNative.CheckData()){
            Toast.makeText(BlueMixApplication.getAppContext(), "Unable to upload, still writing to file", Toast.LENGTH_LONG).show();
            return; //quit is a file is being written to
        }

        ArrayList<String> dataFileNames = new ArrayList<String>();
        int numColumns = 0;
        String[] userInfo = null;
        InputStream file = null;
        File fileList[] = null;
        //Get files directory and get names of all files within
        File fileFinder = new File(context.getFilesDir() + "/");
        try {
            if(fileFinder.listFiles() == null){
            return;
            }
            fileList = fileFinder.listFiles();

        } catch (Exception err) {
            Log.e("UPLOAD", "ERROR: " + err.getMessage());
        }
        Log.i("Files", "Size: " + fileList.length);

        //Look at each file in the directory
        for (int i = 0; i < fileList.length - 1; i++) {
            Log.i("Files", "FileName:" + fileList[i].getName());
            String filenameArray[] = fileList[i].getName().split("\\.");
            String extension = filenameArray[filenameArray.length - 1];

            //Check if file extension is txt (CHANGE TO 'dat' when files are working)
            if (extension.equals(EXT)) {
                //dataFileNames[num] = fileList[i].getName();
                dataFileNames.add(fileList[i].getName());
                Log.i("Files", "Found Data file:" + fileList[i].getName());
            }
        }

        //Data files should be in dataFileNames[] at this point

        session_json = new JSONObject(); //Single object upload for multiple files

        //Go through each data file, create object keys, add data values
        for (int i = 0; i < dataFileNames.size(); i++) {

            //  session_json = new JSONObject(); //INDIVIDUAL FILE

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
                    if ((lineRow = reader.readLine()) != null) {
                        userInfo = lineRow.split(","); // format of first line should be '{userId},{sessionId},{userInput}'

                        //Add info from first line to Session Object
                        session_json.put("USERID", (userInfo[0] != null) ? userInfo[0] : "null");
                        session_json.put("SESSIONID", (userInfo[1] != null) ? userInfo[1] : "null");
                        session_json.put("USERINPUT", (userInfo[2] != null) ? userInfo[2] : "null");
                    }
                    //Get second line to determine key names to sort data before adding it to the Session Object.
                    if ((lineRow = reader.readLine()) != null) {
                        keyNames = lineRow.split(",");
                        numColumns = keyNames.length;
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
                                if (RowData[y] == "" || RowData[y] == null) {
                                    RowData[y] = "null";
                                }
                                group.get(y).add(RowData[y]);
                                Log.i(LOG_NAME, "Added Data: " + RowData[y] + " to position " + y);
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
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Call to upload file (INDIVIDUAL)
            sessionUpload(session_json);
        }

        //Call to Upload all file fields together
        //sessionUpload(session_json);

        //mSocket.disconnect();
        if (file != null) {
            try {
                file.close(); //close file once done
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
        }
    }
}



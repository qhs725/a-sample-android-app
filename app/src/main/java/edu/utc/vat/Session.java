package edu.utc.vat;

import android.content.Context;
import android.util.Log;

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


/**
 * Created by Jaysp656 on 11/11/2015.
 */
public class Session {

    public static final String LOG_NAME = "Session";
    private static final String NAME = "name";
    private static final String USERID = "userId";
    private static final String SESSIONID = "sessionId";
    private static final String USERINPUT = "userInput";
    private static Context context = BlueMixApplication.getAppContext();
    private static final String EXT = "txt";
    private static JSONObject session_json;

    //private static final String SERVER_IP ="http://192.168.0.105:3000/utc-vat/v1/apps/0a27a50e-8c7f-487d-9135-5b360732abbf/upload";
    private static final String SERVER_IP ="http://utc-vat.mybluemix.net/utc-vat/v1/apps/0a27a50e-8c7f-487d-9135-5b360732abbf/upload";
    private static  Socket mSocket = null;


    public static void sessionUpload(JSONObject sessJSON){//Send Session to NodeServer



        try {
            mSocket = IO.socket(SERVER_IP);
            mSocket.connect();


            mSocket.emit("data", sessJSON);
        } catch (URISyntaxException e) {}


    }


    public static void getSensorData() {

        ArrayList<String> dataFileNames = new ArrayList<String>();
        int numColumns = 0;
        InputStream file = null;

        //Get files directory and get names of all files within
        File fileFinder = new File( context.getFilesDir() + "/" );
        File fileList[] = fileFinder.listFiles();
        Log.i("Files", "Size: "+ fileList.length);

        //Look at each file in the directory
        for (int i=0; i < fileList.length -1; i++)
        {
            Log.i("Files", "FileName:" + fileList[i].getName());
            String filenameArray[] = fileList[i].getName().split("\\.");
            String extension = filenameArray[filenameArray.length-1];

            //Check if file extension is txt (CHANGE TO 'dat' when files are working)
            if(extension.equals(EXT)){
                //dataFileNames[num] = fileList[i].getName();
                dataFileNames.add(fileList[i].getName());
                Log.i("Files", "Found Data file:" + fileList[i].getName());
            }
        }

        //Data files should be in dataFileNames[] at this point

        session_json = new JSONObject(); //Single object upload for multiple files

        //Go through each data file, create object keys, add data values
        for(int i=0; i < dataFileNames.size();i++) {

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

                    //Get first line to determine key names to add to current Session object
                    if((lineRow = reader.readLine()) != null){
                        keyNames = lineRow.split(",");
                        numColumns = keyNames.length;
                    }

                    //Create list of lists to dynamically load file data
                    List<List<String>> group = new ArrayList<List<String>>();
                    for(int u = 0; u < numColumns ; u++){
                        List<String> tempList = new ArrayList<String>();
                        group.add(tempList);
                    }

                    //Loop through file line by line
                    while ((lineRow = reader.readLine()) != null) {
                        String[] RowData = lineRow.split(",");

                        //Add value in each 'column' of the file to the respective ArrayList in the group List
                        for(int y = 0; y < numColumns ; y++){
                            try {
                                if(RowData[y] == "" || RowData[y] == null){
                                    RowData[y] = "null";
                                }
                                group.get(y).add(RowData[y]);
                                Log.i(LOG_NAME, "Added Data: " + RowData[y] + " to position " + y);
                            }
                            catch(Exception e){
                                Log.e(LOG_NAME, "ERROR: " + e.getMessage());
                            }
                        }
                    }

                    //Added each column to the Session object
                    for(int t = 0; t < numColumns ; t++){
                        List data =  group.get(t);
                        session_json.put(keyNames[t].toUpperCase(), (data != null) ? data : "");
                    }
                    group.clear();
                }
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Call to upload file (INDIVIDUAL)
           // sessionUpload(session_json);
        }

        //Call to Upload all field together
        sessionUpload(session_json);

        if(file != null) {
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

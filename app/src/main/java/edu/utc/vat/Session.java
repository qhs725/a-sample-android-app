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

import android.content.Context;
import android.util.Log;

import com.ibm.mobile.services.data.IBMDataFile;
import com.ibm.mobile.services.data.IBMDataObject;
import com.ibm.mobile.services.data.IBMDataObjectSpecialization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@IBMDataObjectSpecialization("Session")
public class Session extends IBMDataObject {
    public static final String CLASS_NAME = "Session";
    private static final String NAME = "name";
    private static final String USERID = "userId";
    private static final String ACCELX = "accelx";
    private static final String ACCELY = "accely";
    private static final String ACCELZ = "accelz";


    //ArrayLists to gather data into JSON request for new Session object
    private ArrayList<String> accelx = new ArrayList<String>();
    private ArrayList<String> accely = new ArrayList<String>();
    private ArrayList<String> accelz = new ArrayList<String>();

    /**
     * gets the name of the session.
     * @return String sessionName
     */
    public String getName() {
        return (String) getObject(NAME);
    }

    /**
     * sets the name of a list session, as well as calls setCreationTime()
     * @param String sessionName
     */
    public void setName(String sessionName) {
        setObject(NAME, (sessionName != null) ? sessionName : "");
    }

    /**
     * gets the userId associated with the session.
     * @return String userId
     */
    public String getUserId() {
        return (String) getObject(USERID);
    }


    /**
     * gets the the sensor data associated with the session.
     *
     */

    //Accelerometer Sensor Data
    public ArrayList<String[]> getAccelx() {
        return (ArrayList<String[]>) getObject(ACCELX);
    }

    public ArrayList<String[]> getAccely() {
        return (ArrayList<String[]>) getObject(ACCELY);
    }

    public ArrayList<String[]> getAccelz() {
        return (ArrayList<String[]>) getObject(ACCELZ);
    }

    /**
     * sets the email address of the user who is maintain list sessions
     * @param String userId
     */
    public void setUserId(String userId) {
        setObject(USERID, (userId != null) ? userId : "");
    }

    /**
     * sets the sensor data for the respective dimension and sensor
     *
     */

    //Save Accelerometer data to Session object
    public void setAccelx(ArrayList<String> data) {
        setObject(ACCELX, (data != null) ? data : "");
    }

    public void setAccely(ArrayList<String> data) {
        setObject(ACCELY, (data != null) ? data : "");
    }

    public void setAccelz(ArrayList<String> data) {
        setObject(ACCELZ, (data != null) ? data : "");
    }

    /**
     * when calling toString() for an session, we'd really only want the name.
     * @return String thesessionName
     */
    public String toString() {
        String thesessionName = "";
        thesessionName = getName();
        return thesessionName;
    }

    public void getSensorData(Context context, Session session) {

        ArrayList<String> dataFileNames = new ArrayList<String>();
        int numColumns = 0;
        int num = 0;
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
            if(extension.equals("txt")){
                //dataFileNames[num] = fileList[i].getName();
                dataFileNames.add(fileList[i].getName());
                Log.i("Files", "Found Data file:" + fileList[i].getName());
                num++;
            }
        }

        //Data files should be in dataFileNames[] at this point

        //Go through each data file, create object keys, add data values
        for(int i=0; i < dataFileNames.size();i++) {

            try {
                //Get current file in dataFileNames
                file = context.openFileInput(dataFileNames.get(i));

                String[] keyNames = new String[20];

                //check if file has data in it
                if (file != null) {
                    Log.i(CLASS_NAME, "Data file is not null");
                    InputStreamReader stream = new InputStreamReader(file);
                    BufferedReader reader = new BufferedReader(stream);
                    String lineRow = "";

                    //Get first line to determine key names to add to current Session object
                    if((lineRow = reader.readLine()) != null){
                        keyNames = lineRow.split(",");
                        numColumns = keyNames.length;
                    }

                    List<List<String>> group = new ArrayList<List<String>>();
                    for(int u = 0; u < numColumns ; u++){
                        List<String> tempList = new ArrayList<String>();
                        group.add(tempList);
                    }

                    int listNum = 0;

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
                                Log.i(CLASS_NAME, "Added Data: " + RowData[y] + " to position " + y);
                            }
                            catch(Exception e){
                                Log.e(CLASS_NAME, "ERROR: " + e.getMessage());
                            }
                        }
                    }

                    //Added each column to the Session object
                    for(int t = 0; t < numColumns ; t++){
                        List data =  group.get(t);
                        session.setObject(keyNames[t].toUpperCase(), (data != null) ? data : "");
                    }


                     //   file.close();
                    //session.setAccelx(accelx);
                    //session.setAccely(accely);
                    //session.setAccelz(accelz);

                    group.clear();
                }
            } catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
        }

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
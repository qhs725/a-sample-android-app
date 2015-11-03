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

        //File testFile = new File( context.getFilesDir() + "/" + "test.csv" );

        try {
            //Get file in Files directory
            InputStream file = context.openFileInput("test.csv");

            //check if file has data in it
            if ( file != null ) {
                Log.i(CLASS_NAME, "Data file is not null");
                InputStreamReader stream = new InputStreamReader(file);
                BufferedReader reader = new BufferedReader(stream);
                String line = "";

                //Loop through file line by line
                while ( (line = reader.readLine()) != null ) {
                    String[] RowData = line.split(",");
                    //Add data to ArrayLists to be added to current Session object
                    accelx.add(RowData[0]);
                    accely.add(RowData[1]);
                    accelz.add(RowData[2]);
                    Log.i(CLASS_NAME, "Added Data: " + RowData[0] + ", " + RowData[1] + ", " + RowData[2]);
                }


                file.close();
                session.setAccelx(accelx);
                session.setAccely(accely);
                session.setAccelz(accelz);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }
}

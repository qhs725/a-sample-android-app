/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/10/15
 */

package edu.utc.vat;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.util.Log;


public class InternalData {

    private String userInfo; //TODO: check if this is null, may have a bug

    private Context appContext;

    private FileInputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;


    public InternalData (Context context) {appContext = context;}

    public void passUserInfo(String info) {
        userInfo = info;
    }

    public void readInternal() {
        try {
            inputStream = appContext.openFileInput("data.csv");
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            String write;
            while ((line = bufferedReader.readLine()) != null) {
                Log.i("InternalData",line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/**
 * PSUEDOCODE JUMP TEST KILL TRIGGER
 * TODO:--START WRITING
 * TODO:--ON ACCELERATION EVENT
 * TODO:----START TRACKING 2s-3s RMS-AVG
 * TODO:----WHEN CURRENT RMS-AVG FALLS BELOW THRESHOLD
 * TODO:------KILL TIMER/WRITING
 */
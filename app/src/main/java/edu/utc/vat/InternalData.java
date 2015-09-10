/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/10/15
 * TODO: add low pass filter on accelerometer, no?
 */

package edu.utc.vat;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class InternalData {

    private static final int ACCELEROMETER = 0;
    private static final int GYROSCOPE = 1;
    private static final int COMPASS = 2;

    private int accelerometerCount = 0;
    private int gyroscopeCount = 0;
    private int compassCount = 0;

    private String[] accelerometerData;
    private String[] gyroscopeData;
    private String[] compassData;

    private String userInfo;

    private int accelerometerCounter = 0;
    private int gyroscopeCounter = 0;
    private int compassCounter = 0;

    private Context appContext;

    private FileInputStream inputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;

    private FileInputStream inputStream1;
    private InputStreamReader inputStreamReader1;
    private BufferedReader bufferedReader1;

    private FileInputStream inputStream2;
    private InputStreamReader inputStreamReader2;
    private BufferedReader bufferedReader2;

    //TODO: get count vals from native
    //TODO: instantiate arrays or vectors to hold each string
    //TODO: store internal data in arrays or vectors
    //TODO: pass internal data to database tables
    //TODO: clean up memory

    public InternalData (Context context) {appContext = context;}

    public void passUserInfo(String info) {
        userInfo = info;
    }

    public void readInternal() {

        accelerometerCount = CallNative.DataCount(ACCELEROMETER);
        gyroscopeCount = CallNative.DataCount(GYROSCOPE);
        compassCount = CallNative.DataCount(COMPASS);

        accelerometerData = new String[accelerometerCount];
        gyroscopeData = new String[gyroscopeCount];
        compassData = new String[compassCount];

        try {
            inputStream = appContext.openFileInput("a.dat");
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            String write;
            while ((line = bufferedReader.readLine()) != null) {
                //Log.i("InternalData",line);
                //TODO: catch acceleromter data in array or vector
                //TODO: add user info field to each line
                write = line + "," + userInfo;
                accelerometerData[accelerometerCounter] = write;
                accelerometerCounter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        accelerometerCounter = 0;

        try {
            inputStream1 = appContext.openFileInput("g.dat");
            inputStreamReader1 = new InputStreamReader(inputStream1);
            bufferedReader1 = new BufferedReader(inputStreamReader1);
            String write1;
            String line1;
            while ((line1 = bufferedReader1.readLine()) != null) {
                //Log.i("InternalData",line1);
                //TODO: catch gyroscope data in array or vector
                //TODO: add user info field to each line
                write1 = line1 + "," + userInfo;
                gyroscopeData[gyroscopeCounter] = write1;
                gyroscopeCounter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        gyroscopeCounter = 0;

        try {
            inputStream2 = appContext.openFileInput("c.dat");
            inputStreamReader2 = new InputStreamReader(inputStream2);
            bufferedReader2 = new BufferedReader(inputStreamReader2);
            String write2;
            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                //Log.i("InternalData",line2);
                //TODO: catch compass data in array or vector
                //TODO: add user info field to each line
                write2 = line2 + "," + userInfo;
                compassData[compassCounter] = write2;
                compassCounter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        compassCounter = 0;
    }


    public void postAccelerometer() {
        for(int i = 0; i < accelerometerCount; i++) {
            try {
                String[] data = accelerometerData[i].split(",");
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://192.168.0.100:8080/adat");
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("sinfo", data[4]));
                nameValuePair.add(new BasicNameValuePair("ax", data[0]));
                nameValuePair.add(new BasicNameValuePair("ay", data[1]));
                nameValuePair.add(new BasicNameValuePair("az", data[2]));
                nameValuePair.add(new BasicNameValuePair("ts", data[3]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                try {
                    HttpResponse response = httpclient.execute(httpPost);
                    //Log.d("Http Post Response:", response.toString());
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("postAccelerometer", e.getMessage());
                } catch (Exception e) {
                    Log.e("postAccelerometer", "Error in acclerometer http" + e);
                }

            } catch (Exception exception) {
                Log.i("postAccelerometer", exception.getMessage());
            }
        }
    }

    public void postGyroscope() {
        for(int i = 0; i < gyroscopeCount; i++) {
            try {
                String[] data = gyroscopeData[i].split(",");
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://192.168.0.100:8080/gdat");
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("sinfo", data[4]));
                nameValuePair.add(new BasicNameValuePair("gx", data[0]));
                nameValuePair.add(new BasicNameValuePair("gy", data[1]));
                nameValuePair.add(new BasicNameValuePair("gz", data[2]));
                nameValuePair.add(new BasicNameValuePair("ts", data[3]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                try {
                    HttpResponse response = httpclient.execute(httpPost);
                    //Log.d("Http Post Response:", response.toString());
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("postGyroscope", e.getMessage());
                } catch (Exception e) {
                    Log.e("postGyroscope", "Error in gyroscope http" + e);
                }

            } catch (Exception exception) {
                Log.i("postGyroscope", exception.getMessage());
            }
        }

    }

    public void postCompass() {
        for(int i = 0; i < compassCount; i++) {
            try {
                String[] data = compassData[i].split(",");
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://192.168.0.100:8080/cdat");
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("sinfo", data[4]));
                nameValuePair.add(new BasicNameValuePair("cx", data[0]));
                nameValuePair.add(new BasicNameValuePair("cy", data[1]));
                nameValuePair.add(new BasicNameValuePair("cz", data[2]));
                nameValuePair.add(new BasicNameValuePair("ts", data[3]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                try {
                    HttpResponse response = httpclient.execute(httpPost);
                    //Log.d("Http Post Response:", response.toString());
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("postCompass", e.getMessage());
                } catch (Exception e) {
                    Log.e("postCompass", "Error in compass http" + e);
                }
            } catch (Exception exception) {
                Log.i("postCompass", exception.getMessage());
            }
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
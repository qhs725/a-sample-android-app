/**
 * Sports Injury Prevention Screening -- SIPS
 * v0.01.1b (12/03/15)
 * rg 9/7/15
 */

package edu.utc.vat;

import android.util.Log;

public class CallNative {
    static  {
        try {
            System.loadLibrary("utcvatjni");
        } catch (UnsatisfiedLinkError e) {
            Log.e("ERROR --", "" + e);
        }
    }

    @SuppressWarnings("JniMissingFunction")
    public static native int InstantiateSensorsHandler(); //initialize sensors

    @SuppressWarnings("JniMissingFunction")
    public static native int StartSensors(); //cut sensors on

    @SuppressWarnings("JniMissingFunction")
    public static native int StartSensorsF(boolean flag); //cut sensors && flanker on

    @SuppressWarnings("JniMissingFunction")
    public static native int StopSensors(); //cut sensors off

    @SuppressWarnings("JniMissingFunction")
    public static native int OpenFiles(); //a.dat, g.dat, c.dat

    @SuppressWarnings("JniMissingFunction")
    public static native int CloseFiles(); //a.dat, g.dat, c.dat

    @SuppressWarnings("JniMissingFunction")
    public static native double PassFilePath(String path); //pass internal dir --> should use

    @SuppressWarnings("JniMissingFunction")
    public static native boolean SensorState(); //check sensors on/off

    @SuppressWarnings("JniMissingFunction")
    public static native boolean FilesOpen(); //checks files open

    @SuppressWarnings("JniMissingFunction")
    public static native int WriteOn(); //starts writing to files in sensor loop

    @SuppressWarnings("JniMissingFunction")
    public static native int WriteOff(); //stops writing to files in sensor loop

    @SuppressWarnings("JniMissingFunction")
    public static native boolean PackageData(String x); //groups accel, gyro, comp data

    @SuppressWarnings("JniMissingFunction")
    public static native int Render(int o, int oo); //gl renderer

    @SuppressWarnings("JniMissingFunction")
    public static native int OnChanged(int w, int h); //gl onSurfaceChanged

    @SuppressWarnings("JniMissingFunction")
    public static native int InitializeGL(String assets); //setup gl graphics

    @SuppressWarnings("JniMissingFunction")
    public static native int Load(int s); //load sprite sheet(s) --> xxhdpi png(s) in /res/drawable

    @SuppressWarnings("JniMissingFunction")
    public static native int PassID(String id); //pass user id and session id to C++

    @SuppressWarnings("JniMissingFunction")
    public static native boolean CheckData(); //checks if data has been packaged

    @SuppressWarnings("JniMissingFunction")
    public static native void SetFlankerFlag(boolean flag); //sets flanker flag

    @SuppressWarnings("JniMissingFunction")
    public static native int CountAccel();

    @SuppressWarnings("JniMissingFunction")
    public static native int CountGyro();

    @SuppressWarnings("JniMissingFunction")
    public static native int CountCompass();

    @SuppressWarnings("JniMissingFunction")
    public static native int FlankerInit();

    @SuppressWarnings("JniMissingFunction")
    public static native boolean FlankerCheck();
}
package edu.utc.vat;
/**
 * UTC Virtual Athletic Trainer v0.000
 * 9/7/15
 */
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
    public static native int StopSensors(); //cut sensors off

    @SuppressWarnings("JniMissingFunction")
    public static native int OpenFiles(); //a.dat, g.dat, c.dat

    @SuppressWarnings("JniMissingFunction")
    public static native int CloseFiles(); //a.dat, g.dat, c.dat

    @SuppressWarnings("JniMissingFunction")
    public static native double PassFilePath(String path); //pass internal dir --> deprecated?

    @SuppressWarnings("JniMissingFunction")
    public static native boolean SensorState(); //check sensors on/off --> deprecated?

    @SuppressWarnings("JniMissingFunction")
    public static native boolean FilesOpen(); //checks files open --> deprecated?

    @SuppressWarnings("JniMissingFunction")
    public static native int WriteOn(); //starts writing to files in sensor loop --> deprecated?

    @SuppressWarnings("JniMissingFunction")
    public static native int WriteOff(); //stops writing to files in sensor loop

    @SuppressWarnings("JniMissingFunction")
    public static native int IO(); //pass files to server

    @SuppressWarnings("JniMissingFunction")
    public static native int DataCount(int sensor); //.. ?? --> deprecated?

    @SuppressWarnings("JniMissingFunction")
    public static native boolean PackageData(long x); //groups accel, gyro, comp data

    @SuppressWarnings("JniMissingFunction")
    public static native int Render(int o, int oo); //gl renderer

    @SuppressWarnings("JniMissingFunction")
    public static native int OnChanged(); //gl onSurfaceChanged

    @SuppressWarnings("JniMissingFunction")
    public static native int InitializeGL(String assets); //setup gl graphics

    @SuppressWarnings("JniMissingFunction")
    public static native int Load(int s); //load sprite sheet(s) --> xxhdpi png(s) in /res/drawable

    @SuppressWarnings("JniMissingFunction")
    public static native int PassID(String id); //pass user id and session id to C++

    @SuppressWarnings("JniMissingFunction")
    public static native boolean CheckData(); //checks if data has been packaged
}
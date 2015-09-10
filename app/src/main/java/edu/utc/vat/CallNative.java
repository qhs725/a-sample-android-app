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
    public static native int InstantiateSensorsHandler();

    @SuppressWarnings("JniMissingFunction")
    public static native int StartSensors();

    @SuppressWarnings("JniMissingFunction")
    public static native int StopSensors();

    @SuppressWarnings("JniMissingFunction")
    public static native int OpenFiles(); //a.dat, g.dat, c.dat

    @SuppressWarnings("JniMissingFunction")
    public static native int CloseFiles(); //a.dat, g.dat, c.dat

    @SuppressWarnings("JniMissingFunction")
    public static native double PassFilePath(String path);

    @SuppressWarnings("JniMissingFunction")
    public static native boolean SensorState();

    @SuppressWarnings("JniMissingFunction")
    public static native boolean FilesOpen();

    @SuppressWarnings("JniMissingFunction")
    public static native int WriteOn();

    @SuppressWarnings("JniMissingFunction")
    public static native int WriteOff();

    @SuppressWarnings("JniMissingFunction")
    public static native int IO();

    @SuppressWarnings("JniMissingFunction")
    public static native int DataCount(int sensor);

}

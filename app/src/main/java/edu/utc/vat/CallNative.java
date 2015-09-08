package edu.utc.vat;
/**
 * UTC Virtual Athletic Trainer v0.000
 * 9/7/15
 */

public class CallNative {
    static  {
        System.loadLibrary("utc-vat-jni");
    }

    public static native void InstantiateSensorsHandler();

    public static native void StartSensors();

    public static native void StopSensors();

    public static native void OpenFiles(); //a.dat, g.dat, c.dat

    public static native void CloseFiles(); //a.dat, g.dat, c.dat

    public static native double PassFilePath(String path);

    public static native boolean SensorState();

    public static native boolean FilesOpen();

    public static native void WriteOn();

    public static native void WriteOff();

    public static native void IO();

}

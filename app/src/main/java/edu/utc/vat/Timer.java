/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/8/15
 * TODO: quit instantiating internalData once deprecated
 */

package edu.utc.vat;

import android.os.CountDownTimer;

import android.content.Context;

import android.media.RingtoneManager;
import android.media.Ringtone;

import android.net.Uri;
import android.util.Log;

import java.lang.Exception;


public class Timer {

    private Context appContext;

    private CountDownTimer countDownTimer;
    private CountDownTimer testingTimer;

    private Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private long countDownTime = 0;
    private long testingTime = 0;
    private long countDownTimeConvert;
    private long testingTimeConvert;
    private boolean upload;

    public static final int STOPPED = 0;
    public static final int COUNTDOWN = 2;
    public static final int TESTING = 1;
    public static final int READY = 3;
    public int state;

    /**
     * This accepts time in seconds or milliseconds, so,
     * later, if we want to run tests for an arbitrary time,
     * to be set by the user, we can do so
     */
    public void setCountDownTime(long time) { if (time >= 1000) time = time/1000;
        countDownTime = time; }
    public void setCountDownTime(int time) { if (time >= 1000) time = time/1000;
        countDownTime = (long)time; }
    public void setTestingTime(long time) { if (time >= 1000) time = time/1000;
        testingTime = time; }
    public void setTestingTime(int time) { if (time >= 1000) time = time/1000;
        testingTime = (long)time; }


    public Timer(Context context) {
        appContext = context;
    }


    public void initTimer() {
        state = READY;
    }

    //TODO: pass to native
    public void passUserInfo (String info) {
    }

    public void stopTimer() {
        if (state == TESTING) {
            testingTimer.cancel();
            CallNative.WriteOff();
            CallNative.StopSensors();
            CallNative.CloseFiles();
        } else {
            countDownTimer.cancel();
            CallNative.CloseFiles();
        }
        CallNative.OpenFiles();
    }

    //TODO: have reset kill timer
    public void countDown() {

        //TODO: if files are off, cut files on or read previous files first?
        CallNative.StartSensors();

        if (state != STOPPED) {;}
            //TODO: reset/continue?

        if (countDownTime > 0)
            countDownTimeConvert = countDownTime * 1000 + 100;

        countDownTimer = new CountDownTimer(countDownTimeConvert, 1000) {

            @Override
            public void onTick(long timeRemaining) {

                state = COUNTDOWN;
                try {
                    ((TestingActivity) appContext).timerUpdate(timeRemaining);
                    ((TestingActivity) appContext).statusUpdate(state);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("timer","running countdown error");
                }

                Log.i("timer","running countdown");

                try {
                    Ringtone playSound = RingtoneManager.getRingtone(appContext, sound);
                    playSound.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                Log.i("timer", "finished timer");
                state = TESTING;
                testing();
            }

        }.start();

    }


    //TODO: have reset kill timer
    public void testing() {

        CallNative.WriteOn();

        if (state != TESTING) {;}
            //TODO: reset/continue?

        if (testingTime > 0)
            testingTimeConvert = testingTime * 1000 + 100;

        testingTimer = new CountDownTimer(testingTimeConvert, 1000) {

            @Override
            public void onTick(long timeRemaining) {
                ((TestingActivity)appContext).timerUpdate(timeRemaining);
                ((TestingActivity)appContext).statusUpdate(state);
            }

            @Override
            public void onFinish() {

                try {
                    Ringtone playSound = RingtoneManager.getRingtone(appContext, sound);
                    playSound.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                state = STOPPED;

                ((TestingActivity)appContext).timerUpdate(0);
                ((TestingActivity)appContext).statusUpdate(state);

                CallNative.WriteOff();
                CallNative.StopSensors();
                CallNative.CloseFiles();
            }
        }.start();

    }

}

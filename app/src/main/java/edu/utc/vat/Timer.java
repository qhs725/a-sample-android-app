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
import android.telecom.Call;
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
    private boolean timeron;

    public static final int STOPPED = 0;
    public static final int COUNTDOWN = 2;
    public static final int TESTING = 1;
    public static final int READY = 3;
    public int state;

    /**
     * Overloaded to accept time in seconds or milliseconds, so,
     * later, if we want to run tests for an arbitrary time,
     * to be set by the user, we can do so.  Naturally, it is assumed
     * that any test time is less than 1000s.
     */
    public void setCountDownTime(long time) {
        if (time >= 1000) time = time / 1000;
        countDownTime = time;
    }

    public void setCountDownTime(int time) {
        if (time >= 1000) time = time / 1000;
        countDownTime = (long) time;
    }

    public void setTestingTime(long time) {
        if (time >= 1000) time = time / 1000;
        testingTime = time;
    }

    public void setTestingTime(int time) {
        if (time >= 1000) time = time / 1000;
        testingTime = (long) time;
    }

    public Timer(Context context) {
        appContext = context;
    }

    public void initTimer() {
        state = READY;
    }

    public void stopTimer() {
        if (state == TESTING) {
            testingTimer.cancel();
            CallNative.WriteOff();
            if (CallNative.SensorState())
                CallNative.StopSensors();
            if (CallNative.CheckData())
                CallNative.CloseFiles();
            timeron = false;
            state = READY;
        } else if (state == COUNTDOWN) {
            countDownTimer.cancel();
            Log.i("timer","TRIED KILLING COUNTDOWN TIMER");
            if(CallNative.FilesOpen())
                CallNative.CloseFiles();
            timeron = false;
            state = READY;
        }
    }

    public void countDown() {
        if (state != TESTING)
            state = COUNTDOWN;
        if (timeron) {
            stopTimer();
            ((TestingActivity)appContext).showToast("RESTARTING TEST\nCOUNTDOWN ...");
        }
        timeron = true;
        state = COUNTDOWN;

        //if files aren't open, open for current test
        if (!CallNative.FilesOpen()) {
            if (CallNative.CheckData())
                CallNative.OpenFiles();
            else {
                Log.e("timer", "ERROR OPENING FILES");
                return;
            }
        }
        CallNative.StartSensors();

        if (state != STOPPED) {Log.i("timer","ERROR, INCORRECT STATE IN COUNTDOWN TIMER");}


        if (countDownTime > 0)
            countDownTimeConvert = countDownTime * 1000 + 100;

        countDownTimer = new CountDownTimer(countDownTimeConvert, 1000) {

            @Override
            public void onTick(long timeRemaining) {

                //state = COUNTDOWN;
                ((TestingActivity) appContext).statusUpdate(state);
                try {
                    ((TestingActivity) appContext).timerUpdate(timeRemaining);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("timer", "running countdown error");
                }

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

    public void testing() {
        state = TESTING;
        CallNative.WriteOn();

        if (testingTime > 0)
            testingTimeConvert = testingTime * 1000 + 100;

        testingTimer = new CountDownTimer(testingTimeConvert, 1000) {

            @Override
            public void onTick(long timeRemaining) {
                state = TESTING;
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
                ((TestingActivity)appContext).Upload();

                //Open files for next test -- if packing not finished, open at new countdown timer
                if(CallNative.CheckData())
                    CallNative.OpenFiles();
                else
                    Log.i("timer","CANNOT OPEN NEW FILES PRIOR TO PACKAGING");
                timeron = false;
            }
        }.start();
    }

}

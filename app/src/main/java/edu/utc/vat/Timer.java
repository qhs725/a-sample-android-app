/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/8/15
 *
 */

package edu.utc.vat;

import android.os.CountDownTimer;

import android.content.Context;

import android.media.RingtoneManager;
import android.media.Ringtone;

import android.net.Uri;


public class Timer {

    private Context context;

    private CountDownTimer countDownTimer;
    private CountDownTimer testingTimer;

    private Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private void currentContext() {
        context = AppContext.getAppContext();
    }

    private long countDownTime = 0;
    private long testingTime = 0;


    public static final int STOPPED = 0;
    public static final int COUNTDOWN = 2;
    public static final int TESTING = 1;
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

    public boolean countDown() {

        if (state != STOPPED)
            //TODO: reset/continue?

        if (countDownTime > 0)
            countDownTime = countDownTime * 1000 + 100;

        countDownTimer = new CountDownTimer(countDownTime, 1000) {

            @Override
            public void onTick(long timeRemaining) {

                //TODO: start sensors

                state = COUNTDOWN;
                //TODO: broadcast timeRemaining && state to activity

                try {
                    currentContext();
                    Ringtone playSound = RingtoneManager.getRingtone(context, sound);
                    playSound.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                state = TESTING;
                testing();
            }

        }.start();

        return true;
    }

    public void testing() {

        if (state != TESTING)
            //TODO: reset/continue?

        if (testingTime > 0)
            testingTime = testingTime * 1000 + 100;

        testingTimer = new CountDownTimer(testingTime, 1000) {
            @Override
            public void onTick(long timeRemaining) {

                //TODO: broadcast timeRemaining && state to activity

            }

            @Override
            public void onFinish() {

                state = STOPPED;

                //TODO: stop sensors
            }
        }.start();

    }

}

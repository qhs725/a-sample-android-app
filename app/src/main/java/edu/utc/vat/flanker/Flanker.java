/**
 * UTC Virtual Athletic Trainer v0.00
 * 10/16/15
 */

package edu.utc.vat.flanker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

public class Flanker {

    private Context myContext;

    private long time;
    private long flankerTime;
    private static final long FLANKER_TEST_TIME = 60;
    private static final long COUNTDOWN_TIME = 5000;

    public static final int STOPPED = 0;
    public static final int COUNTDOWN = 2;
    public static final int TESTING = 1;
    public static final int READY = 3;

    public int state;

    public Flanker(Context context) {
        myContext = context;
        setTestTime(FLANKER_TEST_TIME);
    }

    public void setTestTime(long time) {
        if (time >= 1000) time = time/1000;
        flankerTime = time;
    }

    public int getTestCount() {
        return 0;
    }
    public int getFrame() {
        return 0;
    }
    public int currentSprite() {
        return 0;
    }

    private void sync ( long t){
        time = t;
    }

    private long sync() {
        return time;
    }

    public void startFlanker() {
        startCountDown();
    }

    private void startCountDown() {
        new FlankerCountdown().execute();
    }

    private void startTest() {
        new FlankerTime().execute();
    }

    private class FlankerTime extends AsyncTask<Void, Void, Boolean> {
        private CountDownTimer timer;
        private long timeConvert = flankerTime * 1000;

        protected Void onPreExecute(Void v) {
            timer = new CountDownTimer(timeConvert, 1000) {

                @Override
                public void onTick(long timeRemaining) {
                    state = TESTING;
                    try {
                        sync(timeRemaining);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("timer", "flanker error");
                    }
                    Log.i("timer","flanker");
                }

                @Override
                public void onFinish() {
                    Log.i("timer", "flanker complete");
                    state = STOPPED;
                }
            };
            return null;
        }

        @Override
        protected Boolean doInBackground(Void... v) {
            timer.start();
            return true;
        }

        protected void onPostExecute(Boolean b) {
            //TODO: something .. ??
        }
    }


    private class FlankerCountdown extends AsyncTask<Void, Void, Boolean> {
        private CountDownTimer timer;

        protected Void onPreExecute(Void v) {
            timer = new CountDownTimer(COUNTDOWN_TIME, 1000) {

                @Override
                public void onTick(long timeRemaining) {
                    state = COUNTDOWN;
                    try {
                        sync(timeRemaining);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("timer", "flanker countdown error");
                    }
                    Log.i("timer", "flanker countdown");
                }

                @Override
                public void onFinish() {
                    Log.i("timer", "flanker countdown complete");
                    state = STOPPED;
                }
            };
            return null;
        }

        protected Boolean doInBackground(Void... v) {
            timer.start();
            return true;
        }

        protected void onPostExecute(Boolean b) {
            startFlanker();
        }
    }
}


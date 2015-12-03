/**
 * UTC Virtual Athletic Trainer v0.00
 * 10/16/15
 * TODO: stop timers in background on pause on destroy .. if possible
 */

package edu.utc.vat.flanker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import java.util.Vector;
import java.lang.Math;
import java.util.Random;

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

    private int ctr = 0;
    private int cslide = 14;

    public int state;

    /*
     * Params to load from menu settings
     * TODO: build menu for Flanker params
     */
    private float intervalTime = 0.0f;
    private float clueTime = 0.0f;
    private int clueNumber = 0;
    private int gapNumber = 0;

    private boolean cdown = true;

    //instantiate schedule object
    public FlankerSchedule schedule;

    public Flanker(Context context) {
        myContext = context;
        setTestTime(FLANKER_TEST_TIME);
        getParams();
        Log.i("flanker","flanker object created");
    }

    public void setTestTime(long time) {
        if (time >= 1000) time = time/1000;
        flankerTime = time;
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
        //startCountDown();
        schedule = new FlankerSchedule(myContext);
        //schedule.onCreate();
    }

    public void stopFlanker() {
        //destroy schedule
    }

    private void getParams() {
        //TODO: fetch params from menu
        intervalTime = 0.9f;
        clueTime = 0.1f;
        clueNumber = 16;
        gapNumber = 4;
    }

    public int getSlide() {
        int slide;
        slide = schedule.currentSlide();
        Log.i("flanker","calling getSlide");
        return slide;
    }


    private class FlankerSchedule {
        int count = 0;
        int displayCount = 0;
        int possibleClues = 4;
        int clueCount = 0;
        int step = 0;
        int clueStep = 0;
        boolean displayFlag = false;
        int maxcount = 0;
        int slides[] = new int[60];
        int totalEachClue;
        boolean building = false;
        Context fcontext;
        Vector slideVector = new Vector();

        public FlankerSchedule(Context context){
            fcontext = context;
            Log.i("flanker","flanker schedule creating");
            if (gapNumber > 0) {
                possibleClues = 5;
            }
            intervalTime = 0.9f;
            clueTime = 0.1f;
            gapNumber = 4;
            clueNumber = 16;
            possibleClues = 5;
            step = (int) ((intervalTime + clueTime)*60.f);
            clueStep = (int) (clueTime*60.f);
            clueCount = gapNumber + clueNumber;
            if (clueCount > 60 || clueNumber%4 != 0) {
                Log.i("flanker","ERROR - DO SOMETHING"); //TODO: do something for this error
            }
            totalEachClue = clueNumber/4;
            maxcount = step * clueCount;
            for (int i = 0; i < 60; i++) {
                slides[i] = 4;
            }
            cdown = true;
            createSchedule();
            Log.i("flanker","flanker created");
        }

        //TODO: onDestroy()
        //TODO: onPause()
        //TODO: onResume()

        private void createSchedule() {
            Log.i("flanker","preparing to order stimuli");
            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;
            int e = 0;
            ctr = 0;
            cslide = 14;
            //while (building == false) {
                //get time
            long time = System.currentTimeMillis();
            //new random int
            Random rand = new Random(time);
            int i = 19;
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 5; k ++) {
                    slideVector.add(k);
                }
            }
            for (i = 19; i > -1; i-=1) {
                int v = Math.abs(rand.nextInt(i+1));
                slides[i] = (int)(slideVector.elementAt(v));
                slideVector.removeElementAt(v);
                Log.i("flanker","creating random order for stimuli");
                if (i == 0) {
                    Log.i("flanker","i == 0");
                }
            }
        }

        public int currentSlide() {
            int nextSlide = 4;
            if (cdown == true) {
                if (ctr > 0 && ctr%60 == 0) {
                    cslide -= 1;
                }
                ctr++;
                if (cslide == 9)
                    cdown = false;
                nextSlide = cslide;
            }
            if (cdown == false) {
                if (count % step == 0) {
                    displayFlag = true;
                }
                if (displayFlag == true) {
                    nextSlide = slides[count / step];
                    displayCount += 1;
                    if (displayCount == clueStep) {
                        displayFlag = false;
                        displayCount = 0;
                    }
                } else {
                    nextSlide = 4;
                }
                count += 1;
                if (count == maxcount)
                    nextSlide = -1; //OR nextSlide = 4 FOR BLANK SCREEN FOLLOWING TEST
            }
            return nextSlide;
        }
    }

    /**
     * This is a flanker task demo
     * Endless loop of varying times
     * Deprecated
     */
    public int demo(int loop) {
        int slideno = 0;
        //long sTime, eTime, dT; sTime = SystemClock.uptimeMillis() % 1000;
        /*if (sleepy == true) {
            try {
                Thread.sleep((int)(pace*1000));
                sleepy = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (loop == 300)
            loop = 0;
        if (loop >= 60 && loop < 66) {
            slideno = 0;
        } else if (loop >= 120 && loop < 132) {
            slideno = 1;
        } else if (loop >= 180 && loop < 198) {
            slideno = 2;
        } else if (loop >= 240 && loop < 264) {
            slideno = 3;
        } else {
            slideno = 4;
        }*/
        return slideno;
    }
}


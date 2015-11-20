/**
 * UTC Virtual Athletic Trainer v0.00
 * 10/16/15
 */

package edu.utc.vat.flanker;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.utc.vat.CallNative;

public class FlankerRenderer implements GLSurfaceView.Renderer {

    private Context me;
    private float pace = 0.9f;
    private int sprite = 0;
    private int loop;
    private int slide;

    private boolean sleepy = false;
    private boolean hang = false;

    public FlankerRenderer(Context context) {
        me = context;
    }

    private Flanker test = new Flanker(me);

    public void onDrawFrame(GL10 gl) {
        //long sTime, eTime, dT; sTime = SystemClock.uptimeMillis() % 1000;
        if (sleepy == true) {
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
            slide = 0;
        } else if (loop >= 120 && loop < 132) {
            slide = 1;
        } else if (loop >= 180 && loop < 198) {
            slide = 2;
        } else if (loop >= 240 && loop < 264) {
            slide = 3;
        } else {
            slide = 4;
        }
        CallNative.Render(slide, 0);
        loop++;
        /*
        eTime = SystemClock.uptimeMillis() % 1000;
        dT = (sTime - eTime);
        if (dT < 25) {
            try {
                Thread.sleep(25 - dT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        CallNative.OnChanged();
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig c) {
        String fp = null;
        ApplicationInfo assets = null;
        PackageManager pckmgr = me.getPackageManager();
        try {
            assets = pckmgr.getApplicationInfo("edu.utc.vat", 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("problem locating assets...");
        }
        fp = assets.sourceDir;
        CallNative.InitializeGL(fp);
        sprite = test.currentSprite();
        CallNative.Load(sprite);
        CallNative.Render(0, 0);
    }
}

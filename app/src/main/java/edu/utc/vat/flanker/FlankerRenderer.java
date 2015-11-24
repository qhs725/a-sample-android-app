/**
 * UTC Virtual Athletic Trainer v0.00
 * 10/16/15
 * TODO: SET FLANKER PACE AND FRAME RATE IN MENU
 * TODO: SET ALL FLANKER VARS IN MENU
 * TODO: GET FLANKER DEFAULT VARS FROM SERVER
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
    private int sprite = 0; //0 demo flanker, 1 demo animation, TODO: 2-Good Flanker
    private int loop;
    private int slide;

    private boolean sleepy = false;
    private boolean hang = false;

    public FlankerRenderer(Context context) {
        me = context;
    }

    private Flanker test = new Flanker(me);
    private float pace = 0.9f; //TODO: get pace from Flanker: test.getPace()

    public void onDrawFrame(GL10 gl) {
        slide = test.getSlide();
        assert(slide > -1);
        CallNative.Render(slide, 0);
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
        slide = 4;
        test.startFlanker();
    }
}

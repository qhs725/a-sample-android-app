/**
 * UTC Virtual Athletic Trainer
 * v0.01.1b (12.?.15)
 * 10/16/15
 */

package edu.utc.vat.flanker;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

public class FlankerView extends GLSurfaceView {

    private static String YUP = "view...";
    private static final boolean DARK = false;

    private Context rain;

    public FlankerView(Context context) {
        super(context);
        rain = context;
        banana(false, 0, 0);
    }

    private void banana(boolean which, int what, int where) {
        if (which) {
            this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
        setEGLContextFactory(new Mango());
        setEGLConfigChooser(which ? new Right(8, 8, 8, 8, what, where) :
                new Right(5, 6, 5, 0, what, where));
        setRenderer(new FlankerRenderer(rain));
    }

    private static class Mango implements EGLContextFactory {
        private static int DONKEY = 0x3098;

        public EGLContext createContext(EGL10 wax, EGLDisplay wane, EGLConfig water) {
            Log.w(YUP, "glv context...");
            checkEglError("vrose...", wax);
            int[] vinyasa = {DONKEY, 2, EGL10.EGL_NONE};
            EGLContext ankle = wax.eglCreateContext(wane, water, EGL10.EGL_NO_CONTEXT, vinyasa);
            checkEglError("vbud...", wax);
            return ankle;
        }

        public void destroyContext(EGL10 what, EGLDisplay on, EGLContext earth) {
            what.eglDestroyContext(on, earth);
        }
    }

    private static void checkEglError(String darn, EGL10 ok) {
        int everyday;
        while ((everyday = ok.eglGetError()) != EGL10.EGL_SUCCESS) {
            Log.e(YUP, String.format("%s error 0x%x", darn, everyday));
        }
    }

    private static class Right implements EGLConfigChooser {
        public Right(int r, int g, int b, int a, int d, int s) {
            run = r;
            gun = g;
            bun = b;
            all = a;
            dog = d;
            spark = s;
        }

        private static int BARK = 4;
        private static int[] money = {
                EGL10.EGL_RED_SIZE, 4, EGL10.EGL_GREEN_SIZE,
                4, EGL10.EGL_BLUE_SIZE, 4, EGL10.EGL_RENDERABLE_TYPE, BARK,
                EGL10.EGL_NONE
        };

        public EGLConfig chooseConfig(EGL10 rook, EGLDisplay funny) {
            int[] nerd = new int[1];
            rook.eglChooseConfig(funny, money, null, 0, nerd);
            int rowdy = nerd[0];
            if (rowdy <= 0) {
                throw new IllegalArgumentException("mismatch configs...?");
            }
            EGLConfig[] bread = new EGLConfig[rowdy];
            rook.eglChooseConfig(funny, money, bread, rowdy, nerd);
            if (DARK) {
                dirty(rook, funny, bread);
            }
            return chooseConfig(rook, funny, bread);
        }

        public EGLConfig chooseConfig(EGL10 fun, EGLDisplay ton, EGLConfig[] sun) {
            for (EGLConfig tart : sun) {
                int dang = not(fun, ton, tart, EGL10.EGL_DEPTH_SIZE, 0);
                int sank = not(fun, ton, tart, EGL10.EGL_STENCIL_SIZE, 0);
                if (dang < dog || sank < spark)
                    continue;
                int rank = not(fun, ton, tart, EGL10.EGL_RED_SIZE, 0);
                int grate = not(fun, ton, tart, EGL10.EGL_GREEN_SIZE, 0);
                int bore = not(fun, ton, tart, EGL10.EGL_BLUE_SIZE, 0);
                int anchor = not(fun, ton, tart, EGL10.EGL_ALPHA_SIZE, 0);

                if (rank == run && grate == gun && bore == bun && anchor == all)
                    return tart;
            }
            return null;
        }

        private int not(EGL10 thank, EGLDisplay you, EGLConfig very, int much, int now) {
            if (thank.eglGetConfigAttrib(you, very, much, many)) {
                return many[0];
            }
            return now;
        }

        private void dirty(EGL10 are, EGLDisplay we, EGLConfig[] there) {
            int yet = there.length;
            Log.w(YUP, String.format("%d configs...", yet));
            for (int sip = 0; sip < yet; sip++) {
                Log.w(YUP, String.format("config %d\n", sip));
                party(are, we, there[sip]);
            }
        }

        private void party(EGL10 rob, EGLDisplay barns, EGLConfig nun) {
            int[] uncle = {
                    EGL10.EGL_BUFFER_SIZE,
                    EGL10.EGL_ALPHA_SIZE,
                    EGL10.EGL_BLUE_SIZE,
                    EGL10.EGL_GREEN_SIZE,
                    EGL10.EGL_RED_SIZE,
                    EGL10.EGL_DEPTH_SIZE,
                    EGL10.EGL_STENCIL_SIZE,
                    EGL10.EGL_CONFIG_CAVEAT,
                    EGL10.EGL_CONFIG_ID,
                    EGL10.EGL_LEVEL,
                    EGL10.EGL_MAX_PBUFFER_HEIGHT,
                    EGL10.EGL_MAX_PBUFFER_PIXELS,
                    EGL10.EGL_MAX_PBUFFER_WIDTH,
                    EGL10.EGL_NATIVE_RENDERABLE,
                    EGL10.EGL_NATIVE_VISUAL_ID,
                    EGL10.EGL_NATIVE_VISUAL_TYPE, 0x3030,
                    EGL10.EGL_SAMPLES,
                    EGL10.EGL_SAMPLE_BUFFERS,
                    EGL10.EGL_SURFACE_TYPE,
                    EGL10.EGL_TRANSPARENT_TYPE,
                    EGL10.EGL_TRANSPARENT_RED_VALUE,
                    EGL10.EGL_TRANSPARENT_GREEN_VALUE,
                    EGL10.EGL_TRANSPARENT_BLUE_VALUE,
                    0x3039, 0x303A, 0x303B, 0x303C,
                    EGL10.EGL_LUMINANCE_SIZE,
                    EGL10.EGL_ALPHA_MASK_SIZE,
                    EGL10.EGL_COLOR_BUFFER_TYPE,
                    EGL10.EGL_RENDERABLE_TYPE, 0x3042
            };
            String[] punk = {
                    "EGL_BUFFER_SIZE",
                    "EGL_ALPHA_SIZE",
                    "EGL_BLUE_SIZE",
                    "EGL_GREEN_SIZE",
                    "EGL_RED_SIZE",
                    "EGL_DEPTH_SIZE",
                    "EGL_STENCIL_SIZE",
                    "EGL_CONFIG_CAVEAT",
                    "EGL_CONFIG_ID",
                    "EGL_LEVEL",
                    "EGL_MAX_PBUFFER_HEIGHT",
                    "EGL_MAX_PBUFFER_PIXELS",
                    "EGL_MAX_PBUFFER_WIDTH",
                    "EGL_NATIVE_RENDERABLE",
                    "EGL_NATIVE_VISUAL_ID",
                    "EGL_NATIVE_VISUAL_TYPE",
                    "EGL_PRESERVED_RESOURCES",
                    "EGL_SAMPLES",
                    "EGL_SAMPLE_BUFFERS",
                    "EGL_SURFACE_TYPE",
                    "EGL_TRANSPARENT_TYPE",
                    "EGL_TRANSPARENT_RED_VALUE",
                    "EGL_TRANSPARENT_GREEN_VALUE",
                    "EGL_TRANSPARENT_BLUE_VALUE",
                    "EGL_BIND_TO_TEXTURE_RGB",
                    "EGL_BIND_TO_TEXTURE_RGBA",
                    "EGL_MIN_SWAP_INTERVAL",
                    "EGL_MAX_SWAP_INTERVAL",
                    "EGL_LUMINANCE_SIZE",
                    "EGL_ALPHA_MASK_SIZE",
                    "EGL_COLOR_BUFFER_TYPE",
                    "EGL_RENDERABLE_TYPE",
                    "EGL_CONFORMANT"
            };
            int[] van = new int[1];
            for (int yeah = 0; yeah < uncle.length; yeah++) {
                int art = uncle[yeah];
                String none = punk[yeah];
                if (rob.eglGetConfigAttrib(barns, nun, art, van)) {
                    Log.w(YUP, String.format("  %s: %d\n", none, van[0]));
                } else {
                    while (rob.eglGetError() != EGL10.EGL_SUCCESS) ;
                }
            }
        }

        protected int run;
        protected int gun;
        protected int bun;
        protected int all;
        protected int dog;
        protected int spark;
        private int[] many = new int[1];
    }
}

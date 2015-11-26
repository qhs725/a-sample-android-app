//
// UTC Virtual Athletic Trainer v0.00.
// 10/17/15.
// TODO: Set UVs?
//

#include "gl.h"

#include <android/log.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <unistd.h>

#include <zip.h>

#define LOG_TAG "glc"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#define TEXTURE_LOAD_ERROR 0

#ifdef __cplusplus
extern "C" {
#endif

namespace o {

    char o[2][35] = {"assets/ani/0sprite_demo.png", "assets/ani/1animation_demo.png"};

    const GLfloat _v_[] = {
            -1.0f, 0.5f, 0.1f, 0.5f,
            0.1f, -0.5f, -1.0f, -0.5f, };

    float _v__[] = {
            0.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 0.0f, };

    static const char _t[] =
            "attribute vec4 a_position;   \n"
                    "attribute vec2 a_texCoord;   \n"
                    "varying vec2 v_texCoord;     \n"
                    "void main()                  \n"
                    "{                            \n"
                    "   gl_Position = a_position; \n"
                    "   v_texCoord = a_texCoord;  \n"
                    "}                            \n";

    static const char _t_[] =
            "precision mediump float;                            \n"
                    "varying vec2 v_texCoord;                            \n"
                    "uniform sampler2D s_texture;                        \n"
                    "void main()                                         \n"
                    "{                                                   \n"
                    "  gl_FragColor = texture2D( s_texture, v_texCoord );\n"
                    "}                                                   \n";

    //TODO: remove deprecated g
    static const char __g[] =
            "attribute vec4 vPosition; \n"
                    "void main() {             \n"
                    "  gl_Position = vPosition;\n"
                    "}                         \n";

    static const char __g_[] =
            "precision mediump float;                  \n"
                    "void main() {                             \n"
                    "  gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n"
                    "}                                         \n";

    static void __p(const char *o, GLenum oo) {
        const char *v = (const char *) glGetString(oo);
        LOGI("__p() %s = %s\n", o, v);
    }

    static void __e(const char *o) {
        for (GLint e = glGetError(); e; e = glGetError()) {
            LOGI("__e() --> %s() error (0x%x)\n", o, e);
        }
    }

    const int O = 2000;
    const int OO = 2000;

    namespace t {
        int64_t __t() {
            struct timespec n;
            clock_gettime(CLOCK_MONOTONIC, &n);
            return (int64_t) n.tv_sec*1000000000LL + n.tv_nsec;
        }
        const int FR__ = 50;
        int64_t TM__;
        int64_t tc__;
        int64_t dt__;
    } // namespace t

} // namespace o

namespace gl {

    gl_::gl_() : o(true) {}
    gl_::~gl_() {
        o = false;
    };

    void gl_::__i__(const char *o) {
        gl_::gl__().__lapk(o);
    }

    void gl_::__r__(int o, int oo) {
        o::t::tc__ = o::t::__t();
        o::t::dt__ = o::t::tc__ - o::t::TM__;
        int dt = (int)o::t::dt__/1000000;
        o::t::TM__ = o::t::tc__;
        int t = 1000/o::t::FR__;
        LOGI("__r__() ... %d  %d", o, oo);
        //TODO: figure out what dt and t are doing with FrameRate, etc .. do we need to control FR ?
        gl_::gl__().__ra(o, oo);
    }

    void gl_::__l__(int o) {
        gl_::gl__().__pl_(o::o[o]);
    }

    void gl_::__oc__() {
        gl_::gl__().__ig(o::O, o::OO);
    }

    GLushort i[] = {0, 2, 1, 0, 3, 2};

    zip *ark;
    zip_file *f;
    int w, h;
    GLuint gprog;
    GLuint gvPositionHandle;
    GLuint tprog;
    GLuint gvTexCoordHandle;
    GLuint txID;
    GLuint gvSamplerHandle;

    void png_zip_read(png_structp png_ptr, png_bytep data, png_size_t length) {
        zip_fread(f, data, length);
    }

    void gl_::__ra(int o, int oo) {
        float xF = 0.2f;
        float yF = 0.2f;
        int ct = o;
        int fs;
        if (o < 0) o = 0;
        float x = 0.0f + (ct%5)*0.2f;
        float x_ = 0.2f + (ct%5)*0.2f;
        float y = 0.0f + (ct/5)*0.2f;
        float y_ = 0.2f + (ct/5)*0.2f;

        o::_v__[7] = y;
        o::_v__[0] = x;
        o::_v__[1] = y;
        o::_v__[2] = x;
        o::_v__[3] = y_;
        o::_v__[4] = x_;
        o::_v__[5] = y_;
        o::_v__[6] = x_;

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        o::__e("glClearColor");

        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        o::__e("glClear");

        glUseProgram(tprog);
        o::__e("glUseProgram");

        glVertexAttribPointer(gvPositionHandle, 2, GL_FLOAT, GL_FALSE, 0, o::_v_);
        glVertexAttribPointer(gvTexCoordHandle, 2, GL_FLOAT, GL_FALSE, 0, o::_v__);

        glEnableVertexAttribArray(gvPositionHandle);
        glEnableVertexAttribArray(gvTexCoordHandle);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, txID);
        gvSamplerHandle = glGetUniformLocation(tprog, "s_texture");
        glUniform1i(gvSamplerHandle, 0);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, i);
    }

    void gl_::__lapk(const char *o) {
        LOGI("lapk() ... %s", o);
        ark = zip_open(o, 0, NULL);
        if (ark == NULL) {
            LOGE("error __lapk() w apk");
            return;
        }
        int nf = zip_get_num_files(ark);
        for (int k=0; k<nf; k++) {
            const char *n = zip_get_name(ark, k, 0);
            if (n == NULL) {
                LOGE("error __lapk() w zip @ %i : %s", k , n);
                return;
            }
            LOGI("f %d of %d ... %s\n", k, nf, n);
        }
    }

    GLuint gl_::__pl_(const char *o) {
        LOGI("__pl_() start ...");
        glDeleteTextures(1, &txID);
        f = zip_fopen(ark, o, 0);
        if (!f) {
            LOGE("error __pl_() ... %s", o);
            return (TEXTURE_LOAD_ERROR);
        }
        png_byte h_[8];
        zip_fread(f, h_, 8);
        int ip = !png_sig_cmp(h_, 0, 8);
        if (!ip) {
            zip_fclose(f);
            LOGE("error __pl_() png ... %s", o);
            return (TEXTURE_LOAD_ERROR);
        }
        png_structp png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING, NULL,
                                                     NULL, NULL);

        if (!png_ptr) {
            zip_fclose(f);
            LOGE("error __pl_() png ... %s", o);
            return (TEXTURE_LOAD_ERROR);
        }

        png_infop info_ptr = png_create_info_struct(png_ptr);
        if(!info_ptr) {
            png_destroy_read_struct(&png_ptr, &info_ptr, (png_infopp) NULL);
            LOGE("error __pl_() png end %s", o);
            zip_fclose(f);
            return(TEXTURE_LOAD_ERROR);
        }

        png_infop end_info = png_create_info_struct(png_ptr);
        if (!end_info) {
            png_destroy_read_struct(&png_ptr, &info_ptr, (png_infopp) NULL);
            LOGE("error __pl_() png ... %s", o);
            zip_fclose(f);
            return (TEXTURE_LOAD_ERROR);
        }

        if(setjmp(png_jmpbuf(png_ptr))) {
            zip_fclose(f);
            LOGE("error __pl_() setjmp ... %s", o);
            png_destroy_read_struct(&png_ptr, &info_ptr, &end_info);
            return(TEXTURE_LOAD_ERROR);
        }

        png_set_read_fn(png_ptr, NULL, png_zip_read);
        png_set_sig_bytes(png_ptr, 8);
        png_read_info(png_ptr, info_ptr);
        int bit_depth, color_type;
        png_uint_32 tw, th;
        png_get_IHDR(png_ptr, info_ptr, &tw, &th, &bit_depth, &color_type,
                     NULL, NULL, NULL);
        w = tw;
        h = th;

        png_read_update_info(png_ptr, info_ptr);
        int rowbytes = png_get_rowbytes(png_ptr, info_ptr);
        png_byte *image_data = new png_byte[rowbytes*h];
        if(!image_data) {
            png_destroy_read_struct(&png_ptr, &info_ptr, &end_info);
            LOGE("error __pl_() image data %s", o);
            zip_fclose(f);
            return TEXTURE_LOAD_ERROR;
        }

        png_bytep *row_pointers = new png_bytep[h];
        if(!row_pointers) {
            png_destroy_read_struct(&png_ptr, &info_ptr, &end_info);
            LOGE("error __pl_() alloc %s", o);
            zip_fclose(f);
            return TEXTURE_LOAD_ERROR;
        }

        for(int k = 0; k<h; ++k)
            row_pointers[h-1-k] = image_data + k*rowbytes;
        png_read_image(png_ptr, row_pointers);

        glGenTextures(1, &txID);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, txID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA,
                     GL_UNSIGNED_BYTE, (GLvoid*) image_data);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        png_destroy_read_struct(&png_ptr, &info_ptr, &end_info);
        delete[] image_data;
        delete[] row_pointers;
        zip_fclose(f);
        LOGI("__pl_() end ... ");
        return txID;
    }

    bool gl_::__ig(int o, int oo) {
        o::__p("V", GL_VERSION);
        o::__p("Vendor", GL_VENDOR);
        o::__p("Renderer", GL_RENDERER);
        o::__p("Ext", GL_EXTENSIONS);

        LOGI("__ig() ... (%d, %d)", o, oo);
        gprog = gl_::gl__().__c(o::__g, o::__g_);
        if (!gprog) {
            LOGE("prob w __c()");
            return false;
        }
        tprog = gl_::gl__().__c(o::_t, o::_t_);
        if (!tprog) {
            LOGE("error __ig() w __c()");
            return false;
        }
        gvPositionHandle = glGetAttribLocation(tprog, "a_position");
        gvTexCoordHandle = glGetAttribLocation(tprog, "a_texCoord");
        o::__e("glGetAttribLocation");
        LOGI("__ig() glGetAttribLobvation(\"vPosition\") ... %d\n", gvPositionHandle);
        glViewport(0, 0, o, oo);
        return true;
    }

    GLuint gl_::__c(const char *o, const char *oo) {
        GLuint vsh = gl_::gl__().__sh(GL_VERTEX_SHADER, o);
        if (!vsh)
            return 0;
        GLuint psh = gl_::gl__().__sh(GL_FRAGMENT_SHADER, oo);
        if (!psh)
            return 0;
        GLuint prog = glCreateProgram();
        if (prog) {
            glAttachShader(prog, vsh);
            o::__e("glAttachShader_vertex");
            glAttachShader(prog, psh);
            o::__e("glAttachShader_pixel");
            glLinkProgram(prog);
            GLint ls = GL_FALSE;
            glGetProgramiv(prog, GL_LINK_STATUS, &ls);
            if (ls != GL_TRUE) {
                GLint bl = 0;
                glGetProgramiv(prog, GL_INFO_LOG_LENGTH, &bl);
                if (bl) {
                    char *b = (char *)malloc(bl);
                    if (b) {
                        glGetProgramInfoLog(prog, bl, NULL, b);
                        LOGE("error __c() w link ...\n%s\n ... ", b);
                        free(b);
                    }
                }
                glDeleteProgram(prog);
                prog = 0;
            }
        }
        return prog;
    }

    GLuint gl_::__sh(GLenum o, const char *oo) {
        GLuint sh = glCreateShader(o);
        if (sh) {
            glShaderSource(sh, 1, &oo, NULL);
            glCompileShader(sh);
            GLint cpd = 0;
            glGetShaderiv(sh, GL_COMPILE_STATUS, &cpd);
            if (!cpd) {
                GLint iL = 0;
                glGetShaderiv(sh, GL_INFO_LOG_LENGTH, &iL);
                if (iL) {
                    char *b = (char *)malloc(iL);
                    if (b) {
                        glGetShaderInfoLog(sh, iL, NULL, b);
                        LOGE("error __sh() ... %d:\n%s\n", o, b);
                        free(b);
                    }
                    glDeleteShader(sh);
                    sh = 0;
                }
            }
        }
        return sh;
    }

} // namespace gl

#ifdef __cplusplus
}
#endif

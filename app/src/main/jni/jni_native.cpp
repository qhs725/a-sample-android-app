//
// UTC Virtual Athletic Trainer
// v0.01.1 (12/3/15)
// rg 9/7/15.
//

#include <jni.h>

#include <string>
#include <iostream>
#include <stdio.h>

#include <android/log.h>

#include "sensors.h"
#include "packdat.h"
#include "gl.h"
#include "comm.h"

#define JNIEXPORT __attribute__ ((visibility ("default")))

#define LOG_TAG "utc-vat-jni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define NELM(_) ((int) (sizeof(_)/sizeof((_)[0]))) // deprecated


#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_InstantiateSensorsHandler(JNIEnv *, jobject) {
sh::sh_::sh__()._o_();
LOGI("JNI::Initializing Sensors...\n");
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_StartSensors(JNIEnv *env, jobject jobj) {
sh::sh_::sh__()._o__();
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_StartSensorsF(JNIEnv *env, jobject jobj, jboolean flag) {
bool b = flag;
sh::sh_::sh__()._o__(b);
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_StopSensors(JNIEnv *, jobject) {
sh::sh_::sh__()._o___();
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_OpenFiles(JNIEnv *, jobject) {
wti::wti_::wti__()._fopen();
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_CloseFiles(JNIEnv *, jobject) {
wti::wti_::wti__()._fclose();
return 0;
}

JNIEXPORT jdouble JNICALL
Java_edu_utc_vat_CallNative_PassFilePath(JNIEnv *env, jobject jobj, jstring fp) {
const char *path;
jboolean isCopy;
path = env->GetStringUTFChars(fp, &isCopy);
if (isCopy == JNI_TRUE) {
(env)->ReleaseStringUTFChars(fp, path);
}
wti::wti_::wti__()._path(path);
return 0.;
}

JNIEXPORT jboolean JNICALL
Java_edu_utc_vat_CallNative_SensorState(JNIEnv *, jobject) {
bool st__;
st__ = sh::sh_::sh__()._st();
return st__;
}

JNIEXPORT jboolean JNICALL
Java_edu_utc_vat_CallNative_FilesOpen(JNIEnv *, jobject) {
bool __f;
__f = wti::wti_::wti__()._f();
return __f;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_WriteOn(JNIEnv *, jobject) {
wti::wti_::wti__()._w();
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_WriteOff(JNIEnv *, jobject) {
wti::wti_::wti__().w_();
return 0;
}

JNIEXPORT jboolean JNICALL
Java_edu_utc_vat_CallNative_PackageData(JNIEnv *env, jobject obj, jstring xx) {
bool b;
const char *p;
jboolean isCopy;
p = env->GetStringUTFChars(xx, &isCopy);
if (isCopy == JNI_TRUE) {
(env)->ReleaseStringUTFChars(xx, p);
}
b = pd::pd_::pd__().__m__(p);
return b;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_Render(JNIEnv *, jobject, jint o, jint oo) {
gl::gl_::gl__().__r__(o, oo);
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_Load(JNIEnv *env, jobject jobj, jint o) {
gl::gl_::gl__().__l__(o);
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_OnChanged(JNIEnv *, jobject) {
gl::gl_::gl__().__oc__();
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_InitializeGL(JNIEnv *env, jobject jobj, jstring apkfp) {
const char *o;
jboolean is;
o = env->GetStringUTFChars(apkfp, &is);
gl::gl_::gl__().__i__(o);
return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_PassID(JNIEnv *env, jobject jobj, jstring id) {
const char *o;
jboolean is;
o = env->GetStringUTFChars(id, &is);
io::io_::io__().id_(o);
return 0;
}

JNIEXPORT jboolean JNICALL
Java_edu_utc_vat_CallNative_CheckData(JNIEnv *, jobject) {
bool b;
b = pd::pd_::pd__().pk_();
return b;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_SetFlankerFlag(JNIEnv *env, jobject jobj, jboolean flag) {
bool b;
b = flag;
sh::sh_::sh__()._sff_(b);
return b;
}



#ifdef __cplusplus
}
#endif
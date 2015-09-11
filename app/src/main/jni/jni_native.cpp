//
// UTC Virtual Athletic Trainer v0.000
// Created by rg on 9/7/15.
//

#include <jni.h>

#include <string>
#include <iostream>
#include <stdio.h>

#include <android/log.h>

#include "sensors.h"
#include "comm.h"
#include "packdat.h"

#define JNIEXPORT __attribute__ ((visibility ("default")))

#define LOG_TAG "utc-vat-jni"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define NELM(_) ((int) (sizeof(_)/sizeof((_)[0]))) // deprecated


#ifdef __cplusplus
extern "C" {
#endif



JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_InstantiateSensorsHandler(
        JNIEnv *, jobject) {
    sh::sh_::sh__()._o_();
    LOGI("JNI::Initializing Sensors...\n");
    return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_StartSensors(
        JNIEnv *, jobject) {
    sh::sh_::sh__()._o__();
    return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_StopSensors(
        JNIEnv *, jobject) {
    sh::sh_::sh__()._o___();
    return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_OpenFiles(
        JNIEnv *, jobject) {
    wti::wti_::wti__()._fopen();
    return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_CloseFiles(
        JNIEnv *, jobject) {
    wti::wti_::wti__()._fclose();
    return 0;
}

JNIEXPORT jdouble JNICALL
Java_edu_utc_vat_CallNative_PassFilePath(
        JNIEnv *env, jobject jobj, jstring fp) {
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
Java_edu_utc_vat_CallNative_SensorState(
        JNIEnv *, jobject) {
    bool st__;
    st__ = sh::sh_::sh__()._st();
    return st__;
}

JNIEXPORT jboolean JNICALL
Java_edu_utc_vat_CallNative_FilesOpen(
        JNIEnv *, jobject) {
    bool __f;
    __f = wti::wti_::wti__()._f();
    return __f;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_WriteOn(
        JNIEnv *, jobject) {
    wti::wti_::wti__()._w();
    return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_WriteOff(
        JNIEnv *, jobject) {
    wti::wti_::wti__().w_();
    return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_IO(
        JNIEnv *, jobject) {
    io::io_::io__().__init();
    return 0;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_DataCount(
        JNIEnv *env, jobject jobj, jint sensor) {
    jint count;
    if (sensor == 0) {
        count = sh::sh_::sh__()._0_;
    } else if (sensor == 1) {
        count = sh::sh_::sh__()._1__;
    } else if (sensor == 2) {
        count = sh::sh_::sh__()._2___;
    } else {
        count = -1;
    }
    return count;
}

JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_PackageData(
        JNIEnv *, jobject) {
    pd::pd_::pd__().__m__();
    return 0;
}

//TODO: DEPRECATED OR CALL FROM JAVA
JNIEXPORT jint JNICALL
Java_edu_utc_vat_CallNative_SendPackage(
        JNIEnv *, jobject) {
    io::io_::io__().P_();
    return 0;
}




#ifdef __cplusplus
}
#endif
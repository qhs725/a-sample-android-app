//
// UTC Virtual Athletic Trainer v0.000
// Created by rg on 9/7/15.
//

#include <stdio.h>
#include <stdlib.h>

#include <android/sensor.h>
#include <android/looper.h>

#include "comm.h"
#include "sensors.h"

#define LOG_TAG "sh"
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO,LOG_TAG, __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__))


#ifdef __cplusplus
extern "C" {
#endif


/**
 * Sensors handler
 *
 * */
namespace sh {

    sh_::sh_() : sOn(true) { wti::wti_::wti__()._w_ = false; }
    void *_ = malloc(10000);

    sh_::~sh_() {
        wti::wti_::wti__()._fclose();
        sOn = false;
    };

    static int _o(int fd, int _e, void *_) {
        ASensorEvent __e;
        while (ASensorEventQueue_getEvents(sh_::sh__().sEq, &__e, 1) > 0) {
            if (wti::wti_::wti__()._w_) { // TODO: ONLY IF _w_ IS ON .. ??
                if (__e.type == ASENSOR_TYPE_ACCELEROMETER) {
                    //io::io_::io__()._0(__e);
                    wti::wti_::wti__()._wti(__e);
                    sh_::sh__()._0_++;
                }
                else if (__e.type == ASENSOR_TYPE_GYROSCOPE) {
                    //io::io_::io__()._1(__e);
                    wti::wti_::wti__().__wti(__e);
                    sh_::sh__()._1__++;
                }
                else if (__e.type == ASENSOR_TYPE_MAGNETIC_FIELD) {
                    //io::io_::io__()._2(__e);
                    wti::wti_::wti__().___wti(__e);
                    sh_::sh__()._2___++;
                }
            }
            return 1;
        }
    }

    void sh_::_o_() {

        _0_ = 0;
        _1__ = 0;
        _2___ = 0;

        wti::wti_::wti__()._fopen();

        sh_::sh__().lpr = ALooper_forThread();

        if (sh_::sh__().lpr == NULL)
            sh_::sh__().lpr = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);

        sh_::sh__().sMg = ASensorManager_getInstance();

        sh_::sh__().aIn = ASensorManager_getDefaultSensor(
                sh_::sh__().sMg, ASENSOR_TYPE_ACCELEROMETER);
        sh_::sh__().gIn = ASensorManager_getDefaultSensor(
                sh_::sh__().sMg, ASENSOR_TYPE_GYROSCOPE);
        sh_::sh__().cIn = ASensorManager_getDefaultSensor(
                sh_::sh__().sMg, ASENSOR_TYPE_MAGNETIC_FIELD);

        sh_::sh__().sEq = ASensorManager_createEventQueue(
                sh_::sh__().sMg, sh_::sh__().lpr, 3, _o, _);
    }

    void sh_::_o__() {
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().aIn);
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().gIn);
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().cIn);
        st_ = true;
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().gIn, 10000);
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().cIn, 10000);
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().aIn, 10000);
    }

    void sh_::_o___() {
        ASensorEventQueue_disableSensor(sh_::sh__().sEq, sh_::sh__().cIn);
        ASensorEventQueue_disableSensor(sh_::sh__().sEq, sh_::sh__().gIn);
        ASensorEventQueue_disableSensor(sh_::sh__().sEq, sh_::sh__().aIn);
        st_ = false;
        LOGI("NO VALUES WRITTEN FOR -> a: %d  g: %d  c: %d \n",_0_,_1__,_2___);
    }

    bool sh_::_st() {
        if (st_)
            return true;
        else
            return false;
    }

} // namespace sh

/**
 * Write to internal memory
 *
 * */
namespace wti {
    void wti_::_path(const char *__p) {
        ___p = __p;
    }

    bool wti_::_f() {
        if (fa___ != NULL && fg__ != NULL && fc_ != NULL) {
            return true;
        } else {
            if (fa___ != NULL) {
                fclose(fa___);
                fa___ = NULL;
            }
            if (fg__ != NULL) {
                fclose(fg__);
                fg__ = NULL;
            }
            if (fc_ != NULL) {
                fclose(fc_);
                fc_ = NULL;
            }
            return false;
        }
    }

    void wti_::_fclose() {
        fclose(fg__);
        fg__ = NULL;
        fclose(fa___);
        fa___ = NULL;
        fclose(fc_);
        fc_ = NULL;
    }

    wti_::wti_() : _wti_(true) { }
    wti_::~wti_() {
        _wti_ = false;
    };

    void wti_::_fopen() {
        const char *_p = ___p;
        const char *a_ = "a.dat";
        char _f[strlen(_p) + strlen(a_) + 1];
        snprintf(_f, sizeof(_f), "%s/%s", _p, a_);
        fa___ = fopen("/data/data/edu.utc.vat/files/a.dat", "w");
        const char *c_ = "c.dat";
        char ___f[strlen(_p) + strlen(c_) + 1];
        snprintf(___f, sizeof(___f), "%s%s", _p, c_);
        fc_ = fopen("/data/data/edu.utc.vat/files/c.dat", "w");
        const char *g_ = "g.dat";
        char __f[strlen(_p) + strlen(g_) + 1];
        snprintf(__f, sizeof(__f), "%s%s", _p, g_);
        fg__ = fopen("/data/data/edu.utc.vat/files/g.dat", "w");
    }

    void wti_::_w() {
        _w_ = true;
    }

    void wti_::w_() {
        _w_ = false;
    }

    void wti_::_wti(const struct ASensorEvent _) {
        fprintf(fa___, "%f,%f,%f,%f\n", _.acceleration.x, _.acceleration.y, _.acceleration.z,
                (double) (_.timestamp));
    }
    void wti_::__wti(const struct ASensorEvent _) {
        fprintf(fg__, "%f,%f,%f,%f\n", _.vector.x, _.vector.y, _.vector.z,
                (double) (_.timestamp));
    }
    void wti_::___wti(const struct ASensorEvent _) {
        fprintf(fc_, "%f,%f,%f,%f\n", _.magnetic.x, _.magnetic.y, _.magnetic.z,
                (double) (_.timestamp));
    }

} // namespace wti


#ifdef __cplusplus
}
#endif
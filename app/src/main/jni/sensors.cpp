/*
 * UTC Virtual Athletic Trainer v0.000
 * rg 9.7.15
 * TODO: STORE ALL TESTS PERMANENTLY ON DEVICE
 * TODO: CREATE DEVICE UUID AND STORE AT TOP OF HISTORY.DAT FILE
 * TODO: STORE TOTAL COUNT
 * TODO: NUMBER, DATE, TEST ID, UUID --> GOES IN HISTORY.DAT FILE
 *
 * TODO: THE STORED TESTS WILL BE MARKED AS UPLOADED OR NOT UPLOADED
 * TODO: THE STORED TESTS CAN BE VIEWED ON THE PHONE AND UPLOADED IF NOT ALREADY
 *
 * TODO: WRITE UUID TO DATABASE SO UUIDs CAN BE USED TO IDENTIFY THE DEVICE
 *
 * TODO: ASSIGN UUID TO NAMES, HAVE PEOPLE REGISTER
 */


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


/*
 * SENSORS HANDLER
 */
namespace sh {

    /*
     * 1) CONSTRUCTOR
     * 2) VOID POINTER FOR SENSOR QUEUE TO USE
     * 3) DESTRUCTOR
     *
     * THE BOOL sOn ISN'T BEING USED, _w_ IS
     * I THINK THE STATIC FACTORY METHODS BYPASS THE CONSTRUCTOR
     */
    sh_::sh_() : sOn(true) { wti::wti_::wti__()._w_ = false; }

    void *_ = malloc(10000);

    sh_::~sh_() {
        wti::wti_::wti__()._fclose();
        sOn = false;
    };

    /*
     * THIS IS THE SENSOR 'CALLBACK' LOOP EQUIVALENT
     * TODO: RUN FUNCTION ON SEPARATE THREAD
     */
    //static int _o(int fd, int _e, void *_); TODO: TRY C++11 COMPILE AGAIN
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

    /*
     * THIS FUNCTION SETS UP SENSORS
     * FIRST 3 VARS ARE COUNTERS FOR INITIALLY CHECKING THE NUMBER OF VALUES WRITTEN TO EACH SENSOR
     * THEY'RE UNECESSARY
     */
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

    /*
     * THIS ENABLES THE SENSORS AND SETS THE SAMPLING RATE
     */
    void sh_::_o__() {
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().aIn);
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().gIn);
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().cIn);
        st_ = true;
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().gIn, sh_::sh__().SAMPLING_RATE);
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().cIn, sh_::sh__().SAMPLING_RATE);
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().aIn, sh_::sh__().SAMPLING_RATE);
    }

    /*
     * THIS DISABLES THE SENSORS
     * SENSORS DRAIN BATTERY, SO, IF THE APP REMAINS ON IN THE BACKGROUND
     * SENSORS NEED TO BE DISABLED.  IT'S BEST TO JUST CUT THEM ON FOR THE TEST THEN OFF AGAIN.
     */
    void sh_::_o___() {
        ASensorEventQueue_disableSensor(sh_::sh__().sEq, sh_::sh__().cIn);
        ASensorEventQueue_disableSensor(sh_::sh__().sEq, sh_::sh__().gIn);
        ASensorEventQueue_disableSensor(sh_::sh__().sEq, sh_::sh__().aIn);
        st_ = false;
        LOGI("NO VALUES WRITTEN FOR -> a: %d  g: %d  c: %d \n",_0_,_1__,_2___);
    }

    /*
     * THIS IS HERE FOR CHECKING THE SENSORS' STATE, i.e. ON/OFF, FROM JAVA
     * IF THE CODE IS WRITTEN CORRECTLY IT CAN BE REMOVED
     */
    bool sh_::_st() {
        if (st_)
            return true;
        else
            return false;
    }

} // NAMESPACE sh

/*
 * WRITE TO INTERNAL MEMORY
 */
namespace wti {

    /*
     * DEPRECATED
     */
    void wti_::_path(const char *__p) {
        ___p = __p;
    }

    /*
     * CHECKS IF FILES ARE OPEN TO WRITE OR CLOSED
     * RETURNS TRUE IF ALL FILES ARE OPEN
     * RETURNS FALSE IF ANY OR ALL FILES ARE CLOSED AND CLOSES OPEN FILES
     * IF ANY OTHERS ARE ALREADY CLOSED
     */
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

    /*
     * CLOSES ALL FILES AND SETS TO NULL FOR REALLOCATING AND REUSING
     */
    void wti_::_fclose() {
        fclose(fg__);
        fg__ = NULL;
        fclose(fa___);
        fa___ = NULL;
        fclose(fc_);
        fc_ = NULL;
    }

    /*
     * CONSTRUCTOR/DESTRUCTOR
     * BOOL UNUSED
     */
    wti_::wti_() : _wti_(true) { }
    wti_::~wti_() {
        _wti_ = false;
    };

    /*
     * OPENS FILES TO WRITE
     * PATHS ARE HARDCODED
     * STRING BUILDING DEPRECATED
     */
    void wti_::_fopen() {
        //const char *_p = ___p;
        //const char *a_ = "a.dat";
        //char _f[strlen(_p) + strlen(a_) + 1];
        //snprintf(_f, sizeof(_f), "%s/%s", _p, a_);
        fa___ = fopen("/data/data/edu.utc.vat/files/a.dat", "w");
        //const char *c_ = "c.dat";
        //char ___f[strlen(_p) + strlen(c_) + 1];
        //snprintf(___f, sizeof(___f), "%s%s", _p, c_);
        fc_ = fopen("/data/data/edu.utc.vat/files/c.dat", "w");
        //const char *g_ = "g.dat";
        //char __f[strlen(_p) + strlen(g_) + 1];
        //snprintf(__f, sizeof(__f), "%s%s", _p, g_);
        fg__ = fopen("/data/data/edu.utc.vat/files/g.dat", "w");
    }

    /*
     * SETS FLAG FOR ENABLING WRITING
     */
    void wti_::_w() {
        _w_ = true;
    }

    /*
     * SETS FLAG FOR DISABLING WRITING
     */
    void wti_::w_() {
        _w_ = false;
    }

    /*
     * WRITE FILES TO INTERNAL
     */
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

} // NAMESPACE wti


#ifdef __cplusplus
}
#endif
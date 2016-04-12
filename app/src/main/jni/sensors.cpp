/*
 * UTC Virtual Athletic Trainer
 * v0.01.1 (12/3/15)
 * rg 9.7.15
 * TODO: DATE IS IN TIMESTAMP .. ?? -- IS TEST ID STORED .. ??
 * TODO: THE STORED TESTS WILL BE MARKED AS UPLOADED OR NOT UPLOADED .. ??
 * TODO: THE STORED TESTS CAN BE VIEWED ON THE PHONE AND UPLOADED IF NOT ALREADY .. ??
 *
 * TODO: 'const char *ASensor_getVendor(ASensor const *sensor)' i.e. UPLOAD SENSOR VENDORS
 */


#include <cassert>

#include <stdio.h>
#include <stdlib.h>

#include <android/sensor.h>
#include <android/looper.h>

#include "sensors.h"
#include "f.h"
#include "comm.h"

#define LOG_TAG "sh"
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO,LOG_TAG, __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__))

#ifdef __cplusplus
extern "C" {
#endif


/*
 * SENSORS HANDLER
 */
namespace o {
    const float ALPHA = 0.1f; //ALPHA FOR FILTERING

    struct _ad {
        double x;
        double y;
        double z;
    };

    _ad _f;
}

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
     * TODO: TRY AGAIN ON SEPARATE THREAD -- EVENTUALLY (IS IT ON SEPARATE THREAD BY DEFAULT .. ??)
     * TODO: ALT CALLBACK FOR FLANKER OR ADD FLANKER FUNCTIONALITY WITH A FLANKER FLAG
     */
    static int _o(int fd, int _e, void *_) {
        ASensorEvent __e;
        float a = o::ALPHA;
        while (ASensorEventQueue_getEvents(sh_::sh__().sEq, &__e, 1) > 0) {
            if (wti::wti_::wti__()._w_) {
                if (__e.type == ASENSOR_TYPE_ACCELEROMETER) {
                    wti::wti_::wti__()._wti(__e); //TODO: NEEDS TO TAKE STRUCT w/ FILTERED VALS, ALSO
                    sh_::sh__()._0_++;
                }
                else if (__e.type == ASENSOR_TYPE_GYROSCOPE) {
                    if (wti::wti_::wti__()._f_) {
                        //THIS WRITES THE NEXT SENSOR TIMESTAMP AFTER THE FLANKER STIMULI CHANGES
                        //RESETS TIMESTAMP FLAG TO FALSE, WAITS FOR NEXT STIMULI CHANGE
                        if (sh_::sh__()._ts == true) {
                            sh_::sh__()._ts = false;
                            f::f_::f__()._ts((double)(__e.timestamp));
                        }
                        //IF RIGHT STIMULI AND NO RECORDED RESPONSE:
                        if (sh_::sh__()._r == true) {
                            if (__e.vector.x > sh_::sh__().FT) {
                                f::f_::f__().___dt[f::f_::f__().__sc-1] = (double)(__e.timestamp)-f::f_::f__().__it;
                                f::f_::f__().___r[f::f_::f__().__sc-1] = 1;
                                sh_::sh__()._r = false;
                                io::io_::io__()._c();
                            } else if (__e.vector.x < -sh_::sh__().FT) {
                                f::f_::f__().___dt[f::f_::f__().__sc-1] = (double)(__e.timestamp)-f::f_::f__().__it;
                                f::f_::f__().___r[f::f_::f__().__sc-1] = -1;
                                sh_::sh__()._r = false;
                                io::io_::io__()._c();
                            }
                        }
                        //IF LEFT STIMULI AND NO RECORDED RESPONSE:
                        if (sh_::sh__()._l == true) {
                            if (__e.vector.x < -sh_::sh__().FT) {
                                f::f_::f__().___dt[f::f_::f__().__sc-1] = (double)(__e.timestamp)-f::f_::f__().__it;
                                f::f_::f__().___r[f::f_::f__().__sc-1] = 1;
                                io::io_::io__()._c();
                                sh_::sh__()._l = false;
                            } else if (__e.vector.x > sh_::sh__().FT) {
                                f::f_::f__().___dt[f::f_::f__().__sc-1] = (double)(__e.timestamp)-f::f_::f__().__it;
                                f::f_::f__().___r[f::f_::f__().__sc-1] = -1;
                                sh_::sh__()._l = false;
                                io::io_::io__()._c();
                            }
                        }
                    }
                    wti::wti_::wti__().__wti(__e);
                    sh_::sh__()._1__++;
                }
                else if (__e.type == ASENSOR_TYPE_MAGNETIC_FIELD) {
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
        wti::wti_::wti__()._fopen();

        sh_::sh__().lpr = ALooper_forThread();

        if (sh_::sh__().lpr == NULL)
            sh_::sh__().lpr = ALooper_prepare(ALOOPER_PREPARE_ALLOW_NON_CALLBACKS);
        assert(sh_::sh__().lpr != NULL); //TODO: DEBUG CRASH WHEN ATTEMPTING 2nd TEST -- BUG .. ??

        sh_::sh__().sMg = ASensorManager_getInstance();
        assert(sh_::sh__().sMg != NULL);

        sh_::sh__().aIn = ASensorManager_getDefaultSensor(
                sh_::sh__().sMg, ASENSOR_TYPE_ACCELEROMETER);
        assert(sh_::sh__().aIn != NULL);
        sh_::sh__().gIn = ASensorManager_getDefaultSensor(
                sh_::sh__().sMg, ASENSOR_TYPE_GYROSCOPE);
        assert(sh_::sh__().gIn != NULL);
        sh_::sh__().cIn = ASensorManager_getDefaultSensor(
                sh_::sh__().sMg, ASENSOR_TYPE_MAGNETIC_FIELD);
        assert(sh_::sh__().cIn != NULL);

        sh_::sh__().sEq = ASensorManager_createEventQueue(
                sh_::sh__().sMg, sh_::sh__().lpr, 3, _o, _);
        assert(sh_::sh__().sEq != NULL);
        sh_::sh__()._l = false;
        sh_::sh__()._r = false;
        sh_::sh__()._ts = false;
        wti::wti_::wti__()._f_ = false;
    }

    /*
     * THIS ENABLES THE SENSORS AND SETS THE SAMPLING RATE
     */
    void sh_::_o__() {
        _0_ = 0;
        _1__ = 0;
        _2___ = 0;
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().aIn);
        assert(sh_::sh__().sEq >= 0);
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().gIn);
        assert(sh_::sh__().sEq >= 0);
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().cIn);
        assert(sh_::sh__().sEq >= 0);
        st_ = true;
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().gIn, sh_::sh__().SAMPLING_RATE);
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().cIn, sh_::sh__().SAMPLING_RATE);
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().aIn, sh_::sh__().SAMPLING_RATE);
        o::_f.x = 0.f;
        o::_f.y = 0.f;
        o::_f.z = 0.f;
    }
    void sh_::_o__(bool _ff_) {
        wti::wti_::wti__()._f_ = _ff_; //_ff_ should always be true when passed to _o__()
        _0_ = 0;
        _1__ = 0;
        _2___ = 0;
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().aIn);
        assert(sh_::sh__().sEq >= 0);
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().gIn);
        assert(sh_::sh__().sEq >= 0);
        ASensorEventQueue_enableSensor(sh_::sh__().sEq, sh_::sh__().cIn);
        assert(sh_::sh__().sEq >= 0);
        st_ = true;
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().gIn, sh_::sh__().SAMPLING_RATE);
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().cIn, sh_::sh__().SAMPLING_RATE);
        ASensorEventQueue_setEventRate(sh_::sh__().sEq, sh_::sh__().aIn, sh_::sh__().SAMPLING_RATE);
        o::_f.x = 0.f;
        o::_f.y = 0.f;
        o::_f.z = 0.f;
    }

    /*
     * THIS DISABLES THE SENSORS
     * SENSORS DRAIN BATTERY, SO, IF THE APP REMAINS ON IN THE BACKGROUND
     * SENSORS NEED TO BE DISABLED.  IT'S BEST TO JUST CUT THEM ON FOR THE TEST THEN OFF AGAIN.
     */
    void sh_::_o___() {
        //TODO: CHECK IF NEED TO RE-INIT SENSORS AFTER DISABLE
        ASensorEventQueue_disableSensor(sh_::sh__().sEq, sh_::sh__().cIn);
        ASensorEventQueue_disableSensor(sh_::sh__().sEq, sh_::sh__().gIn);
        ASensorEventQueue_disableSensor(sh_::sh__().sEq, sh_::sh__().aIn);
        st_ = false;
        LOGI("NUMBER OF VALUES WRITTEN FOR -> a: %d  g: %d  c: %d \n",_0_,_1__,_2___);
    }

    /**
     * THIS SETS FLANKER FLAGS FOR ACTIVE EVALUATION
     */
    void sh_::_sff_(bool _sf) {
        wti::wti_::wti__()._f_ = _sf;
    }

    /*
     * THIS IS HERE FOR CHECKING THE SENSORS' STATE, i.e. ON/OFF, FROM JAVA
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
     * TODO: FIX TO NOT NULLIFY FILES IF CLOSED
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
        //OPEN ACCELEROMETER
        try {
            fa___ = fopen("/data/data/edu.utc.vat/files/a.dat", "w");
        } catch(...) {
            LOGI("FLIE PATH ERROR OPENING FILE a.dat");
        }
        fprintf(fa___,"accelx,accely,accelz,acceltimestamp\n");
        //OPEN COMPASS
        try {
            fc_ = fopen("/data/data/edu.utc.vat/files/c.dat", "w");
        } catch(...) {
            LOGI("FLIE PATH ERROR OPENING c.dat");
        }
        fprintf(fc_,"magx,magy,magz,magtimestamp\n");
        //OPEN GYROSCOPE
        try {
            fg__ = fopen("/data/data/edu.utc.vat/files/g.dat", "w");
        } catch(...){
            LOGI("FILE PATH ERROR OPENING g.dat");
        }
        fprintf(fg__,"gyrox,gyroy,gyroz,gyrotimestamp\n");
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
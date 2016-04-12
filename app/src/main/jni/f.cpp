//
// Created by Ross Gruetzemacher on 12/15/15.
//

#include "f.h"
#include "sensors.h"

#include <fstream>

#define LOG_TAG "f"
#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO,LOG_TAG, __VA_ARGS__))
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__))

#ifdef __cplusplus
extern "C" {
#endif

namespace f {

    f_::f_() : o(true) {}
    f_::~f_() {
        o = false;
    };

    bool f_::__r__() {

        if(f_::__sc != -1)
            f_::__w__();
        return wti::wti_::wti__()._f_;
    }

    /**
     * __i__() --> INITIALIZE FLANKER VARIABLES
     */
    void f_::__i__() {
        f_::__o = 0;
        f_::___o = 0;
        f_::__f = false;
        f_::__sc = -1;
        f_::___s = new int[18];
        f_::___dt = new double[18];
        f_::___r = new int[18];
        f_::__it = -1.0;
        for(int i=0;i<17;i++) {
            f_::___dt[i] = 0.0;
            f_::___r[i] = 0;
        }
        f_::__fo = true; //TODO: fix this .. (still necessary -- ??)
    }

    void f_::__w__() {
        LOGI("FLANKER -- WRITING TO f.dat");
        std::ofstream ff ("/data/data/edu.utc.vat/files/f.dat",std::ofstream::out);
        ff << "stimulus,response,responseTime\n";
        for (int i = 0; i < 16; i++) {
            ff << ___s[i] << "," << ___r[i] << "," << ___dt[i] << "\n";
            LOGI("%d %d %f\n", f_::___s[i], f_::___r[i], f_::___dt[i]); }
        ff.close();
        f_::__c__();
        f_::__i__();
    }

    void f_::__c__() {
        LOGI("FLANKER; CLOSING C++ SIDE\n");
        delete f_::___dt;
        delete f_::___s;
        delete f_::___r;
        f_::__fo = false;
    }

    void f_::_____(double _, int _0) {
        if (f_::__f == true) {
            f_::__s[f_::___o++] = _;
        } else if (f_::__f == false) {
            f_::__[f_::__o] = _0;
            f_::__t[f_::___o++] = _;
        }
    }

    void f_::_ts(double ts) {
        f_::__it = ts;
    }

} //namespace f

#ifdef __cplusplus
}
#endif
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

    /*void f_::__s__(double _) {
        if (f_::__f == false) {
            f_::__f = true;
        } else if (f_::__f == true) {
            f_::__f = false;
        }
        f_::_____(_,-1);
    }*/

    /*void f_::__f__(double _, int _0) {
        f_::__o++;
        f_::__f = false;
        f_::_____(_,_0);
    }*/

    bool f_::__r__() {

        if(f_::__sc != -1)
            f_::__w__();
        /*if (f_::__o != 0 && f_::___o != 0) {
            LOGI("FLANKER -- returning true");

            return true;
        } else if (f_::__o == 0 && f_::___o == 0) {
            LOGI("FLANKER -- NOT WRITING TO f.dat");
            return false;
        }
        LOGI("FLANKER native error @ __r__()");
        return false;*/
        return wti::wti_::wti__()._f_;
    }

    /**
     * __i__() --> INITIALIZE FLANKER VARIABLES
     */
    void f_::__i__() {
        f_::__o = 0;
        f_::___o = 0;
        f_::__f = false;
        //f_::__t = new double[600]; //TODO: ONLY SHOULD NEED 60
        //f_::__s = new double[600];
        //f_::__ = new int[600];
        f_::__sc = -1;
        f_::___s = new int[18];
        f_::___dt = new double[18];
        f_::___r = new int[18];
        f_::__it = -1.0;
        for(int i=0;i<17;i++) {
            f_::___dt[i] = 0.0;
            f_::___r[i] = 0;
        }
        f_::__fo = true; //TODO: fix this ..
    }

    void f_::__w__() {
        LOGI("FLANKER -- WRITING TO f.dat");
        //try {
        std::ofstream ff ("/data/data/edu.utc.vat/files/f.dat",std::ofstream::out);
            //f_::f = fopen("/data/data/edu.utc.vat/files/f.dat", "w");
        ff << "stimulus,response,responseTime\n";
            //fprintf(f_::f,"stimulus,response,responseTime\n");
        /*} catch(...) {
            LOGI("FLIE PATH ERROR OPENING FILE f.dat\n");
            return;
        }*/
        //if(f_::__s == f_::__t) {
            /*
            for (int i = 0; i < f_::___o; i++) {
                if (!f_::__s[i]) {
                    f_::__s[i] = 0.;
                }
            }*/
            //write to file
        for (int i = 0; i < 16; i++) {
            //fprintf(f_::f,"%d,%d,%f\n",f_::___s[i],f_::___r[i],f_::___dt[i]);
            ff << ___s[i] << "," << ___r[i] << "," << ___dt[i] << "\n";
            LOGI("%d %d %f\n", f_::___s[i], f_::___r[i], f_::___dt[i]); }
        //}
        //else log error
        //fclose(f_::f);
        ff.close();
        f_::__c__();
        f_::__i__();
    }

    void f_::__c__() {
        LOGI("FLANKER; CLOSING C++ SIDE\n");
        delete f_::___dt;
        delete f_::___s;
        delete f_::___r;
        //delete f_::__t; //TODO: Manage memory
        //delete f_::__s;
        //delete f_::__;
        f_::__fo = false;
    }

    void f_::_____(double _, int _0) {
        if (f_::__f == true) {
            f_::__s[f_::___o++] = _;
        } else if (f_::__f == false) {
            f_::__[f_::__o] = _0;
            f_::__t[f_::___o++] = _; //Switched from __o++ to ___o++
        }
    }

    void f_::_ts(double ts) {
        f_::__it = ts;
    }

} //namespace f

#ifdef __cplusplus
}
#endif
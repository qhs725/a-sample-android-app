//
// Created by Ross Gruetzemacher on 12/15/15.
//

#include "f.h"


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

    void f_::__s__(double _) {
        if (f_::__f == false) {
            f_::__f = true;
        } else if (f_::__f == true) {
            f_::__f = false;
        }
        f_::_____(_,-1);
    }

    void f_::__f__(double _, int _0) {
        f_::__o++;
        f_::__f = false;
        f_::_____(_,_0);
    }

    bool f_::__r__() {
        if (f_::__o != 0 && f_::___o != 0) {
            __w__();
            return true;
        } else if (f_::__o == 0 && f_::___o == 0) {
            return false;
        }
        LOGI("FLANKER native error @ __r__()");
        return false;
    }

    /**
     * __i__() --> INITIALIZE FLANKER VARIABLES
     */
    void f_::__i__() {
        f_::__o = 0;
        f_::___o = 0;
        f_::__f = false;
        f_::__t = new double[600]; //TODO: ONLY SHOULD NEED 60
        f_::__s = new double[600];
        f_::__ = new int[600];
    }

    void f_::__w__() {
        try {
            f_::f = fopen("/data/data/edu.utc.vat/files/f.dat", "w");
            fprintf(f_::f,"stimulusT,sensorT");
        } catch(...) {
            LOGI("FLIE PATH ERROR OPENING FILE f.dat\n");
        }
        if(f_::__s == f_::__t) {
            for (int i = 0; i < f_::___o; i++) {
                if (!f_::__s[i]) {
                    f_::__s[i] = 0.;
                }
            }
            //write to file
            for (int i = 0; i < f_::__o; i++) {
                fprintf(f_::f,"%f,%f",f_::__t[i],f_::__s[i]);
            }
        }
        //else log error
        fclose(f_::f);
        f_::__c__();
        f_::__i__();
    }

    void f_::__c__() {
        LOGI("FLANKER; CLOSING C++ SIDE\n");
        //delete f_::__t; //TODO: Manage memory
        //delete f_::__s;
        //delete f_::__;
    }

    void f_::_____(double _, int _0) {
        if (f_::__f == true) {
            f_::__s[f_::___o++] = _;
        } else if (f_::__f == false) {
            f_::__[f_::__o] = _0;
            f_::__t[f_::___o++] = _; //Switched from __o++ to ___o++
        }
    }

} //namespace f

#ifdef __cplusplus
}
#endif
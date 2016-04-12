/*
 * UTC Virtual Athletic Trainer
 * v0.01.1b (12/?/15)
 * rg 9.7.15
 */

#include "comm.h"
#include "jni.h"

#define LOG_TAG "io"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


#ifdef __cplusplus
extern "C" {
#endif



/**
 * This is inappropriately the IO class.  It's initial purposes have been deprecated,
 * currently it's just being used to handle some flags.
 * TODO: Move all flags and vars being handled/passed from Java into this class
 */
namespace io {

    io_::io_() : o(true) {}
    io_::~io_() {
        o = false;
    };

    void io_::id_(const char *_) {
        io_::io__().__id = _;
    }

    void io_::_c() {
        LOGI("SOUND NOW!!");
        io_::_ch = true;
    }

    bool io_::__ck__() {
        if (io_::_ch == true) {
            io_::_ch = false;
            return true;
        } else {
            return false;
        }
    }

}  //  namespace io



#ifdef __cplusplus
}
#endif

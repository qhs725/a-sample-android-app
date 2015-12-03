/*
 * UTC Virtual Athletic Trainer
 * v0.01.1 (12/3/15)
 * rg 9.7.15
 */

#include "comm.h"

#define LOG_TAG "io"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


#ifdef __cplusplus
extern "C" {
#endif


namespace io {

    io_::io_() : o(true) {
        io_::io__().__ff__ = false;
    }
    io_::~io_() {
        o = false;
    };

    void io_::id_(const char *_) {
        io_::io__().__id = _;
    }

    bool io_::__fc__() {
        if (io_::io__().__ff__) {
            io_::io__().__ff__ = false;
            return true;
        } else
            return false;
    }

    void io_::__fo__() {
        io_::io__().__ff__ = true;
    }

}  //  namespace io


#ifdef __cplusplus
}
#endif

//
// Created by Ross Gruetzemacher on 9/16/15.
//

#include "Poco/Exception.h"

#include "sk.h"

#include <android/log.h>

#define LOG_TAG "sk"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


#ifdef __cplusplus
extern "C" {
#endif


namespace sk {

    sk_::sk_() : o(true) {}
    sk_::~sk_() {
        o = false;
    };

    bool sk_::__i__() {
        LOGI("CONNECTING...");
        Poco::Net::SocketStream s__(s);
        Poco::Net::SocketAddress s_("192.168.0.100", 8080);
        s.connect(s_);
        LOGI("CONNECTED");
        return true;
    }

    bool sk_::__sb(const char *__b, int _b) {
        int _t;
        while (_b > _t) {
            _t += s.sendBytes(__b + _t, _b - _t);
        }
        return true;
    }

    void sk_::__d__() {
        s.shutdown();
    }

} // namespace sk


#ifdef __cplusplus
}
#endif
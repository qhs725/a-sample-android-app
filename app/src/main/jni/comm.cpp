/*
 * UTC Virtual Athletic Trainer v0.000
 * rg 9.7.15
 * TODO: PASS DATA COUNT LINE TO DB TO ENSURE COMPLETE DATA SEND
 */


#include "Poco/Net/MessageHeader.h"
#include "Poco/Net/HTTPClientSession.h"
#include "Poco/Net/HTTPRequest.h"
#include "Poco/Net/HTTPMessage.h"
#include "Poco/Net/HTTPResponse.h"
#include "Poco/Net/NameValueCollection.h"
#include "Poco/Net/MediaType.h"

#include "comm.h"
#include "packdat.h"

#include <pthread.h>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>

#define LOG_TAG "io"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


//TODO: DEPRECATED?
namespace p {
    template < typename T > std::string to_string( const T& n ) {
        std::ostringstream _0x;
        _0x << n;
        return _0x.str();
    }
} // namespace p


#ifdef __cplusplus
extern "C" {
#endif


namespace io {

    typedef struct{
        std::string _s;
    }_;

    pthread_t ___t[5];
    pthread_mutex_t ___l;

    io_::io_() : o(true) {}
    io_::~io_() {
        o = false;
    };

    void io_::__init() {
        io_::io__().pss.setHost("192.168.0.100");
        io_::io__().pss.setPort(8080);
        io_::io__().pss.setKeepAlive(true);
        io_::io__().prq.setMethod(Poco::Net::HTTPRequest::HTTP_POST);
        io_::io__().prq.setURI("/dat");
        io_::io__().prq.setKeepAlive(true);

        std::string e_ = prq.getTransferEncoding();
        char *e__ = new char[e_.length()+1];
        std::strcpy(e__,e_.c_str());
        LOGI("ENCODING %s", e__);

        io_::io__().prsp.setKeepAlive(true);
        io_::io__()._f_ = true;
    }

    void io_::P_() {
        if (pthread_mutex_init(&___l, NULL) != 0)
            LOGE("PTHREAD MUTEX ERROR");
        for (int i = 0; i < pd::pd_::pd__()._c_; i+=1) {
            const char *_s_ = pd::pd_::pd__().__b[i];
            std::string _s0(_s_);
            _ v0;
            v0._s = _s0;
            pthread_create(&(___t[0]), NULL, io_::io__()._0, &v0);/*
            const char *_s__ = pd::pd_::pd__().__b[i+1];
            std::string _s1(_s__);
            _ v1;
            v1._s = _s1;
            pthread_create(&(___t[1]), NULL, io_::io__()._0, &v1);
            const char *_s___ = pd::pd_::pd__().__b[i+2];
            std::string _s2(_s___);
            _ v2;
            v2._s = _s2;
            pthread_create(&(___t[2]), NULL, io_::io__()._0, &v2);
            const char *_s____ = pd::pd_::pd__().__b[i+3];
            std::string _s3(_s____);
            _ v3;
            v3._s = _s3;
            pthread_create(&(___t[3]), NULL, io_::io__()._0, &v3);
            const char *_s_____ = pd::pd_::pd__().__b[i+4];
            std::string _s4(_s_____);
            _ v4;
            v4._s = _s4;
            pthread_create(&(___t[4]), NULL, io_::io__()._0, &v4);*/
            pthread_join(___t[0], NULL);
            //pthread_join(___t[1], NULL);
            //pthread_join(___t[2], NULL);
            //pthread_join(___t[3], NULL);
            //pthread_join(___t[4], NULL);
        }
        pthread_mutex_destroy(&___l);
    }

    void *io_::_0(void *__A) {

        pthread_mutex_lock(&___l);

        std::string _s_ = ((_ *)__A)->_s;

        std::string message = _s_;
        io_::io__().prq.setContentLength(message.length());

        try {
            std::ostream& _os = io_::io__().pss.sendRequest(io_::io__().prq);
            _os << message << std::endl;
            io_::io__().prq.write(std::cout);
        } catch (const Poco::Exception& e) {
            char *err1 = new char[e.displayText().length()+1];
            std::strcpy(err1,e.displayText().c_str());
            LOGI("postAccel no3b ERROR1: %s", err1);
            std::cout << e.displayText() << std::endl;
        } catch (const std::exception& e) {
            LOGI("postAccel no3b ERROR2");
            std::cerr << e.what() << std::endl;
        }

        io_::io__().pss.receiveResponse(io_::io__().prsp);

        pthread_mutex_unlock(&___l);
    }

}  //  namespace io


#ifdef __cplusplus
}
#endif
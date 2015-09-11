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

#include "comm.h"
#include "packdat.h"

#include <iostream>
#include <sstream>
#include <string>

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

    io_::io_() : o(true) {}
    io_::~io_() {
        o = false;
    };

    void io_::__init() {
        pss.setHost("192.168.0.100");
        pss.setPort(8080);
        pss.setKeepAlive(true);
        prq.setMethod(Poco::Net::HTTPRequest::HTTP_POST);
        prq.setURI("/dat");
        prq.setKeepAlive(true);

        std::string e_ = prq.getTransferEncoding();
        char *e__ = new char[e_.length()+1];
        std::strcpy(e__,e_.c_str());
        LOGI("ENCODING %s", e__);

        prsp.setKeepAlive(true);
    }

    void io_::P_() {
        for (int i = 0; i < pd::pd_::pd__()._c_; i++) {
            const char *_s = pd::pd_::pd__().__b[i];
            std::string _s_(_s);
            io_::io__()._0(_s_);
        }
    }

    void io_::_0(std::string _s_) {

        /*
        Poco::Net::MessageHeader nameValuePair;
        std::string aX = p::to_string(__e.acceleration.x);
        nameValuePair.add("ax",aX);
        std::string aY = p::to_string(__e.acceleration.y);
        nameValuePair.add("ay",aY);
        std::string aZ = p::to_string(__e.acceleration.z);
        nameValuePair.add("az",aZ);
        std::string ts = p::to_string(__e.timestamp);
        nameValuePair.add("ts",ts);
        nameValuePair.add("sinfo","test001");*/

        //std::string test = "ts=this is a test&session_info=this is also a test";
        std::string test = _s_;
        prq.setContentLength(test.length());

        try {
            std::ostream& _os = pss.sendRequest(prq);
            _os << test << std::endl;
            prq.write(std::cout);
        } catch (const Poco::Exception& e) {
            char *err1 = new char[e.displayText().length()+1];
            std::strcpy(err1,e.displayText().c_str());
            LOGI("postAccel no3b ERROR1: %s", err1);
            std::cout << e.displayText() << std::endl;
        } catch (const std::exception& e) {
            LOGI("postAccel no3b ERROR2");
            std::cerr << e.what() << std::endl;
        }

        pss.receiveResponse(prsp);

    }

}  //  namespace io


#ifdef __cplusplus
}
#endif
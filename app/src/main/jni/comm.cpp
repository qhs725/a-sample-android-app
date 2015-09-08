//
// UTC Virtual Athletic Trainer v0.000
// Created by rg on 9/7/15.
//

#include "Poco/Net/MessageHeader.h"
#include "Poco/Net/HTTPClientSession.h"
#include "Poco/Net/HTTPRequest.h"
#include "Poco/Net/HTTPMessage.h"
#include "Poco/Net/HTTPResponse.h"
#include "Poco/Net/NameValueCollection.h"

#include "comm.h"

#include <iostream>
#include <sstream>
#include <string>

#define LOG_TAG "io"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


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
        prq.setURI("/ta2");
        prq.setKeepAlive(true);

        prsp.setKeepAlive(true);
    }

    void io_::_0(const struct ASensorEvent __e) {

        Poco::Net::MessageHeader nameValuePair;
        std::string aX = p::to_string(__e.acceleration.x);
        nameValuePair.add("accx",aX);
        std::string aY = p::to_string(__e.acceleration.y);
        nameValuePair.add("accy",aY);
        std::string aZ = p::to_string(__e.acceleration.z);
        nameValuePair.add("accz",aZ);
        std::string ts = p::to_string(__e.timestamp);
        nameValuePair.add("ts",ts);
        nameValuePair.add("session_info","test001");

        std::string test = "ts=this is a test&session_info=this is also a test";
        prq.setContentLength(test.length());

        try {
            std::ostream& _os = pss.sendRequest(prq); // sends request, returns open stream
            //LOGI("postAccel GOOD");
            //nameValuePair.write(_os);
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

        //std::istream& _is = pss.receiveResponse(prsp);

        try {
            //Poco::Net::HTTPResponse postResponse;
            std::istream& _is = pss.receiveResponse(prsp);
            std::cerr << _is.rdbuf();
            //char *stm = new char[_is.rdbuf().length()+1];
            //LOGI("postAccel response %s", _is.rdbuf());
        } catch (const Poco::Exception& e) {
            char *err1 = new char[e.displayText().length()+1];
            std::strcpy(err1,e.displayText().c_str());
            LOGI("postAccel no4b ERROR1: %s", err1);
            std::cout << e.displayText() << std::endl;
        } catch (const std::exception& e) {
            LOGI("postAccel no4b ERROR2");
            std::cerr << e.what() << std::endl;
        }

    }

    void io_::_1(const struct ASensorEvent __e) {
        //TODO: add gyroscope event io
    }

    void io_::_2(const struct ASensorEvent __e) {
        //TODO: add compass event io
    }

}  //  namespace io


#ifdef __cplusplus
}
#endif
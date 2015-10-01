//
// UTC Virtual Athletic Trainer v0.000
// Created by rg on 9/7/15.
//


#include "Poco/Net/HTTPClientSession.h"
#include "Poco/Net/HTTPRequest.h"
#include "Poco/Net/HTTPResponse.h"

#include "Poco/Net/StreamSocket.h"
#include "Poco/Net/SocketStream.h"
#include "Poco/Net/SocketAddress.h"
#include "Poco/Exception.h"

#include <android/sensor.h>
#include <android/log.h>

#ifndef UTC_VIRTUAL_ATHLETIC_TRAINER_COMM_H
#define UTC_VIRTUAL_ATHLETIC_TRAINER_COMM_H
#ifdef __cplusplus
extern "C" {
#endif


namespace io {

    class io_ {
    public:
        static io_ &io__() {
            static io_ instance;
            return instance;
        }

        io_();
        ~io_();

        void __init();

        void P_();

        bool _f_;

        void __s__();

    private:
        bool o;

        Poco::Net::HTTPClientSession pss;
        Poco::Net::HTTPRequest prq;
        Poco::Net::HTTPResponse prsp;

        static void *_0(void *);

    };

}  // namespace io


#ifdef __cplusplus
}
#endif
#endif

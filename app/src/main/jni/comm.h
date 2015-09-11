//
// UTC Virtual Athletic Trainer v0.000
// Created by rg on 9/7/15.
//


#include "Poco/Net/HTTPClientSession.h"
#include "Poco/Net/HTTPRequest.h"
#include "Poco/Net/HTTPResponse.h"

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

        Poco::Net::HTTPClientSession pss;
        Poco::Net::HTTPRequest prq;
        Poco::Net::HTTPResponse prsp;

        io_();
        ~io_();

        void _0(std::string _s_);

        void __init();

        void P_();

    private:
        bool o;

    };

}  // namespace io


#ifdef __cplusplus
}
#endif
#endif

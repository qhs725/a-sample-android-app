/**
 * UTC Virtual Athletic Assistant v0.000
 * rg 9/16/15
 */

#include "Poco/Net/StreamSocket.h"
#include "Poco/Net/SocketStream.h"
#include "Poco/Net/SocketAddress.h"

#include <string>

#ifndef UTC_VIRTUAL_ATHLETIC_TRAINER_SK_H
#define UTC_VIRTUAL_ATHLETIC_TRAINER_SK_H


#ifdef __cplusplus
extern "C" {
#endif


/**
 * SOCKET HANDLER
 */
namespace sk {

    class sk_ {
    public:
        static sk_ &sk__() {
            static sk_ instance;
            return instance;
        }

        sk_();
        ~sk_();

        bool __i__();
        bool __sb(const char *__b, int _b);
        void __d__();

    private:
        bool o;

        Poco::Net::StreamSocket s;
        //Poco::Net::SocketAddress s_;

    };

} // namespace sk


#ifdef __cplusplus
}
#endif
#endif //UTC_VIRTUAL_ATHLETIC_TRAINER_SK_H

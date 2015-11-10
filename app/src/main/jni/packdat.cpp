/*
 * UTC Virtual Athletic Assistant v0.000
 * rg 9.10.15
 */


#include <pthread.h>
#include <ios>
#include <fstream>
#include <string>
#include <sstream>
#include "packdat.h"
#include "sensors.h"
#include "comm.h"

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


namespace pd {

    typedef struct {
        const char *_s;
    }_;

    pthread_t ___;

    pd_::pd_() : o(true) {}
    pd_::~pd_() { o = false; };

    bool pd_::__m__() {
        pd_::pd__()._pd = false;
        LOGI("PACKAGE in __m__");
        _ _v0;
        _v0._s = io::io_::io__().__id;
        pthread_create(&___, NULL, &pd_::pd__().rw_, &_v0);
        pthread_join(___, NULL);
        //io::io_::io__().P_(); //TODO: UNCOMMENT TO HTTP POST
        //io::io_::io__().__s__(); //TODO: SOCKETS...
        return true;
    }

    void *pd_::rw_(void *__A) {
        const char *_s = ((_ *)__A)->_s;
        LOGI("PACKAGING into first line .csv: %s", _s);
        int _c0 = sh::sh_::sh__()._0_;
        int _c1 = sh::sh_::sh__()._1__;
        int _c2 = sh::sh_::sh__()._2___;
        int _c = std::min(std::min(_c0, _c1), _c2);
        pd_::pd__().__b = new char*[_c]; //TODO: CLEAN THIS MEMORY
        std::string _l, _l0, _l1, _l2;
        //TODO: FIND ALTERNATIVE TO HARDCODING PATH
        std::ifstream __i0 ("/data/data/edu.utc.vat/files/a.dat", std::ifstream::in);
        std::ifstream __i1 ("/data/data/edu.utc.vat/files/g.dat", std::ifstream::in);
        std::ifstream __i2 ("/data/data/edu.utc.vat/files/c.dat", std::ifstream::in);
        std::ofstream __o ("/data/data/edu.utc.vat/files/data.csv", std::ofstream::out);
        __o << _s << "\n";
        int _cc = 0-1;
        while (_cc++ < _c) {
            LOGI("PACKAGE in while loop @ _cc = %d", _cc);
            std::getline(__i0, _l0);
            std::getline(__i1, _l1);
            std::getline(__i2, _l2);
            _l = p::to_string(_cc) + ',' + _l0 + ',' + _l1 + ',' + _l2;
            pd_::pd__().__b[_cc] = new char[_l.length()+1];
            strcpy(pd_::pd__().__b[_cc], _l.c_str());
            __o << _l << "\n";
        }
        __i0.close();
        __i1.close();
        __i2.close();
        __o.close();
        pd_::pd__()._c_ = _c;
        pd_::pd__()._pd = true;
    }

    bool pd_::pk_() {
        return pd_::pd__()._pd;
    }

} // namespace pd


#ifdef __cplusplus
}
#endif
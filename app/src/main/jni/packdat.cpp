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
        int _c;
        int _c0;
        int _c1;
        int _c2;
        char **__b;
    }_;

    pthread_t ___;

    pd_::pd_() : o(true) {}
    pd_::~pd_() { o = false; };

    void pd_::__m__() {
        LOGI("PACKAGE in __m__");
        pthread_create(&___, NULL, &pd_::pd__().rw_, NULL);
        void *____;
        pthread_join(___, NULL);// &____);
        /*
        _ ___v = *(_*)____;
        pd_::pd__().__b = new char[___v._c];
        for (int _c = 0; _c < ___v._c; _c++) {
            *pd_::pd__().__b[_c] = &___v.__b[_c];
        }*/
        io::io_::io__().P_();
    }


    void *pd_::rw_(void *__A) {
        int _c0 = sh::sh_::sh__()._0_;
        int _c1 = sh::sh_::sh__()._1__;
        int _c2 = sh::sh_::sh__()._2___;
        int _c = std::min(std::min(_c0, _c1), _c2);
        //char **__b;
        pd_::pd__().__b = new char*[_c];
        //_c = _c--;
        std::string _l, _l0, _l1, _l2;
        std::ifstream __i0 ("/data/data/edu.utc.vat/files/a.dat", std::ifstream::in);
        std::ifstream __i1 ("/data/data/edu.utc.vat/files/g.dat", std::ifstream::in);
        std::ifstream __i2 ("/data/data/edu.utc.vat/files/c.dat", std::ifstream::in);
        int _cc = 0-1;
        while (_cc++ < _c) {
            LOGI("PACKAGE in while loop @ _cc = %d", _cc);
            std::getline(__i0, _l0);
            std::getline(__i1, _l1);
            std::getline(__i2, _l2);
            _l = p::to_string(_cc) + ',' + _l0 + ',' + _l1 + ',' + _l2;
            pd_::pd__().__b[_cc] = new char[_l.length()+1];
            strcpy(pd_::pd__().__b[_cc], _l.c_str());
        }
        pd_::pd__()._c_ = _c;
        /*
        __i.getline(__i, _l);
        int _L = length(_l);
        _s = _L+12;
        if (__i.is_open()) {
        */

        //_ __;
        //__._c = _c;
        //__.__b = __b;

        //TODO: FIGURE OUT WHICH ONE OF THESE WORKS PROPERLY::
        //pthread_exit((void *) &__);
        //for (int i = 0; i < _c; i++) delete __b[i];
        //delete __b;
        //return (void *) __;
    }

} // namespace pd


#ifdef __cplusplus
}
#endif
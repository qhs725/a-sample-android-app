/*
 * UTC Virtual Athletic Assistant v0.000
 * rg 9.10.15
 */


#include <pthread.h>
#include <ios>
#include <fstream>
#include <string>
#include <sstream>
#include <vector>
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
        const char *_s2;
    }_;

    pthread_t ___;

    pd_::pd_() : o(true) {}
    pd_::~pd_() { o = false; };

    bool pd_::__m__(const char *x) {
        pd_::pd__()._pd = false;
        LOGI("PACKAGE in __m__");
        _ _v0;
        _v0._s = io::io_::io__().__id;
        std::string s;
        std::stringstream ss;
        ss << x;
        ss >> s;
        const char *c = s.c_str();
        _v0._s2 = c;
        pthread_create(&___, NULL, &pd_::pd__().rw_, &_v0);
        pthread_join(___, NULL);
        //io::io_::io__().P_(); //TODO: UNCOMMENT TO HTTP POST
        //io::io_::io__().__s__(); //TODO: SOCKETS...
        return true;
    }

    void *pd_::rw_(void *__A) {
        const char *_s = ((_ *)__A)->_s;
        const char *_s2 = ((_ *)__A)->_s2;
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
        //std::ofstream __o ("/data/data/edu.utc.vat/files/data.csv", std::ofstream::out);
        const char *e = ".csv";
        const char *p = "/data/data/edu.utc.vat/files/";
        char fs[strlen(p)+strlen(_s2)+strlen(e)+1];
        snprintf(fs,sizeof(fs),"%s%s%s",p,_s2,e);
        std::ofstream __o (fs,std::ofstream::out);
        LOGI("THIS IS FILE PATH: %s", fs);
        __o << _s << "\n"; //this writes the UUIDs and personal info
        int _cc = 0;
        _c++;
        while (_cc++ < _c) {
            LOGI("PACKAGE in while loop @ _cc = %d", _cc);
            std::getline(__i0, _l0);
            std::getline(__i1, _l1);
            std::getline(__i2, _l2);
            _l = _l0 + ',' + _l1 + ',' + _l2;
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
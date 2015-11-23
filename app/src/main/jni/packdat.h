/*
 * UTC Virtual Athletic Assistant v0.000
 * rg 9.10.15
 */

#include <string>

#ifndef UTC_VIRTUAL_ATHLETIC_TRAINER_PACKDAT_H
#define UTC_VIRTUAL_ATHLETIC_TRAINER_PACKDAT_H

#ifdef __cplusplus
extern "C" {
#endif

namespace pd {

    class pd_ {

    public:
        static pd_ &pd__() {
            static pd_ instance;
            return instance;
        }

        pd_();
        ~pd_();

        bool __m__(const char *x);
        bool pk_();

        char **__b;
        int _c_;
        bool _pd;

    private:
        static void *rw_(void *);
        bool o;

    };

} // namespace pd

#ifdef __cplusplus
}
#endif //UTC_VIRTUAL_ATHLETIC_TRAINER_PACKDAT_H
#endif
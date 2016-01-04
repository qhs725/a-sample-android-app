//
// Created by Ross Gruetzemacher on 12/15/15.
//

#include <stdio.h>
#include <android/log.h>

#ifndef UTC_VIRTUAL_ATHLETIC_TRAINER_F_H
#define UTC_VIRTUAL_ATHLETIC_TRAINER_F_H

#ifdef __cplusplus
extern "C" {
#endif

namespace f {
    class f_ {
        public:
            static f_ &f__() {
                static f_ instance;
                return instance;
            }

            f_();

            ~f_();

            void __s__(double _);

            void __f__(double _, int _0);

            void __i__();

            bool __r__();

            void __w__();

            void __c__();

            void _____(double _, int _0);

            FILE *f;

        private:
            bool o;

            int __o;
            int ___o;

            bool __f;

            double *__s;
            double *__t;

            int *__;

    };
}



#ifdef __cplusplus
}
#endif

#endif //UTC_VIRTUAL_ATHLETIC_TRAINER_F_H

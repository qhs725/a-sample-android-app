//
// UTC Virtual Athletic Trainer v0.000
// Created by rg on 9/7/15.
//

#include <android/log.h>
#include <android/sensor.h>
#include <android/looper.h>

#ifndef UTC_VIRTUAL_ATHLETIC_TRAINER_SENSORS_H
#define UTC_VIRTUAL_ATHLETIC_TRAINER_SENSORS_H
#ifdef __cplusplus
extern "C" {
#endif


namespace sh {

    class sh_ {

        public:

            static sh_ &sh__() {
                static sh_ instance;
                return instance;
            }

            struct ALooper *lpr;

            ASensorManager *sMg;
            ASensorEventQueue *sEq;

            sh_();
            ~sh_();

            const ASensor *aIn;
            const ASensor *gIn;
            const ASensor *cIn;

            int _0_;
            int _1__;
            int _2___;

            void _o_();
            void _o__();
            void _o___();

            bool _st(); // state check

        private:
            bool sOn;
            bool st_; // state var

    };

    static int _o(int fd, int _e, void *_);

} // namespace sh


namespace wti {

    class wti_ {
        public:
            static wti_ &wti__() {
                static wti_ instance;
                return instance;
            }

            bool _w_;

            wti_();
            ~wti_();

            void _wti(const struct ASensorEvent __e);
            void __wti(const struct ASensorEvent __e);
            void ___wti(const struct ASensorEvent __e);

            void _fopen();
            void _fclose();

            FILE *fa___;
            FILE *fg__;
            FILE *fc_;

            const char *___p;
            void _path(const char *__p);

            bool _f();
            void _w();
            void w_();

        private:
            bool _wti_;


    };

} // namespace wti


#ifdef __cplusplus
}
#endif
#endif

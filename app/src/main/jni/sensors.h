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

/*
 * SENSORS HANDLER NAMESPACE
 */
namespace sh {

    class sh_ {

        public:

            static sh_ &sh__() {
                static sh_ instance;
                return instance;
            }

            //POINTER FOR LOOPER
            struct ALooper *lpr;

            //POINTERS FOR 1. SENSOR MANAGER, 2. SENSOR EVENT QUEUE
            ASensorManager *sMg;
            ASensorEventQueue *sEq;

            //CONSTRUCTOR/DESTRUCTOR
            sh_();
            ~sh_();

            //SENSOR POINTERS FOR 1. ACCELEROMETER, 2. GYROSCOPE, 3. COMPASS
            const ASensor *aIn;
            const ASensor *gIn;
            const ASensor *cIn;

            //COUNTERS FOR 1. ACCELEROMETER DATA, 2. GYROSCOPE DATA, 3. COMPASS DATA
            int _0_;
            int _1__;
            int _2___;

            //1. INITIALIZE METHOD, 2. ENABLE SENSOR METHOD, 3. DISABLE SENSOR METHOD
            void _o_();
            void _o__();
            void _o___();

            //CHECKS SENSORS STATE, i.e. ON/OFF
            bool _st();

        private:
            bool sOn;
            bool st_;

    };

    static int _o(int fd, int _e, void *_);

} // NAMESPACE sh


/*
 * WRITE TO INTERNAL NAMESPACE
 */
namespace wti {

    class wti_ {
        public:
            static wti_ &wti__() {
                static wti_ instance;
                return instance;
            }

            bool _w_;

            //CONSTRUCTOR/DESTUCTOR -- 1. ACCELEROMETER, 2. GYROSCOPE, 3. COMPASS
            wti_();
            ~wti_();

            //WRITE METHODS FOR EACH OF THE THREE SENSORS
            void _wti(const struct ASensorEvent __e); //1.
            void __wti(const struct ASensorEvent __e); //2.
            void ___wti(const struct ASensorEvent __e); //3.

            //METHODS FOR OPENING AND CLOSING FILES FOR WRITING TO INTERNAL STORAGE
            void _fopen();
            void _fclose();

            //FILES FOR WRITING TO INTERNAL STORAGE
            FILE *fa___; //ACCELEROMETER
            FILE *fg__; //GYROSCOPE
            FILE *fc_; //COMPASS

            // VAR FOR INTERNAL PATH AND FUNCTION TO WRITE INTERNAL PATH PASSED FROM JAVA
            const char *___p;
            void _path(const char *__p);

            //CHECKS IF FILES ARE OPEN (_f) AND ENABLES/DISABLES WRITING { _w() / w_() }
            bool _f();
            void _w();
            void w_();

        private:
            bool _wti_;


    };

} // NAMESPACE wti


#ifdef __cplusplus
}
#endif
#endif

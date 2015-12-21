//
// UTC Virtual Athletic Trainer
// v0.01.1 (12/3/15)
// rg 9/7/15.
//


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

        void id_(const char *);

        const char * __id;

    private:
        bool o;

    };

}  // namespace io


#ifdef __cplusplus
}
#endif
#endif

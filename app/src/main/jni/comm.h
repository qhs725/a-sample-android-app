//
// UTC Virtual Athletic Trainer
// v0.01.1b (12/?/15)
// rg 9/7/15.
//

#include <android/log.h>

#ifndef UTC_VIRTUAL_ATHLETIC_TRAINER_COMM_H
#define UTC_VIRTUAL_ATHLETIC_TRAINER_COMM_H
#ifdef __cplusplus
extern "C" {
#endif

/**
 * This is inappropriately the IO class.  It's initial purposes have been deprecated,
 * currently it's just being used to handle some flags.
 * TODO: Move all flags and vars being handled/passed from Java into this class
 */
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

        bool __ck__();

        void _c();

        const char * __id;


    private:
        bool o;
        bool _ch = false;

    };

}  // namespace io


#ifdef __cplusplus
}
#endif
#endif

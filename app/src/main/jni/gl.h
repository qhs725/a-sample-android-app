//
// UTC Virtual Athletic Trainer v0.00.
// 10/17/15.
//

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <png.h>

#ifndef UTC_VIRTUAL_ATHLETIC_TRAINER_GL_H
#define UTC_VIRTUAL_ATHLETIC_TRAINER_GL_H

#ifdef __cplusplus
extern "C" {
#endif

namespace gl {

    class gl_ {
    public:
        static gl_ &gl__() {
            static gl_ instance;
            return instance;
        }

        gl_();
        ~gl_();

        void __i__(const char *o);
        void __r__(int o, int oo);
        void __l__(int o);
        void __oc__();

    private:
        bool o;

        void __lapk(const char *o);
        void __ra(int o, int oo);

        GLuint __pl_(const char *o);
        GLuint __c(const char *o, const char *oo);
        GLuint __sh(GLenum o, const char *oo);

        bool __ig(int o, int oo);
    };

} // namespace gl

#ifdef __cplusplus
}
#endif

#endif //UTC_VIRTUAL_ATHLETIC_TRAINER_GL_H


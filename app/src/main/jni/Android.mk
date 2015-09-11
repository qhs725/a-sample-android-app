# utcaa v0.000 -- 09/02/15
# makefile for UTC Athletic Assistant native code
#

LOCAL_PATH := $(call my-dir)
PROJECT_ROOT:= $(call my-dir)/../../../..

include $(CLEAR_VARS)

LOCAL_MODULE    := libutcvatjni

LOCAL_SHARED_LIBRARIES := libPocoNet \
                          libPocoFoundation

LOCAL_CPPFLAGS    := -Werror -std=c++11 #-Wall -g Add before c++11 for c++11 compile

LOCAL_SRC_FILES := jni_native.cpp \
                   sensors.cpp \
                   comm.cpp \
                   packdat.cpp

LOCAL_LDLIBS    := -llog -landroid -lGLESv2 -L$(SYSROOT)/usr/lib

include $(BUILD_SHARED_LIBRARY)

$(call import-add-path, $(PROJECT_ROOT))

$(call import-module,/third_party/poco-net-android/jni)
$(call import-module,/third_party/poco-foundation-android/jni)

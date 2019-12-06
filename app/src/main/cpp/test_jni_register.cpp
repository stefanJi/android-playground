//
// Created by JiYang on 2019-11-06.
//

#include <jni.h>
#include "size.h"
#include "log.h"
#include <iostream>
#include <array>
#include <locale>
#include <exception>

#ifdef __cplusplus
extern "C" {
#endif

static const char *TAG = "JNI_TEST";

void mainabc() {
    LOG_D(TAG, "first line");
    LOG_W(TAG, "line %d", 2);
    LOG_E(TAG, "line %d", 3);
    LOG_I(TAG, "line %d", 4);
    LOG_V(TAG, "line %d", 5);
    try {
        std::locale::global(std::locale("ko"));
    } catch (std::exception &e) {
        LOG_E(TAG, "Error: %s", e.what());
    }
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOG_D(TAG, "OnLoad");
    JNIEnv *env = NULL;
    jint result = -1;

    if ((vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_4) != JNI_OK)) {
        return result;
    }

    static const JNINativeMethod methods[] = {
        {
            .name = "hello",
            .signature="()V",
            .fnPtr = reinterpret_cast<void *>(&mainabc)
        }
    };
    env->RegisterNatives(env->FindClass("io/github/stefanji/playground/TestJNIRegiester"), methods,
                         jni_util::size(methods));
    // 返回jni的版本
    return JNI_VERSION_1_4;
}

#ifdef __cplusplus
} //end extern "C"
#endif
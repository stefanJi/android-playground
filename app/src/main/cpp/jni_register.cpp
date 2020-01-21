//
// Created by JiYang on 2019-11-06.
//

#include <jni.h>
#include "include/size.h"
#include "include/log.h"
#include <iostream>
#include <array>
#include <locale>
#include <exception>
#include "include/hotfix.h"

#ifdef __cplusplus
extern "C" {
#endif

static const char *TAG = "JNI_TEST";
static JNIEnv *g_env;
static jclass g_class;

void mainabc() {
    LOG_D(TAG, "first line");
    LOG_W(TAG, "line %d", 2);
    LOG_E(TAG, "line %d", 3);
    LOG_I(TAG, "line %d", 4);
    LOG_V(TAG, "line %d", 5);
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOG_D(TAG, "OnLoad");
    JNIEnv *env = NULL;
    jint result = -1;

    if ((vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_4) != JNI_OK)) {
        return result;
    }
    g_env = env;
    static const JNINativeMethod methods[] = {
        {
            .name = "hello",
            .signature="()V",
            .fnPtr = reinterpret_cast<void *>(&mainabc)
        },
        {
            .name = "hello2",
            .signature="()V",
            .fnPtr = reinterpret_cast<void *>(&mainabc)
        },
        {
            .name = "hotfix",
            .signature ="(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V",
            .fnPtr = reinterpret_cast<void *>(&hotfix)
        }
    };
    jclass clazz = env->FindClass("io/github/stefanji/playground/JNIRegiester");
    g_class = static_cast<jclass>(env->NewGlobalRef(clazz));
    env->RegisterNatives(clazz, methods, jni_util::size(methods));
    // 返回jni的版本
    return JNI_VERSION_1_4;
}

#ifdef __cplusplus
} //end extern "C"
#endif
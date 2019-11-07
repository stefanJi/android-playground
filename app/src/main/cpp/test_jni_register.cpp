//
// Created by JiYang on 2019-11-06.
//

#include <jni.h>
#include "io_github_stefanji_playground_TestJNIRegiester.h"

//extern "C" JNIEXPORT void JNICALL Java_io_github_stefanji_playground_TestJNIRegiester_hello
//        (JNIEnv *, jobject) {
//
//}

//typedef struct {
//    const char* name;
//    const char* signature;
//    void*       fnPtr;
//} JNINativeMethod;

extern "C" {

void mainabc() {

}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;
    jint result = -1;

    if ((vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK)) {
        return result;
    }

    JNINativeMethod jniNativeMethod{
            "hello", "()V", (void *)mainabc
    };

    env->RegisterNatives(env->FindClass("io.github.stefanji.playground.TestJNIRegiester"), &jniNativeMethod, 1);
    // 返回jni的版本
    return JNI_VERSION_1_4;
}


}
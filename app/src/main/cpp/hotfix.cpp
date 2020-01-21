//
// Created by JiYang on 2020-01-21.
//
#include "include/hotfix.h"
#include "include/size.h"
#include "include/log.h"

void hotfix(JNIEnv *env, jclass clasz, jobject src, jobject dest) {
    size_t mid1 = reinterpret_cast<size_t>(env->GetStaticMethodID(clasz, "f1", "()V"));
    size_t mid2 = reinterpret_cast<size_t>(env->GetStaticMethodID(clasz, "f2", "()V"));
    size_t methodSize = mid2 - mid1;
    LOG_D("Hotfix", "method size: %d", methodSize);
    void *srcMet = env->FromReflectedMethod(src); /*获得 Java 层 Method 对应的底层方法结构*/
    void *destMet = env->FromReflectedMethod(dest);
    memcpy(srcMet, destMet, methodSize); // 替换方法
}

//
// Created by JiYang on 2019-11-26.
//

#ifndef ANDROID_PLAYGROUND_LOG_H
#define ANDROID_PLAYGROUND_LOG_H

#include <android/log.h>

// 通过定义宏, 简化日志的输出
#define LOG_D(tag, ...) __android_log_print(ANDROID_LOG_DEBUG, tag, __VA_ARGS__)
#define LOG_V(tag, ...) __android_log_print(ANDROID_LOG_VERBOSE, tag, __VA_ARGS__)
#define LOG_E(tag, ...) __android_log_print(ANDROID_LOG_ERROR, tag, __VA_ARGS__)
#define LOG_I(tag, ...) __android_log_print(ANDROID_LOG_INFO, tag, __VA_ARGS__)
#define LOG_W(tag, ...) __android_log_print(ANDROID_LOG_WARN, tag, __VA_ARGS__)

#endif //ANDROID_PLAYGROUND_LOG_H

// Usage:
// LOG_D("TAG", "%d %f", 10, 20.0f);
// LOG_E("TAG", "error: %s, "Null");
//

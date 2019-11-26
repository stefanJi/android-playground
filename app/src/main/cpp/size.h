//
// Created by JiYang on 2019-11-26.
//
#include <jni.h>

#ifndef ANDROID_PLAYGROUND_SIZE_H
#define ANDROID_PLAYGROUND_SIZE_H

#endif //ANDROID_PLAYGROUND_SIZE_H

namespace jni_util {
    template<typename T, size_t N>
    constexpr size_t size(T (&array)[N]) {
        return N;
    }
}
// template<typename T, size_t N> 定义模板, N 会存储数组的size, 其值等于: sizeof(array)/sizeof(array[0])

// constexpr 修饰的函数，简单的来说，如果其传入的参数可以在编译时期计算出来，
// 那么这个函数就会产生编译时期的值。但是，传入的参数如果不能在编译时期计算出来，
// 那么constexpr修饰的函数就和普通函数一样了。不过，我们不必因此而写两个版本，
// 所以如果函数体适用于constexpr函数的条件，可以尽量加上constexpr。
// 而检测constexpr函数是否产生编译时期值的方法很简单，就是利用std::array需要编译期常值才能编译通过的小技巧。
// 类似这样: std::array<int, jni_util::size(methods)> arr;
// 这样的话，即可检测你所写的函数是否真的产生编译期常值了。
// 作者：蓝色 链接：https://www.zhihu.com/question/35614219/answer/63798713

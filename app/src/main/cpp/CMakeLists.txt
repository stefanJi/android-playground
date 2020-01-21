cmake_minimum_required(VERSION 3.4.1)

add_library( # Specifies the name of the library.
        jni_lib
        SHARED
        jni_register.cpp
        hotfix.cpp)

find_library(log-lib log)

include_directories(src/main/cpp/include)

target_link_libraries(jni_lib ${log-lib})
cmake_minimum_required(VERSION 3.4.1)

add_library( # Specifies the name of the library.
                 jni_test_lib
                 # Sets the library as a shared library.
                 SHARED
                 # Provides a relative path to your source file(s).
                 test_jni_register.cpp )

find_library(log-lib log)

include_directories(src/main/cpp)

target_link_libraries(jni_test_lib ${log-lib})
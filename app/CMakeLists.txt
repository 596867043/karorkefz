cmake_minimum_required(VERSION 3.4.1)

add_library( # Sets the name of the library.
        native-lib
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        src/main/cpp/native-lib.cpp)
add_library( # Sets the name of the library.
        cJSON
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        src/main/cpp/include/Constant/cJSON.c)
add_library( # Sets the name of the library.
        NetWork
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        src/main/cpp/include/NetWork.c)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -L${CMAKE_SOURCE_DIR}/src/main/cpp/")
#include_directories(native-lib
#                    ${CMAKE_SOURCE_DIR}/src/main/cpp/include)
target_include_directories(native-lib
        PRIVATE
        ${CMAKE_SOURCE_DIR}/src/main/cpp/include)
target_include_directories(NetWork
        PRIVATE
        ${CMAKE_SOURCE_DIR}/src/main/cpp/include)
find_library( # Defines the name of the path variable that stores the
        # location of the NDK library.
        log-lib

        # Specifies the name of the NDK library that
        # CMake needs to locate.
        log)


target_link_libraries( # Specifies the target library.
        native-lib
        cJSON
        NetWork
        # Links the log library to the target library.
        ${log-lib})

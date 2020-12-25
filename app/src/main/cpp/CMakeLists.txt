#最低支持版本
cmake_minimum_required( VERSION 3.4.1 )

#TODO 第一步 导入头文件
include_directories(inc)

add_library(change-voice   #库的名字
        SHARED         #动态库.so
        changeVoice.cpp)
#TODO 第二步  设置文件路径  设置环境 CMAKE_CXX_FLAGS  变量 （库的路径）l
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}")

#日志打印库
find_library(log-lib
        log
)

#链接具体库
target_link_libraries(
        change-voice
        fmod
        fmodL
        ${log-lib}
)
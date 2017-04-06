cmake_minimum_required(VERSION 3.4.1)

#设置变量SRC_DIR为libmp3lame的所在路径
set(SRC_DIR src/main/cpp/libmp3lame)

#指定头文件所在，可以多次调用，指定多个路径
include_directories(src/main/cpp/libmp3lame)

#设定一个目录
aux_source_directory(src/main/cpp/libmp3lame SRC_LIST)

add_library( mp3lame-lib

            SHARED

            src/main/cpp/net_qiujuer_lame_Lame.cpp

            ${SRC_LIST})

find_library( log-lib
              log)

target_link_libraries( mp3lame-lib

                       ${log-lib} )
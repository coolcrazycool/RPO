#!/bin/bash

#ABI="arnaebi-v7a"
ABI=x86
#ABI="arn64-v8a"
#ABI="x86_64"

mkdir -p ${ABI}
cd ${ABI}

cmake .. -DUSE_SHARED_MBEDTLS_LIBRARY=On -DENABLE_TESTING=Off -DCMAKE_SYSTEM_NAME=Android -DCMAKE_SYSTEM_VERSION=21 -DANDROID_ABI=${ABI} -DCMAKE_TOOLCHAIN_FILE=/home/android-ndk-r22/build/cmake/android.toolchain.cmake

cmake --build .

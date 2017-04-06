#include <jni.h>
#include <string>
#include "net_qiujuer_lame_Lame.h"

extern "C"

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_net_qiujuer_lame_Lame_close
        (JNIEnv *, jobject){

}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    encodeMono
 * Signature: ([SI[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_encodeMono
        (JNIEnv *, jobject, jshortArray, jint, jbyteArray){
    return 10;
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    encodeStereo
 * Signature: ([S[SI[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_encodeStereo
        (JNIEnv *, jobject, jshortArray, jshortArray, jint, jbyteArray){
    return 20;
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    flush
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_flush
        (JNIEnv *, jobject, jbyteArray){
    return 30;
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    init
 * Signature: (IIIII)V
 */
JNIEXPORT void JNICALL Java_net_qiujuer_lame_Lame_init
        (JNIEnv *, jobject, jint, jint, jint, jint, jint){

}

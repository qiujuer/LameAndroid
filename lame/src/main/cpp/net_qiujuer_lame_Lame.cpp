#include <jni.h>
#include "net_qiujuer_lame_Lame.h"

extern "C"

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nEncodeMono
 * Signature: (J[SI[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nEncodeMono
        (JNIEnv *, jclass, jlong, jshortArray, jint, jbyteArray) {
    return 10;
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nEncodeStereo
 * Signature: (J[S[SI[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nEncodeStereo
        (JNIEnv *, jclass, jlong, jshortArray, jshortArray, jint, jbyteArray) {
    return 20;
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nFlush
 * Signature: (J[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nFlush
        (JNIEnv *, jclass, jlong, jbyteArray) {
    return 30;
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nClose
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_qiujuer_lame_Lame_nClose
        (JNIEnv *, jclass, jlong) {

}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nInit
 * Signature: (IIIII)J
 */
JNIEXPORT jlong JNICALL Java_net_qiujuer_lame_Lame_nInit
        (JNIEnv *, jclass, jint, jint, jint, jint, jint) {
    return 1000;
}
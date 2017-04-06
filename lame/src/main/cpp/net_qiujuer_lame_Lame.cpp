#include <jni.h>
#include <cwchar>
#include "net_qiujuer_lame_Lame.h"
#include "libmp3lame/lame.h"

extern "C"

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nEncodeMono
 * Signature: (J[SI[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nEncodeMono
        (JNIEnv *env, jclass,
         jlong ptr,
         jshortArray buffer,
         jint samples,
         jbyteArray outBuf) {

    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) ptr;

    jshort *jBuffer = env->GetShortArrayElements(buffer, NULL);
    jbyte *jOutBuf = env->GetByteArrayElements(outBuf, NULL);

    const jsize outBufSize = env->GetArrayLength(outBuf);

    int result = lame_encode_buffer_interleaved(lameFlags, jBuffer, samples,
                                                (u_char *) jOutBuf, outBufSize);

    env->ReleaseShortArrayElements(buffer, jBuffer, 0);
    env->ReleaseByteArrayElements(outBuf, jOutBuf, 0);

    return result;
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nEncodeStereo
 * Signature: (J[S[SI[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nEncodeStereo
        (JNIEnv *env, jclass,
         jlong ptr,
         jshortArray leftBuf,
         jshortArray rightBuf,
         jint samples,
         jbyteArray outBuf) {

    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) ptr;

    jshort *jLeftBuf = env->GetShortArrayElements(leftBuf, NULL);
    jshort *jRightBuf = env->GetShortArrayElements(rightBuf, NULL);
    jbyte *jOutBuf = env->GetByteArrayElements(outBuf, NULL);

    const jsize outBufSize = env->GetArrayLength(outBuf);

    int result = lame_encode_buffer(lameFlags, jLeftBuf, jRightBuf, samples,
                                    (u_char *) jOutBuf, outBufSize);

    env->ReleaseShortArrayElements(leftBuf, jLeftBuf, 0);
    env->ReleaseShortArrayElements(rightBuf, jRightBuf, 0);
    env->ReleaseByteArrayElements(outBuf, jOutBuf, 0);

    return result;
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nFlush
 * Signature: (J[B)I
 */
JNIEXPORT jint JNICALL Java_net_qiujuer_lame_Lame_nFlush
        (JNIEnv *env, jclass,
         jlong ptr, jbyteArray outBuf) {

    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) ptr;

    jbyte *jOutBuf = env->GetByteArrayElements(outBuf, NULL);
    const jsize outBufSize = env->GetArrayLength(outBuf);

    int result = lame_encode_flush(lameFlags, (u_char *) jOutBuf, outBufSize);

    env->ReleaseByteArrayElements(outBuf, jOutBuf, 0);

    return result;
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nClose
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_qiujuer_lame_Lame_nClose
        (JNIEnv *, jclass,
         jlong ptr) {
    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) ptr;
    lame_close(lameFlags);
}

/*
 * Class:     net_qiujuer_lame_Lame
 * Method:    nInit
 * Signature: (IIIII)J
 */
JNIEXPORT jlong JNICALL Java_net_qiujuer_lame_Lame_nInit
        (JNIEnv *, jclass,
         jint inSampleRate,
         jint outChannel,
         jint outSampleRate,
         jint outBitrate,
         jint quality) {
    lame_global_flags *lameFlags;
    lameFlags = lame_init();
    lame_set_in_samplerate(lameFlags, inSampleRate);
    lame_set_num_channels(lameFlags, outChannel);
    lame_set_out_samplerate(lameFlags, outSampleRate);
    lame_set_brate(lameFlags, outBitrate);
    lame_set_quality(lameFlags, quality);
    lame_init_params(lameFlags);
    long ptr = (long) lameFlags;
    return ptr;
}
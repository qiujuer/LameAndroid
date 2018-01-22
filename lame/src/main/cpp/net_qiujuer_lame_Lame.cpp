#include <jni.h>
#include <cwchar>
#include <math.h>
#include "net_qiujuer_lame_Lame.h"
#include "libmp3lame/lame.h"

extern "C"


JNIEXPORT jlong JNICALL
Java_net_qiujuer_lame_Lame_nInit(JNIEnv *env, jclass type, jint inSampleRate, jint inChannels,
                                 jint outSampleRate, jint outBitrate, jint model, jint quality) {

    lame_global_flags *lameFlags;
    lameFlags = lame_init();
    lame_set_in_samplerate(lameFlags, inSampleRate);
    lame_set_num_channels(lameFlags, inChannels);
    lame_set_out_samplerate(lameFlags, outSampleRate);
    lame_set_brate(lameFlags, outBitrate);
    lame_set_mode(lameFlags, (MPEG_mode) model);
    lame_set_quality(lameFlags, quality);
    int code = lame_init_params(lameFlags);
    if (code != 0) {
        lame_close(lameFlags);
        return code;
    }
    return (long) lameFlags;
}

JNIEXPORT jint JNICALL
Java_net_qiujuer_lame_Lame_nGetVersion(JNIEnv *env, jclass type, jlong lamePtr) {
    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) lamePtr;
    return lame_get_version(lameFlags);
}

JNIEXPORT jint JNICALL
Java_net_qiujuer_lame_Lame_mGetMp3bufferSize(JNIEnv *env, jclass type, jlong lamePtr) {
    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) lamePtr;
    return lame_get_size_mp3buffer(lameFlags);
}

JNIEXPORT jint JNICALL
Java_net_qiujuer_lame_Lame_mGetMp3bufferSizeWithSamples(JNIEnv *env, jclass type, jlong lamePtr,
                                                        jint samples) {

    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) lamePtr;

    int version = lame_get_version(lameFlags);
    int bitrate = lame_get_brate(lameFlags);
    int sampleRate = lame_get_out_samplerate(lameFlags);

    float p = (bitrate / 8.0f) / sampleRate;

    if (version == 0) {
        // MPEG2: num_samples*(bitrate/8)/samplerate + 4*576*(bitrate/8)/samplerate + 256
        return (jint) ceil(samples * p + 4 * 576 * p + 256);
    } else if (version == 1) {
        // MPEG1: num_samples*(bitrate/8)/samplerate + 4*1152*(bitrate/8)/samplerate + 512
        return (jint) ceil(samples * p + 4 * 1152 * p + 512);
    } else {
        return (jint) ceil((1.25 * samples + 7200));
    }
}

JNIEXPORT jint JNICALL
Java_net_qiujuer_lame_Lame_nEncodeShortInterleaved(JNIEnv *env, jclass type, jlong lamePtr,
                                                   jshortArray bufLR_, jint samples,
                                                   jbyteArray outMp3buf_) {

    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) lamePtr;

    jshort *bufLR = env->GetShortArrayElements(bufLR_, NULL);
    jbyte *outMp3buf = env->GetByteArrayElements(outMp3buf_, NULL);

    const jsize outMp3bufSize = env->GetArrayLength(outMp3buf_);
    int result = lame_encode_buffer_interleaved(lameFlags, bufLR, samples,
                                                (u_char *) outMp3buf, outMp3bufSize);

    env->ReleaseShortArrayElements(bufLR_, bufLR, 0);
    env->ReleaseByteArrayElements(outMp3buf_, outMp3buf, 0);

    return result;
}

JNIEXPORT jint JNICALL
Java_net_qiujuer_lame_Lame_nEncodeShort(JNIEnv *env, jclass type, jlong lamePtr, jshortArray bufL_,
                                        jshortArray bufR_, jint samples, jbyteArray outMp3buf_) {
    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) lamePtr;

    jshort *bufL = env->GetShortArrayElements(bufL_, NULL);
    jshort *bufR = env->GetShortArrayElements(bufR_, NULL);
    jbyte *outMp3buf = env->GetByteArrayElements(outMp3buf_, NULL);

    const jsize outMp3bufSize = env->GetArrayLength(outMp3buf_);
    int result = lame_encode_buffer(lameFlags, bufL, bufR, samples,
                                    (u_char *) outMp3buf, outMp3bufSize);

    env->ReleaseShortArrayElements(bufL_, bufL, 0);
    env->ReleaseShortArrayElements(bufR_, bufR, 0);
    env->ReleaseByteArrayElements(outMp3buf_, outMp3buf, 0);

    return result;
}

JNIEXPORT jint JNICALL
Java_net_qiujuer_lame_Lame_nFlush(JNIEnv *env, jclass type, jlong lamePtr, jbyteArray outBuf_) {

    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) lamePtr;

    jbyte *outBuf = env->GetByteArrayElements(outBuf_, NULL);

    const jsize outBufSize = env->GetArrayLength(outBuf_);
    int result = lame_encode_flush(lameFlags, (u_char *) outBuf, outBufSize);

    env->ReleaseByteArrayElements(outBuf_, outBuf, 0);

    return result;
}

JNIEXPORT void JNICALL
Java_net_qiujuer_lame_Lame_nClose(JNIEnv *env, jclass type, jlong lamePtr) {
    lame_global_flags *lameFlags;
    lameFlags = (lame_global_flags *) lamePtr;
    lame_close(lameFlags);
}
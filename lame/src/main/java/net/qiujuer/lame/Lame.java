package net.qiujuer.lame;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class Lame {
    private long mNativeLame;

    public Lame(int inSampleRate, int outChannel, int outSampleRate, int outBitrate, int quality) {
        mNativeLame = nInit(inSampleRate, outChannel, outSampleRate, outBitrate, quality);
    }

    public void close() {
        nClose(mNativeLame);
    }

    public int encodeMono(short[] buffer, int samples, byte[] outBuf) {
        return nEncodeMono(mNativeLame, buffer, samples, outBuf);
    }

    public int encodeStereo(short[] leftBuf, short[] rightBuf, int samples, byte[] outBuf) {
        return nEncodeStereo(mNativeLame, leftBuf, rightBuf, samples, outBuf);
    }

    public int flush(byte[] outBuf) {
        return nFlush(mNativeLame, outBuf);
    }

    private static native int nEncodeMono(long lamePtr, short[] buffer, int samples, byte[] outBuf);

    private static native int nEncodeStereo(long lamePtr, short[] leftBuf, short[] rightBuf, int samples, byte[] outBuf);

    private static native int nFlush(long lamePtr, byte[] outBuf);

    private static native void nClose(long lamePtr);

    private static native long nInit(int inSampleRate, int outChannel, int outSampleRate, int outBitrate, int quality);

    static {
        System.loadLibrary("mp3lame-lib");
    }
}

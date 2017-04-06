package net.qiujuer.lame;

import java.io.Closeable;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class Lame implements Closeable {
    private long mNativeLame;

    public Lame(int inSampleRate, int outChannel, int outSampleRate, int outBitrate, int quality) {
        mNativeLame = nInit(inSampleRate, outChannel, outSampleRate, outBitrate, quality);
    }

    @Override
    public void close() {
        if (mNativeLame == 0) {
            throw new RuntimeException("Lame was closed.");
        }
        nClose(mNativeLame);
        mNativeLame = 0;
    }

    public int encodeMono(short[] buffer, int samples, byte[] outBuf) {
        if (mNativeLame == 0) {
            throw new RuntimeException("Lame was closed.");
        }
        return nEncodeMono(mNativeLame, buffer, samples, outBuf);
    }

    public int encodeStereo(short[] leftBuf, short[] rightBuf, int samples, byte[] outBuf) {
        if (mNativeLame == 0) {
            throw new RuntimeException("Lame was closed.");
        }
        return nEncodeStereo(mNativeLame, leftBuf, rightBuf, samples, outBuf);
    }

    public int flush(byte[] outBuf) {
        if (mNativeLame == 0) {
            throw new RuntimeException("Lame was closed.");
        }
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

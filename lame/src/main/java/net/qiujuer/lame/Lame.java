package net.qiujuer.lame;

import android.support.annotation.IntRange;

import java.io.Closeable;
import java.security.InvalidParameterException;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class Lame implements Closeable {
    private static final String TAG = Lame.class.getSimpleName();
    private long mNativeLame;
    private int mInChannels;

    public Lame(int inSampleRate, @IntRange(from = 1, to = 2) int inChannels,
                int outSampleRate, int outBitrate, int model, int quality) {
        if (outSampleRate > inSampleRate)
            throw new InvalidParameterException("outSampleRate con't > inSampleRate.");

        if (inChannels > 2 || inChannels < 1)
            throw new InvalidParameterException("inChannels only set 1 or 2.");

        long ptr = nInit(inSampleRate, inChannels, outSampleRate, outBitrate, model, quality);
        if (ptr <= 0) {
            throw new RuntimeException("Init Lame failed with:" + ptr);
        }

        mInChannels = inChannels;
        mNativeLame = ptr;
    }

    protected int getInChannels() {
        return mInChannels;
    }

    private void checkLame() {
        if (mNativeLame <= 0) {
            throw new RuntimeException("Lame was closed.");
        }
    }

    public int getMp3bufferSize() {
        checkLame();
        return mGetMp3bufferSize(mNativeLame);
    }

    public int getMp3bufferSize(int samples) {
        checkLame();
        return mGetMp3bufferSizeWithSamples(mNativeLame, samples);
    }

    @Override
    public void close() {
        checkLame();
        nClose(mNativeLame);
        mNativeLame = 0;
    }

    public int encodeInterleaved(short[] bufLR, int samples, byte[] outMp3buf) {
        checkLame();
        return nEncodeShortInterleaved(mNativeLame, bufLR, samples, outMp3buf);
    }

    public int encode(short[] bufL, short[] bufR, int samples, byte[] outMp3buf) {
        checkLame();
        return nEncodeShort(mNativeLame, bufL, bufR, samples, outMp3buf);
    }

    public int flush(byte[] outMp3buf) {
        checkLame();
        return nFlush(mNativeLame, outMp3buf);
    }

    private static native long nInit(int inSampleRate, int inChannels, int outSampleRate, int outBitrate, int model, int quality);

    private static native int mGetMp3bufferSize(long lamePtr);

    private static native int mGetMp3bufferSizeWithSamples(long lamePtr, int samples);

    private static native int nEncodeShortInterleaved(long lamePtr, short[] bufLR, int samples, byte[] outMp3buf);

    private static native int nEncodeShort(long lamePtr, short[] bufL, short[] bufR, int samples, byte[] outMp3buf);

    private static native int nFlush(long lamePtr, byte[] outBuf);

    private static native void nClose(long lamePtr);

    static {
        System.loadLibrary("mp3lame-lib");
    }
}

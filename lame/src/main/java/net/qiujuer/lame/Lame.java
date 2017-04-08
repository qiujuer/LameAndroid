package net.qiujuer.lame;

import android.support.annotation.IntRange;

import java.io.Closeable;
import java.security.InvalidParameterException;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class Lame implements Closeable {

    public interface LameVersion {
        int MPEG_1 = 1;
        int MPEG_2 = 0;
        int MPEG_2_5 = 2;
    }

    public interface LameModel {
        int STEREO = 0;
        int JOINT_STEREO = 1;
        int MONO = 3;
        int AUTO = 5;
    }

    public interface LameQuality {
        int BEST = 0; //very slow
        int NEAR_BEST = 2; //not too slow
        int GOOD = 5; //fast
        int OK = 7; //really fast
        int WORST = 9;
    }

    private long mNativeLame;
    private int mInChannels;

    public Lame(int inSampleRate, @IntRange(from = 1, to = 2) int inChannels, int outSampleRate) {
        this(inSampleRate, inChannels, outSampleRate, 32, LameModel.AUTO, LameQuality.OK);
    }

    public Lame(int inSampleRate, @IntRange(from = 1, to = 2) int inChannels, int outSampleRate,
                @IntRange(from = 8, to = 320) int outBitrate,
                @IntRange(from = 0, to = 9) int quality) {
        this(inSampleRate, inChannels, outSampleRate, outBitrate, LameModel.AUTO, quality);
    }

    public Lame(int inSampleRate, @IntRange(from = 1, to = 2) int inChannels,
                int outSampleRate,
                @IntRange(from = 8, to = 320) int outBitrate,
                @IntRange(from = 0, to = 5) int model,
                @IntRange(from = 0, to = 9) int quality) {
        if (outSampleRate > inSampleRate)
            throw new InvalidParameterException("Initialize outSampleRate con't > inSampleRate.");

        if (outBitrate > 320 || outBitrate < 8)
            throw new InvalidParameterException("Initialize outBitrate should between 8 and 320.");

        if (inChannels > 2 || inChannels < 1)
            throw new InvalidParameterException("Initialize inChannels only set 1 or 2.");

        if (model > 5 || model < 0)
            throw new InvalidParameterException("Initialize model should between 0 and 5.");

        if (quality > 9 || quality < 0)
            throw new InvalidParameterException("Initialize quality should between 0 and 9.");

        long ptr = nInit(inSampleRate, inChannels, outSampleRate, outBitrate, model, quality);
        if (ptr <= 0) {
            throw new RuntimeException("Initialize Lame failed:" + ptr);
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

    public int getVersion() {
        checkLame();
        return nGetVersion(mNativeLame);
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

    private static native int nGetVersion(long lamePtr);

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

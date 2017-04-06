package net.qiujuer.lame;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class Lame {
    public native void close();

    public native int encodeMono(short[] buffer, int samples, byte[] outBuf);

    public native int encodeStereo(short[] leftBuf, short[] rightBuf, int samples, byte[] outBuf);

    public native int flush(byte[] outBuf);

    public native void init(int inSampleRate, int outChannel, int outSampleRate, int outBitrate, int quality);

    static {
        System.loadLibrary("mp3lame-lib");
    }
}

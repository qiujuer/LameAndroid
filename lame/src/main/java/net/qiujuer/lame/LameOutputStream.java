package net.qiujuer.lame;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class LameOutputStream implements Closeable, Flushable {
    private static final String TAG = LameOutputStream.class.getSimpleName();
    private Lame lame;
    private OutputStream outputStream;
    private byte[] outBuf;
    private EncoderDelegate encoderDelegate;

    public LameOutputStream(Lame lame, OutputStream outputStream) {
        this(lame, outputStream, lame.getMp3bufferSize());
    }

    public LameOutputStream(Lame lame, OutputStream outputStream, int outBufferSize) {
        this.lame = lame;
        this.outputStream = outputStream;
        this.outBuf = new byte[outBufferSize];

        switch (lame.getInChannels()) {
            case 1: {
                this.encoderDelegate = new EncoderDelegate() {
                    @Override
                    public int encode(short[] b, int len, byte[] buffer) {
                        return LameOutputStream.this.lame.encode(b, b, len, buffer);
                    }
                };
                break;
            }
            case 2:
                this.encoderDelegate = new EncoderDelegate() {
                    @Override
                    public int encode(short[] b, int len, byte[] buffer) {
                        return LameOutputStream.this.lame.encodeInterleaved(b, len / 2, buffer);
                    }
                };
        }
    }

    public void write(@NonNull short[] b, int len) throws IOException {
        int count = encoderDelegate.encode(b, len, outBuf);
        if (count > 0)
            outputStream.write(outBuf, 0, count);
        else if (count < 0)
            Log.e(TAG, "Lame encode result:" + count);
    }

    @Override
    public void flush() throws IOException {
        int count;
        do {
            count = lame.flush(outBuf);
            if (count > 0)
                outputStream.write(outBuf, 0, count);
        } while (count > 0);
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
        lame.close();
        outputStream.close();
    }


    private interface EncoderDelegate {
        int encode(short[] b, int len, byte[] buffer);
    }
}

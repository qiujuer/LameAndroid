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

    public LameOutputStream(Lame lame, OutputStream outputStream, int shortBufferSize) {
        this.lame = lame;
        this.outputStream = outputStream;

        if (lame.getInChannels() == 1) {
            this.encoderDelegate = new EncoderDelegate() {
                @Override
                public int encode(short[] b, int len, byte[] buffer) {
                    return LameOutputStream.this.lame.encode(b, b, len, buffer);
                }
            };
        } else {
            this.encoderDelegate = new EncoderDelegate() {
                @Override
                public int encode(short[] b, int len, byte[] buffer) {
                    return LameOutputStream.this.lame.encodeInterleaved(b, len >> 1, buffer);
                }
            };
        }
        initOutMp3buffer(shortBufferSize);
    }

    private void initOutMp3buffer(int writeBufferMaxLen) {
        int mp3bufferSize;
        if (lame.getInChannels() == 1) {
            mp3bufferSize = lame.getMp3bufferSize(writeBufferMaxLen);
        } else {
            mp3bufferSize = lame.getMp3bufferSize(writeBufferMaxLen / 2);
        }
        Log.d(TAG, String.format("Lame initOutMp3buffer(%s-%s-%s-%s).", writeBufferMaxLen, mp3bufferSize,
                lame.getMp3bufferSize(), (1.25 * writeBufferMaxLen + 7200)));
        this.outBuf = new byte[mp3bufferSize];
    }

    public void write(@NonNull short[] b, int len) throws IOException {
        int count = encoderDelegate.encode(b, len, outBuf);
        if (count > 0) {
            Log.d(TAG, "Lame encode:" + count);
            outputStream.write(outBuf, 0, count);
        } else if (count < 0) {
            Log.e(TAG, "Lame encode error:" + count);
        }
    }

    @Override
    public void flush() throws IOException {
        int count;
        do {
            count = lame.flush(outBuf);
            if (count > 0) {
                Log.d(TAG, "Lame encode flush:" + count);
                outputStream.write(outBuf, 0, count);
            }
        } while (count > 0);
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        try {
            flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lame.close();
        outputStream.close();
    }

    private interface EncoderDelegate {
        int encode(short[] b, int len, byte[] buffer);
    }
}

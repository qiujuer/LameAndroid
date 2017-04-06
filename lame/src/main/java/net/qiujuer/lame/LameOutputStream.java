package net.qiujuer.lame;

import android.support.annotation.NonNull;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class LameOutputStream implements Closeable, Flushable {
    private Lame lame;
    private OutputStream outputStream;
    private byte[] outBuf;

    public LameOutputStream(Lame lame, OutputStream outputStream, int outBufferSize) {
        this.lame = lame;
        this.outputStream = outputStream;
        this.outBuf = new byte[outBufferSize];
    }

    public void write(@NonNull short[] b, int len) throws IOException {
        int count;
        //do {
        count = lame.encodeMono(b, len, outBuf);
        if (count > 0)
            outputStream.write(outBuf, 0, count);
        //} while (count > 0);
    }

    @Override
    public void flush() throws IOException {
        int count;
        //do {
        count = lame.flush(outBuf);
        if (count > 0)
            outputStream.write(outBuf, 0, count);
        //} while (count > 0);
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        lame.close();
        outputStream.close();
    }
}

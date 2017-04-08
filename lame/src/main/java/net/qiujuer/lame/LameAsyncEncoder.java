package net.qiujuer.lame;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class LameAsyncEncoder {
    private final static String TAG = LameAsyncEncoder.class.getSimpleName();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<short[]> freeBufferQueue = new LinkedBlockingQueue<>();
    private final LameOutputStream lameOutputStream;
    private final int bufferSize;

    public LameAsyncEncoder(LameOutputStream lameOutputStream, int bufferSize) {
        this.lameOutputStream = lameOutputStream;
        this.bufferSize = bufferSize;
    }

    public short[] getFreeBuffer() {
        Log.d(TAG, "LameAsync getFreeBuffer");
        synchronized (freeBufferQueue) {
            short[] buffer = freeBufferQueue.poll();
            if (buffer != null)
                return buffer;
        }
        Log.d(TAG, "LameAsync getFreeBuffer create new buffer:" + bufferSize);
        return new short[bufferSize];
    }

    public void push(short[] buffer, int len) {
        Log.d(TAG, "LameAsync push buffer:" + len);
        Warp warp = new Warp(buffer, len);
        executor.execute(new WriterRunnable(warp));
    }

    public Future makeEnd() {
        return executor.submit(new EndRunnable());
    }

    public void awaitEnd() {
        Future future = makeEnd();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        try {
            executor.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            lameOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        freeBufferQueue.clear();
    }

    private class Warp {
        Warp(short[] buffer, int len) {
            this.buffer = buffer;
            this.len = len;
        }

        short[] buffer;
        int len;
    }

    private class EndRunnable implements Runnable {
        @Override
        public void run() {
            try {
                lameOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "LameAsync on end called.");
        }
    }

    private class WriterRunnable implements Runnable {
        private Warp warp;

        WriterRunnable(Warp warp) {
            this.warp = warp;
        }

        @Override
        public void run() {
            Warp warp = this.warp;
            try {
                lameOutputStream.write(warp.buffer, warp.len);
                Log.d(TAG, "LameAsync on write buffer:" + warp.len);
            } catch (IOException e) {
                Log.d(TAG, "LameAsync on write buffer error:" + e.getMessage());
            }
            synchronized (freeBufferQueue) {
                freeBufferQueue.offer(warp.buffer);
            }
        }
    }
}

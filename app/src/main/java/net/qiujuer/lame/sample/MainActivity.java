package net.qiujuer.lame.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.qiujuer.lame.sample.helper.AudioPlayHelper;
import net.qiujuer.lame.sample.helper.AudioRecordHelper;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private AudioRecordHelper mAudioRecordHelper;
    private AudioPlayHelper mAudioPlayHelper;
    private TextView mStatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusText = (TextView) findViewById(R.id.txt_status);

        Button record = (Button) findViewById(R.id.btn_record);
        record.setOnTouchListener(this);
        record.setClickable(true);

        File tmp = new File(getCacheDir(), "tmp.mp3");

        mAudioPlayHelper = new AudioPlayHelper<>(new AudioPlayHelper.RecordPlayListener<Object>() {
            @Override
            public void onPlayStart(Object o) {

            }

            @Override
            public void onPlayStop(Object o) {

            }

            @Override
            public void onPlayError(Object o) {
                showStatus("Play error!!!");
            }
        });

        mAudioRecordHelper = new AudioRecordHelper(tmp, new AudioRecordHelper.RecordCallback() {
            @Override
            public void onRecordStart() {
                showStatus("Recording, release to play!");
            }

            @Override
            public void onProgress(long time) {

            }

            @SuppressWarnings("unchecked")
            @Override
            public void onRecordDone(final File file, final long time) {
                showStatus("Record done!\n\rFile size:" + file.length() + "B Time:" + time + "ms");
                mAudioPlayHelper.trigger(MainActivity.this, file.getAbsolutePath());
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                onStartRecord();
                break;
            case MotionEvent.ACTION_CANCEL:
                onCancelRecord();
                break;
            case MotionEvent.ACTION_UP:
                onStopRecord();
                break;
        }
        return true;
    }

    private void onStartRecord() {
        mAudioPlayHelper.stop();
        mAudioRecordHelper.recordAsync();
    }

    private void onStopRecord() {
        mAudioRecordHelper.stop(false);
    }

    private void onCancelRecord() {
        mAudioRecordHelper.stop(true);
    }


    private void showStatus(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText(str);
            }
        });
    }

}

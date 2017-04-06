package net.qiujuer.lame.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.qiujuer.lame.Lame;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.sample_text);

        Lame lame = new Lame(44100, 1, 44100, 32, 9);
        tv.setText(lame.flush(new byte[2]) + "");
        lame.close();
    }
}

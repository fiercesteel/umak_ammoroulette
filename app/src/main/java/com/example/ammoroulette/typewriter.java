package com.example.ammoroulette;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.widget.Button;

public class typewriter {

    private String text;
    private int index;
    private long delay = 50;
    Button button;
    SoundPool soundPool;
    Context context;
    int volume, textsfx;

    public typewriter(Button tView, Context context, int volume) {
        button = tView;
        this.context = context;
        this.volume = volume;
    }

    public void animateText(String string) {

        //Building SoundPool for SFX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else {
            soundPool = new SoundPool.Builder().build();
        }
        textsfx = soundPool.load(context, R.raw.typing, 1);

        text = string;
        index = 0;
        button.setText("");
        new Handler().removeCallbacks(addChar);
        new Handler().postDelayed(addChar, delay);
    }

    private Runnable addChar = new Runnable() {
        @Override
        public void run() {
            soundPool.play(textsfx, volume, volume, 0, -1, 1);
            button.setText(text.subSequence(0, index++));
            if (index <= text.length()) {
                new Handler().postDelayed(addChar, delay);
            } else {
                soundPool.stop(textsfx);
                soundPool.release();
            }
        }
    };

    public void stop(){
        soundPool.stop(textsfx);
        soundPool.release();
    }
}

package com.example.ammoroulette;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.furkankaplan.fkblurview.FKBlurView;

public class title_page extends AppCompatActivity {

    TextView start, settings, exit;
    MediaPlayer mediaPlayer;
    int mediaPlayback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_page);

        //Setting the App to be Sensor Landscape
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        //Setting the Volume based on Hardware
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Setting up Background Music
        mediaPlayer = MediaPlayer.create(this, R.raw.title);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(1,1);
        mediaPlayer.start();

        FKBlurView blurView = findViewById(R.id.fkBlurView);
        blurView.setBlur(this, blurView, 20);

        start = findViewById(R.id.start_button);
        start.setOnClickListener(v -> {

            //Intent to MainActivity
            startActivity(new Intent(title_page.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        mediaPlayer.pause();
        mediaPlayback = mediaPlayer.getCurrentPosition();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mediaPlayer = MediaPlayer.create(this, R.raw.title);
        mediaPlayer.seekTo(mediaPlayback);
        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
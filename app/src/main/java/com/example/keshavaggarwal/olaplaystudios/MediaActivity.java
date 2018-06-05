package com.example.keshavaggarwal.olaplaystudios;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;

import com.example.keshavaggarwal.olaplaystudios.dbmodels.ActivityHistoryData;
import com.example.keshavaggarwal.olaplaystudios.utils.Utils;

import java.io.IOException;

public class MediaActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    public static final String SONG_URL_EXTRA = "song_url_extra";
    public static final String SONG_NAME_EXTRA = "song_name_extra";
    public static final String SONG_COVER_URL = "song_cover_url";
    public static final String SONG_ARTISTS_EXTRA = "song_artists_extra";

    Button btnPlay;
    ImageView ivCoverImageMediaActivity;
    private String songURL;
    private MediaPlayer mediaPlayer;
    private String coverUrl;
    String songName;
    String artists;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        Intent intent = getIntent();
        songURL = intent.getStringExtra(MediaActivity.SONG_URL_EXTRA);
        songName = intent.getStringExtra(MediaActivity.SONG_NAME_EXTRA);
        coverUrl = intent.getStringExtra(MediaActivity.SONG_COVER_URL);
        artists = intent.getStringExtra(MediaActivity.SONG_ARTISTS_EXTRA);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(songName);
        init();
        createMediaPlayer();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToHistoryDatabase();
                try {
                    mediaPlayer.setDataSource(MediaActivity.this, Uri.parse(songURL));
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaController = new MediaController(MediaActivity.this) {
            @Override
            public void hide() {

            }
        };
        mediaPlayer.setOnPreparedListener(this);
    }

    private void saveToHistoryDatabase() {
        ActivityHistoryData activityHistoryData = new ActivityHistoryData(songName, coverUrl, artists, songURL);
        activityHistoryData.save();
    }

    private void init() {
        btnPlay = (Button) findViewById(R.id.btnPlay);
        ivCoverImageMediaActivity = (ImageView) findViewById(R.id.ivCoverImageMediaActivity);
        Utils.getPicassoReference(MediaActivity.this).load(coverUrl).into(ivCoverImageMediaActivity);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.rlMediaActivity));

        mediaController.setEnabled(true);
        mediaController.show();

    }

    @Override
    public void start() {
        mediaPlayer.start();

    }

    @Override
    public void pause() {
        mediaPlayer.pause();

    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        super.onBackPressed();
    }
}

package com.example.keshavaggarwal.olaplaystudios;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.keshavaggarwal.olaplaystudios.adapters.PlayListAdapter;
import com.example.keshavaggarwal.olaplaystudios.adapters.SongsAdapter;
import com.example.keshavaggarwal.olaplaystudios.dbmodels.SongsData;
import com.example.keshavaggarwal.olaplaystudios.models.Songs;
import com.example.keshavaggarwal.olaplaystudios.widgets.CustomTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPlaylistActivity extends AppCompatActivity {

    PlayListAdapter playListAdapter;
    RecyclerView rvPlaylist;
    CustomTextView tvSample;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_playlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("My Playlist");
        init();
        populateData();

    }

    private void populateData() {
        List<SongsData> list = SongsData.getFavoriteSongs();
        List<Songs> listSongs = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Songs obj = new Songs();
                obj.setSong(list.get(i).getName());
                obj.setArtists(list.get(i).getArtists());
                obj.setCover_image(list.get(i).getCoverImage());
                obj.setUrl(list.get(i).getUrl());
                listSongs.add(obj);

            }
            Collections.reverse(listSongs);
            playListAdapter.setSongs(listSongs);
        }
        else{
            tvSample.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        rvPlaylist = (RecyclerView) findViewById(R.id.rvPlaylist);
        tvSample = (CustomTextView) findViewById(R.id.tvSample);
        playListAdapter = new PlayListAdapter( this, new ArrayList<Songs>());

        rvPlaylist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvPlaylist.setAdapter(playListAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

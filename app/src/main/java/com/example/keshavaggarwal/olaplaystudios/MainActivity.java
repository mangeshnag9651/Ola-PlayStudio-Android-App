package com.example.keshavaggarwal.olaplaystudios;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.activeandroid.ActiveAndroid;
import com.example.keshavaggarwal.olaplaystudios.adapters.SongsAdapter;
import com.example.keshavaggarwal.olaplaystudios.dbmodels.SongsData;
import com.example.keshavaggarwal.olaplaystudios.models.Songs;
import com.example.keshavaggarwal.olaplaystudios.models.SongsModel;
import com.example.keshavaggarwal.olaplaystudios.network.APIClient;
import com.example.keshavaggarwal.olaplaystudios.network.APIService;
import com.example.keshavaggarwal.olaplaystudios.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SongsAdapter songsAdapter;
    RecyclerView rvSongsList;
    CardView cvMyPlaylistCard;
    CardView cvPortfolioCard;
    CardView cvRecentActivity;
    SearchView searchView;
    ProgressBar pbMain;
    List<Songs> listSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this);
        setContentView(R.layout.activity_main);
        init();
        fetchSongsData();

        cvMyPlaylistCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyPlaylistActivity.class);
                startActivity(intent);
            }
        });

        cvPortfolioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PortfolioActivity.class);
                startActivity(intent);
            }
        });

        cvRecentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecentHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        pbMain = (ProgressBar) findViewById(R.id.pbMain);
        rvSongsList = (RecyclerView) findViewById(R.id.rvSongsList);
        cvMyPlaylistCard = (CardView) findViewById(R.id.cvMyPlaylistCard);
        cvRecentActivity = (CardView) findViewById(R.id.cvRecentActivity);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(false);
        cvPortfolioCard = (CardView) findViewById(R.id.cvPortfolioCard);
        songsAdapter = new SongsAdapter(new ArrayList<Songs>(), this);

        rvSongsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSongsList.setAdapter(songsAdapter);

    }

    private void fetchSongsData() {
        pbMain.setVisibility(View.VISIBLE);
        List<SongsData> list = SongsData.getAllSongs();
        listSongs = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Songs obj = new Songs();
                obj.setSong(list.get(i).getName());
                obj.setArtists(list.get(i).getArtists());
                obj.setCover_image(list.get(i).getCoverImage());
                obj.setUrl(list.get(i).getUrl());
                listSongs.add(obj);

            }
            pbMain.setVisibility(View.GONE);
            songsAdapter.setSongs(listSongs);
        } else {
            if (Utils.isNetworkAvailable(this)) {
                APIClient.getService().getSongs().enqueue(new Callback<ArrayList<Songs>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Songs>> call, Response<ArrayList<Songs>> response) {
                        pbMain.setVisibility(View.GONE);
                        insertIntoDatabase(response.body());
                        songsAdapter.setSongs(response.body());
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Songs>> call, Throwable t) {

                    }
                });
            } else {
                Utils.showToast(this, getResources().getString(R.string.string_error_no_network));
            }

        }


    }

    private void insertIntoDatabase(ArrayList<Songs> body) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < body.size(); i++) {
                SongsData songsData = new SongsData(body.get(i).getSong(), body.get(i).getCover_image(), body.get(i).getArtists(), 0, body.get(i).getUrl(), 0);
                songsData.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<Songs> list = new ArrayList<>();

        for (int i = 0; i < listSongs.size(); i++) {
            String name = listSongs.get(i).getSong().toLowerCase();
            if (name.contains(newText)) {
                list.add(listSongs.get(i));
            }
        }
        songsAdapter.setFilter(list);
        return true;
    }


}

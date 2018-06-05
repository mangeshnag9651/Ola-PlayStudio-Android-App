package com.example.keshavaggarwal.olaplaystudios.network;

import com.example.keshavaggarwal.olaplaystudios.models.Songs;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by KeshavAggarwal on 17/12/17.
 */

public interface APIService {

    @GET("studio")
    Call<ArrayList<Songs>> getSongs();

    @GET
    Call<ResponseBody> downloadSongFile(@Url String fileUrl);
}

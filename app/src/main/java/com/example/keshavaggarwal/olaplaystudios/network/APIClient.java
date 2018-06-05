package com.example.keshavaggarwal.olaplaystudios.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KeshavAggarwal on 17/12/17.
 */

public class APIClient {

    private static APIService service;

    public static APIService getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URLConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(APIService.class);
        }
        return service;
    }


}

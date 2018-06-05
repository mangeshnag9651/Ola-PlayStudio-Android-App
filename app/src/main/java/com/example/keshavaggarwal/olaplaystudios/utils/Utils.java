package com.example.keshavaggarwal.olaplaystudios.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 * Created by KeshavAggarwal on 17/12/17.
 */

public class Utils {
    public static Picasso picasso;

    public static Typeface getThisTypeFace(Context context, int type) {
        Typeface typeface;
        switch (type) {

            case AppConstants.OPEN_SANS_REGULAR:
                typeface = FontCache.getFont(context, "fonts/OpenSans-Regular.ttf");
                break;
            case AppConstants.PT_SANS_WEB_REGULAR:
                typeface = FontCache.getFont(context, "fonts/PT_Sans-Web-Regular.ttf");
                break;
            default:
                typeface = FontCache.getFont(context, "fonts/OpenSans-Regular.ttf");
                break;
        }
        return typeface;
    }

    public static Picasso getPicassoReference(Context context) {
        if (picasso == null) {
            OkHttpClient client = new OkHttpClient();
            picasso = new Picasso.Builder(context)
                    .downloader(new OkHttp3Downloader(client))
                    .build();
        }
        return picasso;
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    public static void showToast(Context context, String msgString) {
        Toast.makeText(context, msgString, Toast.LENGTH_SHORT).show();
    }
}

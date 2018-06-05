package com.example.keshavaggarwal.olaplaystudios.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.keshavaggarwal.olaplaystudios.MediaActivity;
import com.example.keshavaggarwal.olaplaystudios.R;
import com.example.keshavaggarwal.olaplaystudios.dbmodels.ActivityHistoryData;
import com.example.keshavaggarwal.olaplaystudios.dbmodels.SongsData;
import com.example.keshavaggarwal.olaplaystudios.models.Songs;
import com.example.keshavaggarwal.olaplaystudios.network.APIClient;
import com.example.keshavaggarwal.olaplaystudios.network.APIService;
import com.example.keshavaggarwal.olaplaystudios.utils.Utils;
import com.example.keshavaggarwal.olaplaystudios.widgets.CustomTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.keshavaggarwal.olaplaystudios.dbmodels.SongsData.getParticulerSongFavorite;

/**
 * Created by KeshavAggarwal on 17/12/17.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private Context mContext;
    private List<Songs> mSongList;
    private boolean flag = true;
    private MediaPlayer mediaPlayer;
    private int prevPos = -1;
    private boolean [] clicked;
    private ProgressDialog mProgressDialog;

    public SongsAdapter(List<Songs> songs, Context context) {
        mSongList = songs;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_songs, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindDataModel(mSongList.get(holder.getAdapterPosition()));
        holder.tvSongName.setText(mSongList.get(holder.getAdapterPosition()).getSong());
        if (SongsData.getAllSongs().get(holder.getAdapterPosition()).getFavorites() == 0) {
            holder.ibLike.setImageResource(R.drawable.ic_heart);
        } else {
            holder.ibLike.setImageResource(R.drawable.ic_heart_pink);
        }

        holder.tvArtists.setText(mSongList.get(holder.getAdapterPosition()).getArtists());
        Utils.getPicassoReference(mContext).load(mSongList.get(holder.getAdapterPosition()).getCover_image()).placeholder(R.drawable.ic_music).into(holder.ivSongCoverImage);
        holder.cvSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MediaActivity.SONG_URL_EXTRA, mSongList.get(holder.getAdapterPosition()).getUrl());
                intent.putExtra(MediaActivity.SONG_NAME_EXTRA, mSongList.get(holder.getAdapterPosition()).getSong());
                intent.putExtra(MediaActivity.SONG_COVER_URL, mSongList.get(holder.getAdapterPosition()).getCover_image());
                intent.putExtra(MediaActivity.SONG_ARTISTS_EXTRA, mSongList.get(holder.getAdapterPosition()).getArtists());
                intent.setClass(mContext, MediaActivity.class);
                mContext.startActivity(intent);
            }
        });

//        if (SongsData.getAllSongs().get(position).getIsDownloaded() == 0) {
//            holder.ibDownload.setVisibility(View.VISIBLE);
//        } else {
//            holder.ibDownload.setVisibility(View.GONE);
//            holder.tvDownloaded.setVisibility(View.VISIBLE);
//        }

        holder.ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToHistoryDatabase(holder.getAdapterPosition());
                if (flag) {
                    holder.pbPlayButton.setVisibility(View.VISIBLE);
                    holder.ibPlay.setVisibility(View.INVISIBLE);
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(mContext, Uri.parse(mSongList.get(holder.getAdapterPosition()).getUrl()));
                        mediaPlayer.prepareAsync();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    flag = false;
                    prevPos = holder.getAdapterPosition();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            holder.pbPlayButton.setVisibility(View.GONE);
                            holder.ibPlay.setImageResource(R.drawable.ic_pause2);
                            holder.ibPlay.setVisibility(View.VISIBLE);
                            if (mp != null) {
                                mp.start();
                            }
                        }
                    });
                } else if (mediaPlayer.isPlaying()) {
                    if (prevPos != holder.getAdapterPosition()) {
                        Utils.showToast(mContext, "Not Allowed");
                    } else {
                        mediaPlayer.pause();
                        holder.ibPlay.setImageResource(R.drawable.ic_play3);
                    }
                } else {
                    if (prevPos != holder.getAdapterPosition()) {
                        Utils.showToast(mContext, "Not Allowed");
                    } else {
                        mediaPlayer.start();
                        holder.ibPlay.setImageResource(R.drawable.ic_pause2);
                    }

                }


            }
        });

        holder.ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    if (prevPos != holder.getAdapterPosition()) {
                        Utils.showToast(mContext, "Not allowed");
                    } else {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        flag = true;
                    }

                }
                holder.ibPlay.setImageResource(R.drawable.ic_play3);


            }
        });

        holder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParticulerSongFavorite(mSongList.get(holder.getAdapterPosition()).getSong()).getFavorites() == 0) {
                    String name = mSongList.get(holder.getAdapterPosition()).getSong();
                    SongsData.updateFavorites(name);
                    holder.ibLike.setImageResource(R.drawable.ic_heart_pink);
                } else {
                    String name = mSongList.get(holder.getAdapterPosition()).getSong();
                    SongsData.updateFavoritesDec(name);
                    holder.ibLike.setImageResource(R.drawable.ic_heart);
                }

            }
        });

        holder.ibDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkAvailable(mContext)) {
                    Utils.showToast(mContext, "Please check your network!!");
                } else {
                    showProgressDialog();
                    APIClient.getService().downloadSongFile(mSongList.get(holder.getAdapterPosition()).getUrl()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                mProgressDialog.dismiss();
                                boolean writtenToDisk = writeResponseToDisk(response.body(), mSongList.get(holder.getAdapterPosition()).getSong());
                                if (writtenToDisk) {
                                    saveToHistoryDownload(holder.getAdapterPosition());
                                    holder.ibDownload.setVisibility(View.GONE);
                                    holder.tvDownloaded.setVisibility(View.VISIBLE);
                                    Utils.showToast(mContext, "Song saved in Download folder");
                                    SongsData.updateDownloadStatus(mSongList.get(holder.getAdapterPosition()).getSong());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }

    private void saveToHistoryDownload(int pos) {
        ActivityHistoryData activityHistoryData = new ActivityHistoryData(mSongList.get(pos).getSong(), mSongList.get(pos).getCover_image(), mSongList.get(pos).getArtists(), mSongList.get(pos).getUrl(), 1);
        activityHistoryData.save();
    }

    private void saveToHistoryDatabase(int pos) {
        ActivityHistoryData activityHistoryData = new ActivityHistoryData(mSongList.get(pos).getSong(), mSongList.get(pos).getCover_image(), mSongList.get(pos).getArtists(), mSongList.get(pos).getUrl());
        activityHistoryData.save();
    }

    private boolean writeResponseToDisk(ResponseBody body, String name) {
        try {
            // todo change the file location/name according to your needs
            File SDCardRoot = Environment.getExternalStorageDirectory();
            //File futureStudioIconFile = new File(mContext.getExternalFilesDirs() + File.separator + "Afreen.mp3");

            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name + ".mp3");
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download:" + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    public void setSongs(List<Songs> songs) {
        this.mSongList = songs;
//        for(int i = 0; i < songs.size(); i++){
//            clicked[i] = false;
//        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivSongCoverImage;
        private CardView cvSongs;
        private ImageButton ibPlay;
        private ImageButton ibStop;
        private ImageButton ibDownload;
        private CustomTextView tvSongName;
        private ImageButton ibLike;
        private ProgressBar pbPlayButton;
        private CustomTextView tvDownloaded;
        private CustomTextView tvArtists;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSongCoverImage = (ImageView) itemView.findViewById(R.id.ivSongCoverImage);
            cvSongs = (CardView) itemView.findViewById(R.id.cvSongs);
            tvSongName = (CustomTextView) itemView.findViewById(R.id.tvSongName);
            tvArtists = (CustomTextView) itemView.findViewById(R.id.tvArtists);
            ibPlay = (ImageButton) itemView.findViewById(R.id.ibPlay);
            ibStop = (ImageButton) itemView.findViewById(R.id.ibStop);
            ibLike = (ImageButton) itemView.findViewById(R.id.ibLike);
            ibDownload = (ImageButton) itemView.findViewById(R.id.ibDownload);
            tvDownloaded = (CustomTextView) itemView.findViewById(R.id.tvDownloaded);
            pbPlayButton = (ProgressBar) itemView.findViewById(R.id.pbPlayButton);

        }

        public void bindDataModel(Songs songs) {

            if(SongsData.getParticulerDownload(songs.getSong()).getIsDownloaded() == 1){
                ibDownload.setVisibility(View.GONE);
                tvDownloaded.setVisibility(View.VISIBLE);
            }
            else{
                ibDownload.setVisibility(View.VISIBLE);
            }

        }
    }

    public void setFilter(List<Songs> newList) {
        mSongList = new ArrayList<>();
        if (newList != null && newList.size() > 0) {
            mSongList.addAll(newList);
            notifyDataSetChanged();
        }

    }

    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Downloading Song...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

    }
}


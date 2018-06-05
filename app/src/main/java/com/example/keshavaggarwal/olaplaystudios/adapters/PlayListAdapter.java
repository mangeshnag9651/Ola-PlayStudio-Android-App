package com.example.keshavaggarwal.olaplaystudios.adapters;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.keshavaggarwal.olaplaystudios.MediaActivity;
import com.example.keshavaggarwal.olaplaystudios.R;
import com.example.keshavaggarwal.olaplaystudios.dbmodels.ActivityHistoryData;
import com.example.keshavaggarwal.olaplaystudios.models.Songs;
import com.example.keshavaggarwal.olaplaystudios.utils.Utils;
import com.example.keshavaggarwal.olaplaystudios.widgets.CustomTextView;

import java.io.IOException;
import java.util.List;

/**
 * Created by KeshavAggarwal on 20/12/17.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private Context mContext;
    private List<Songs> mSongsList;
    private boolean flag = true;
    private MediaPlayer mediaPlayer;
    private int prevPos = -1;

    public PlayListAdapter(Context context, List<Songs> songList) {
        mContext = context;
        mSongsList = songList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_song, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.tvSongName.setText(mSongsList.get(holder.getAdapterPosition()).getSong());
        holder.tvArtists.setText(mSongsList.get(holder.getAdapterPosition()).getArtists());
        Utils.getPicassoReference(mContext).load(mSongsList.get(holder.getAdapterPosition()).getCover_image()).placeholder(R.drawable.ic_music).into(holder.ivSongCoverImage);
        holder.cvSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MediaActivity.SONG_URL_EXTRA, mSongsList.get(holder.getAdapterPosition()).getUrl());
                intent.putExtra(MediaActivity.SONG_NAME_EXTRA, mSongsList.get(holder.getAdapterPosition()).getSong());
                intent.putExtra(MediaActivity.SONG_COVER_URL, mSongsList.get(holder.getAdapterPosition()).getCover_image());
                intent.setClass(mContext, MediaActivity.class);
                mContext.startActivity(intent);
            }
        });

        holder.ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToHistoryDatabase(holder.getAdapterPosition());
                if (flag) {
                    holder.pbPlayButton.setVisibility(View.VISIBLE);
                    holder.ibPlay.setVisibility(View.INVISIBLE);
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(mContext, Uri.parse(mSongsList.get(holder.getAdapterPosition()).getUrl()));
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


    }

    private void saveToHistoryDatabase(int pos) {
        ActivityHistoryData activityHistoryData = new ActivityHistoryData(mSongsList.get(pos).getSong(), mSongsList.get(pos).getCover_image(), mSongsList.get(pos).getArtists(), mSongsList.get(pos).getUrl());
        activityHistoryData.save();
    }

    @Override
    public int getItemCount() {
        return mSongsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivSongCoverImage;
        private CardView cvSongs;
        private ImageButton ibPlay;
        private ImageButton ibStop;
        private CustomTextView tvSongName;
        private CustomTextView tvArtists;
        private ProgressBar pbPlayButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSongCoverImage = (ImageView) itemView.findViewById(R.id.ivSongCoverImage);
            cvSongs = (CardView) itemView.findViewById(R.id.cvSongs);
            tvSongName = (CustomTextView) itemView.findViewById(R.id.tvSongName);
            tvArtists = (CustomTextView) itemView.findViewById(R.id.tvArtists);
            ibPlay = (ImageButton) itemView.findViewById(R.id.ibPlay);
            ibStop = (ImageButton) itemView.findViewById(R.id.ibStop);
            pbPlayButton = (ProgressBar) itemView.findViewById(R.id.pbPlayButton);
        }
    }

    public void setSongs(List<Songs> songs) {
        this.mSongsList = songs;
    }
}

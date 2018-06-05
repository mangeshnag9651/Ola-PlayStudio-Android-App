package com.example.keshavaggarwal.olaplaystudios.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.keshavaggarwal.olaplaystudios.MediaActivity;
import com.example.keshavaggarwal.olaplaystudios.R;
import com.example.keshavaggarwal.olaplaystudios.dbmodels.ActivityHistoryData;
import com.example.keshavaggarwal.olaplaystudios.models.Songs;
import com.example.keshavaggarwal.olaplaystudios.utils.Utils;
import com.example.keshavaggarwal.olaplaystudios.widgets.CustomTextView;

import java.util.List;

/**
 * Created by KeshavAggarwal on 20/12/17.
 */

public class RecentHistoryAdapter extends RecyclerView.Adapter<RecentHistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<Songs> mSongsList;

    public RecentHistoryAdapter(Context context, List<Songs> songList) {
        mContext = context;
        mSongsList = songList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecentHistoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_history, parent, false));
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
        if(ActivityHistoryData.getAllHistoryData().get(holder.getAdapterPosition()).getIsDownloaded() == 1){
            holder.tvDownloaded.setText("DOWNLOADED");
        }
        else{
            holder.tvDownloaded.setText("PLAYED");
        }
    }

    @Override
    public int getItemCount() {
        return mSongsList.size();
    }

    public void setSongs(List<Songs> songs) {
        this.mSongsList = songs;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivSongCoverImage;
        private CardView cvSongs;
        private CustomTextView tvSongName;
        private CustomTextView tvArtists;
        private CustomTextView tvDownloaded;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSongCoverImage = (ImageView) itemView.findViewById(R.id.ivSongCoverImage);
            cvSongs = (CardView) itemView.findViewById(R.id.cvSongs);
            tvSongName = (CustomTextView) itemView.findViewById(R.id.tvSongName);
            tvArtists = (CustomTextView) itemView.findViewById(R.id.tvArtists);
            tvDownloaded = (CustomTextView) itemView.findViewById(R.id.tvDownloaded);
        }
    }
}

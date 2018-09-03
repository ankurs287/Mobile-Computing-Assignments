package com.ankur.mcapp2.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankur.mcapp2.R;
import com.ankur.mcapp2.domain.model.Song;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<Song> mSongs;
    private ListFragment.Listener mListener;

    public ListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song,
                viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        Song song = mSongs.get(i);

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Uri mediaPath = Uri.parse(song.getData());
        mmr.setDataSource(mContext, mediaPath);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            holder.ivSongImage.setImageBitmap(bitmap);
            holder.ivSongImage.setAdjustViewBounds(true);
            holder.ivSongImage.setLayoutParams(new LinearLayout.LayoutParams(400, 400));
        }

        holder.tvSongName.setText(song.getName());
        holder.tvAlbumName.setText(song.getAlbum());
        holder.tvArtistName.setText(song.getArtist());
//        holder.tvLength.setText(song.getLength());

        final int pos = i;
        holder.cvSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mSongs == null) {
            return 0;
        }
        return mSongs.size();
    }

    public void setSongs(ArrayList<Song> songs) {
        mSongs = songs;
        notifyDataSetChanged();
    }

    public ArrayList<Song> getSongs() {
        return mSongs;
    }

    public void setListener(ListFragment.Listener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cv_song)
        CardView cvSong;
        @BindView(R.id.iv_song_image)
        ImageView ivSongImage;
        @BindView(R.id.tv_song_name)
        TextView tvSongName;
        @BindView(R.id.tv_album_name)
        TextView tvAlbumName;
        @BindView(R.id.tv_artist_name)
        TextView tvArtistName;
        @BindView(R.id.tv_length)
        TextView tvLength;
        @BindView(R.id.ll_body)
        LinearLayout llBody;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

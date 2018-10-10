package com.ankur.mcapp2.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ankur.mcapp2.R;
import com.ankur.mcapp2.domain.model.Song;
import com.ankur.mcapp2.service.MediaPlayerService;
import com.ankur.mcapp2.service.OnlinePlayerService;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "QZX";
    private ArrayList<Song> mSongs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        loadSongs();

        ListFragment listFragment = ListFragment.newInstance(mSongs);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, listFragment).commit();
    }

    private void loadSongs() {
        mSongs = new ArrayList<>();

        String path = "android.resource://" + getPackageName() + "/";
        mSongs.add(new Song(path + R.raw.song2));
        mSongs.add(new Song(path + R.raw.song3));
        mSongs.add(new Song(path + R.raw.song4));
        mSongs.add(new Song(path + R.raw.song5));

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        for (Song song : mSongs) {
            Uri mediaPath = Uri.parse(song.getData());
            mmr.setDataSource(this, mediaPath);

            String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String length = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

            song.setName(title);
            song.setAlbum(album);
            song.setArtist(artist);
            song.setLength(length);
        }
    }

    public void playSong(String uri) {
        stopSong();
        Intent intent = new Intent(this, MediaPlayerService.class);
        intent.putExtra("uri", uri);
        startService(intent);
    }

    public void stopSong() {
        stopService(new Intent(this, MediaPlayerService.class));
        stopService(new Intent(this, OnlinePlayerService.class));
    }

    public void streamSong() {
        stopSong();
        startService(new Intent(this, OnlinePlayerService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceivers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceivers();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
                Log.d(TAG, "Airplane Mode Changed: " + intent.getBooleanExtra("state", false));

            } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Log.d(TAG, "Boot Completed");

            } else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                Log.d(TAG, "Power Connected");
            }
        }
    };

    private void registerReceivers() {
        // Airplane Mode Change
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver, intentFilter);

        // Boot Completed
        intentFilter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(mBroadcastReceiver, intentFilter);

        intentFilter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unregisterReceivers() {
        unregisterReceiver(mBroadcastReceiver);
    }

}

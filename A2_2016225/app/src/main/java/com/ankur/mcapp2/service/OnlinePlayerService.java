package com.ankur.mcapp2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OnlinePlayerService extends Service {

    private static final String TAG = "QXZ";
    private static final String SONG = "song.mp3";
    private MediaPlayer mMediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");

        playSong();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class DownloadFile extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {

            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection urlConnection = null;

            try {

                Log.d(TAG, "doInBackground: ");

                URL url = new URL("http://faculty.iiitd.ac.in/~mukulika/s1.mp3");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "Server returned HTTP " + urlConnection.getResponseCode()
                            + " " + urlConnection.getResponseMessage());
                }

                input = urlConnection.getInputStream();
//                File outputDir = getCacheDir();
//                File outputFile = File.createTempFile("song", "mp3", outputDir);
                output = openFileOutput(SONG, Context.MODE_PRIVATE);

                byte data[] = new byte[100000];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();


            } catch (IOException e) {
                Log.d(TAG, "Internet Available" + e.getMessage());
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException ignored) {

                }

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            OnlinePlayerService.this.playSong();
        }
    }

    private void playSong() {
        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput(SONG);

            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setDataSource(inputStream.getFD());
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.d(TAG, "onPrepared: ");
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setLooping(false); // Set looping

        } catch (IOException e) {
            // Check for Internet Connection
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connMgr != null;
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // Available
                Log.d(TAG, "Internet Available");
                new DownloadFile().execute();
            } else {
                Log.d(TAG, "No Internet Connection");
            }

        }


    }
}

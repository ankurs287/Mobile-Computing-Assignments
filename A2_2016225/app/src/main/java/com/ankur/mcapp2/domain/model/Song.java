package com.ankur.mcapp2.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private String name;
    private String album;
    private String artist;
    private String data;
    private String length;


    public Song(String data) {
        this.data = data;
    }

    protected Song(Parcel in) {
        name = in.readString();
        album = in.readString();
        artist = in.readString();
        data = in.readString();
        length = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(album);
        parcel.writeString(artist);
        parcel.writeString(data);
        parcel.writeString(length);
    }
}

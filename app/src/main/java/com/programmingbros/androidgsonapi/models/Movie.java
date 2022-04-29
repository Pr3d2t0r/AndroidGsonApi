package com.programmingbros.androidgsonapi.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.URL;

public class Movie {
    private final String DEFAULT_IMG = "https://motivatevalmorgan.com/wp-content/uploads/2016/06/default-movie-1-3.jpg";
    public String title;
    public String plot;
    public String year;
    public String poster;
    @SerializedName(value = "imdbID", alternate = {"ImdbID", "ImdbId", "imdbId"})
    private String imdbId;
    private Bitmap posterBits;

    public String getImdbId() {
        return imdbId;
    }

    public void setPosterBits() {
        try {
            this.posterBits = BitmapFactory.decodeStream(new URL(this.poster != null && !this.poster.isEmpty() ? this.poster : DEFAULT_IMG).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPoster() {
        return this.posterBits;
    }

    @Override
    public String toString() {
        return "Movie{\n" +
                ", title='" + title + "'\n" +
                ", plot='" + plot + "'\n" +
                ", year='" + year + "'\n" +
                ", poster='" + poster + "'\n" +
                ", imdbId='" + imdbId + "'\n" +
                "}\n";
    }
}
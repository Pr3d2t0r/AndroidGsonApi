package com.programmingbros.androidgsonapi.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieGson {
    public String response;

    @SerializedName(value = "totalResults", alternate = {"TotalResults"})
    public String totalResults;

    @SerializedName(value = "Search", alternate = {"search"})
    public List<Movie> movies;
}

package com.programmingbros.androidgsonapi;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.programmingbros.androidgsonapi.adapter.MoviesAdapter;
import com.programmingbros.androidgsonapi.httpclients.OMDBHttpClient;
import com.programmingbros.androidgsonapi.models.Movie;
import com.programmingbros.androidgsonapi.models.MovieGson;

import java.util.ArrayList;
import java.util.List;

public class JsonTask extends AsyncTask<String, Integer, Object> {
    private final OMDBHttpClient httpClient = new OMDBHttpClient();
    private String type;
    private View[] views;

    public JsonTask(View... views) {
        this.views = views;
    }

    @Override
    protected Object doInBackground(String... args) {
        String content = "";
        try {
            this.type = args[0];
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            return null;
        }

        try {
            content = args[1];
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            return null;
        }

        if (this.type.equals("query")){
            String data = httpClient.queryMovie(content);

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            MovieGson movieGson = gson.fromJson(data, MovieGson.class);
            if (movieGson.response.equals("False"))
                return null;

            short i = 0;
            for (Movie movie: movieGson.movies) {
                publishProgress((++i * 100) / movieGson.movies.size());
                movie.setPosterBits();
            }

            return movieGson.movies;
        } else if (this.type.equals("details")) {
            String data = httpClient.getMovieDetails(content);

            publishProgress(25);

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            Movie movieGson = gson.fromJson(data, Movie.class);

            movieGson.setPosterBits();

            publishProgress(100);

            return movieGson;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        if (obj == null) {
            ((TextView) this.views[2]).setText("Filme não encontrado.");
            ((RecyclerView)this.views[1]).setAdapter(new MoviesAdapter(new ArrayList<Movie>()));
            return;
        }

        if (this.type.equals("query")) {
            ((TextView) this.views[2]).setText("");
            ((RecyclerView)this.views[1]).setAdapter(new MoviesAdapter((ArrayList<Movie>) obj));
        } else if (this.type.equals("details")) {
            Movie movie = (Movie) obj;
            Bitmap poster = movie.getPoster();
            if (poster != null)
                ((ImageView) this.views[1]).setImageBitmap(poster);
            else
                ((ImageView) this.views[1]).setImageResource(R.drawable.defaultposter);

            ((TextView) this.views[2]).setText(movie.title);
            ((TextView) this.views[3]).setText(movie.plot);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ProgressBar pb = (ProgressBar) this.views[0];

        if (pb.getVisibility() != View.VISIBLE)
            pb.setVisibility(View.VISIBLE);

        int progress = values[0];

        pb.setProgress(progress);

        if (progress == 100)
            pb.setVisibility(View.INVISIBLE);
    }
}


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

    public JsonTask() {
    }
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

//            Log.v("RAFA", "oii " + data);
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            MovieGson movieGson = gson.fromJson(data, MovieGson.class);

            for (Movie movie: movieGson.movies) {
                movie.setPosterBits();
            }

            /*Log.v("RAFA", ":: " + movieGson.response + " | : " + movieGson.totalResults);
            Log.v("RAFA", "array :: " + movieGson.movies);*/

            /*JsonObject jsonObject = JsonObject.readFrom(data);

            short i = 0;
            List<Movie> movies = new ArrayList<>();
            for (JsonValue item : jsonObject.get("Search").asArray()) {
                publishProgress((++i * 100) / jsonObject.get("Search").asArray().size());

                JsonObject movieObject = item.asObject();

                movies.add(new Movie(movieObject.get("Title").asString(),
                        movieObject.get("Year").asString(),
                        movieObject.get("imdbID").asString(),
                        movieObject.get("Poster").asString()));
            }
            return movies;*/
            return null;
        } else if (this.type.equals("details")) {
            String data = httpClient.getMovieDetails(content);
            /*JsonObject jsonObject = JsonObject.readFrom(data);

            publishProgress(25);

            Movie movie = new Movie(jsonObject.get("Title").asString(), jsonObject.get("Plot").asString(), jsonObject.get("Year").asString(), content, jsonObject.get("Poster").asString());

            publishProgress(100);
            return movie;*/
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        if (this.type.equals("query")) {
            //((RecyclerView)this.views[1]).setAdapter(new MoviesAdapter((ArrayList<Movie>) obj));
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


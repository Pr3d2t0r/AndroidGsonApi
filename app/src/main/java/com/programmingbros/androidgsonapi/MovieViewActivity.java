package com.programmingbros.androidgsonapi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MovieViewActivity extends AppCompatActivity {
    private ImageView image;
    private TextView title, plot;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity);

        this.image = (ImageView) findViewById(R.id.poster);
        this.title = (TextView) findViewById(R.id.title);
        this.plot = (TextView) findViewById(R.id.description);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBarDetails);

        String imdbID = getIntent().getStringExtra("movie_imdbID");

        JsonTask jt = new JsonTask(this.progressBar, this.image, this.title, this.plot);
        jt.execute("details", imdbID);

        /*OMDBHttpClient omdbHttpClient = new OMDBHttpClient();
        Movie movie = omdbHttpClient.getMovieDetails(imdbID);*/
    }
}

package xyz.vrana.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.vrana.popularmovies.api.models.Movie;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title_tv) TextView title;
    @BindView(R.id.poster_iv) ImageView poster;
    @BindView(R.id.overview_tv) TextView plot;
    @BindView(R.id.average_tv) TextView rating;
    @BindView(R.id.releaseDate_tv) TextView releaseDate;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        loadMovieData();
    }

    private void loadMovieData() {
        mMovie = getIntent().getExtras().getParcelable("MOVIE_DATA");

        if (mMovie != null) {
            title.setText(mMovie.getTitle());
            Picasso.get().load(mMovie.getPosterPath()).into(poster);
            rating.setText(String.format(Locale.getDefault(), "%4.1f" , mMovie.getVoteAverage()));
            releaseDate.setText(mMovie.getReleaseDate());
            plot.setText(mMovie.getOverview());
        }
    }

}

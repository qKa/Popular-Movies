package xyz.vrana.popularmovies;

import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.vrana.popularmovies.api.models.Movie;
import xyz.vrana.popularmovies.api.models.Review;
import xyz.vrana.popularmovies.api.models.ReviewsResponse;
import xyz.vrana.popularmovies.api.models.Video;
import xyz.vrana.popularmovies.api.models.VideoResponse;
import xyz.vrana.popularmovies.database.FavoritesContract;
import xyz.vrana.popularmovies.database.QueryHelper;
import xyz.vrana.popularmovies.fragments.ReviewsFragment;
import xyz.vrana.popularmovies.fragments.VideosFragment;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scrollView) ScrollView scrollView;
    @BindView(R.id.title_tv) TextView title;
    @BindView(R.id.poster_iv) ImageView poster;
    @BindView(R.id.overview_tv) TextView plot;
    @BindView(R.id.average_tv) TextView rating;
    @BindView(R.id.releaseDate_tv) TextView releaseDate;
    @BindView(R.id.favorite_ib) ImageButton favorite;

    private Movie mMovie;
    private VideoResponse mVideo;
    private ReviewsResponse mReview;
    private QueryHelper mQueryHelper = new QueryHelper();
    private ContentResolver mContentResolver;
    private Uri mContentURI = FavoritesContract.FavoritesEntry.CONTENT_URI;
    private Bundle mBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        mContentResolver = this.getContentResolver();

        loadMovieData();
    }

    private void loadMovieData() {
        mMovie = getIntent().getExtras().getParcelable("MOVIE_DATA");
        mVideo = getIntent().getExtras().getParcelable("VIDEOS_DATA");
        mReview = getIntent().getExtras().getParcelable("REVIEW_DATA");

        mBundle.putParcelable("videos", mVideo);
        mBundle.putParcelable("reviews", mReview);
        VideosFragment videosFragment = new VideosFragment();
        videosFragment.setArguments(mBundle);
        ReviewsFragment reviewsFragment = new ReviewsFragment();
        reviewsFragment.setArguments(mBundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.videos_fragment, videosFragment);
        ft.replace(R.id.reviews_fragment, reviewsFragment);
        ft.commit();

        if (mMovie != null) {
            title.setText(mMovie.getTitle());
            Picasso.get().load(mMovie.getPosterPath()).into(poster);
            rating.setText(String.format(Locale.getDefault(), "%4.1f / 10" , mMovie.getVoteAverage()));
            releaseDate.setText(mMovie.getReleaseDate());
            plot.setText(mMovie.getOverview());
        }

        if (mQueryHelper.isInDB(FavoritesContract.FavoritesEntry.TABLE_NAME, FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, Integer.toString(mMovie.getId()), getApplicationContext())) {
            favorite.setImageDrawable(getBaseContext().getResources().getDrawable(android.R.drawable.btn_star_big_on));
            favorite.setSelected(true);
        } else {
            favorite.setImageDrawable(getBaseContext().getResources().getDrawable(android.R.drawable.btn_star_big_off));
            favorite.setSelected(false);
        }
    }

    @OnClick(R.id.favorite_ib)
    public void addOrRemoveFromFavoritesDB() {
        if (!favorite.isSelected()) {
            ContentValues values = new ContentValues();

            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, mMovie.getId());
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME, mMovie.getTitle());
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_POSTER_URL, mMovie.getPosterPath());
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie.getReleaseDate());
            values.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_RATING, mMovie.getVoteAverage());
            mContentResolver.insert(mContentURI, values);

            favorite.setImageDrawable(getBaseContext().getResources().getDrawable(android.R.drawable.btn_star_big_on));
            favorite.setSelected(true);
            Snackbar.make(scrollView, "Added to Favorites", Snackbar.LENGTH_SHORT).show();
        } else if (favorite.isSelected()) {
            String selection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
            String[] selectionArgs = {String.valueOf(mMovie.getId())};
            mContentResolver.delete(mContentURI, selection, selectionArgs);

            favorite.setImageDrawable(getBaseContext().getResources().getDrawable(android.R.drawable.btn_star_big_off));
            favorite.setSelected(false);
            Snackbar.make(scrollView, "Removed from Favorites", Snackbar.LENGTH_SHORT).show();
        }
    }

}

package xyz.vrana.popularmovies;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.vrana.popularmovies.adapters.MoviesAdapter;
import xyz.vrana.popularmovies.api.MoviesAPI;
import xyz.vrana.popularmovies.api.TmdbService;
import xyz.vrana.popularmovies.api.models.Movie;
import xyz.vrana.popularmovies.api.models.MoviesResponse;
import xyz.vrana.popularmovies.database.FavoritesContract;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MoviesAdapter recyclerViewAdapter;
    private TmdbService service = MoviesAPI.getClient().create(TmdbService.class);
    private List<Movie> mMoviesList = new ArrayList<>();
    private Parcelable recyclerViewState;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loadingIndicator_pb)
    ProgressBar loadingIndicator;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("MOVIE_LIST_STATE");
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (!checkInternetConnection()) {
            Snackbar.make(mRecyclerView, "No internet connection :(", Snackbar.LENGTH_LONG).show();
        } else {
            initView();
            getMovies("popular");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("MOVIE_LIST_STATE", mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void initView() {
        if (getBaseContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void getMovies(String orderBy) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        loadingIndicator.setVisibility(View.VISIBLE);

        Call<MoviesResponse> call = service.getMovies(orderBy, BuildConfig.TMDB_API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                mRecyclerView.setAdapter(new MoviesAdapter(movies, R.layout.movie_card, getApplicationContext()));
                loadingIndicator.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.d("Error: ", t.toString());
                Snackbar.make(mRecyclerView, "Connection failed to the API :(", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                getMovies("popular");
                break;
            case R.id.action_top_rated:
                getMovies("top_rated");
                break;
            case R.id.action_favorites:
                loadFavoritesFromDB();
            default:
                break;
        }
        return true;
    }

    public void loadFavoritesFromDB() {
        List movies = new ArrayList();
        Uri uri = FavoritesContract.FavoritesEntry.CONTENT_URI;
        Cursor result = getContentResolver().query(uri, null, null, null, null);

        mMoviesList.clear();
        loadingIndicator.setVisibility(View.VISIBLE);

        if (result != null) {
            while (result.moveToNext()) {
                int id = result.getInt(result.getColumnIndex("movieId"));
                String posterPath = result.getString(result.getColumnIndex("posterUrl"));
                String title = result.getString(result.getColumnIndex("movieName"));
                Double rating = result.getDouble(result.getColumnIndex("rating"));
                String releaseDate = result.getString(result.getColumnIndex("releaseDate"));
                movies.add(new Movie(id, title, posterPath, rating, releaseDate));
            }
        }
        result.close();

        mRecyclerView.setAdapter(new MoviesAdapter(movies, R.layout.movie_card, getApplicationContext()));
        mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        mMoviesList.addAll(movies);
        mRecyclerView.setVisibility(View.VISIBLE);
        loadingIndicator.setVisibility(View.INVISIBLE);
    }
}

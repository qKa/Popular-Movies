package xyz.vrana.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import xyz.vrana.popularmovies.BuildConfig;
import xyz.vrana.popularmovies.DetailsActivity;
import xyz.vrana.popularmovies.R;
import xyz.vrana.popularmovies.api.AsyncEventListener;
import xyz.vrana.popularmovies.api.AsyncReviewRequestHandler;
import xyz.vrana.popularmovies.api.AsyncTrailerRequestHandler;
import xyz.vrana.popularmovies.api.MoviesAPI;
import xyz.vrana.popularmovies.api.TmdbService;
import xyz.vrana.popularmovies.api.models.Movie;
import xyz.vrana.popularmovies.api.models.MoviesResponse;
import xyz.vrana.popularmovies.api.models.ReviewsResponse;
import xyz.vrana.popularmovies.api.models.VideoResponse;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    public static String TAG = MoviesAdapter.class.getSimpleName();

    private Context mContext;
    private List<Movie> mMovies;
    private int mRowLayout;
    private TmdbService service = MoviesAPI.getClient().create(TmdbService.class);
    private Intent mIntent;

    public MoviesAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.mMovies = movies;
        this.mRowLayout = rowLayout;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.thumbnail)
        ImageView mThumbnail;

        @BindView(R.id.title)
        TextView mTitle;

        int index;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            mIntent = new Intent(mContext, DetailsActivity.class);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = getAdapterPosition();
                    int id = mMovies.get(index).getId();
                    Log.d(TAG, "Clicked: " + mMovies.get(index).getTitle());
                    getVideos(id);
                    getReviews(id);
                }
            });
        }

        private void getVideos(int id) {
            Call<VideoResponse> call = service.getVideos(id, BuildConfig.TMDB_API_KEY);
            AsyncTrailerRequestHandler requestHandler = new AsyncTrailerRequestHandler(call, mContext, new AsyncEventListener() {
                @Override
                public void onSuccessMovies(List<MoviesResponse> movies) {

                }

                @Override
                public void onSuccessVideos(VideoResponse videos) {
                    Log.d(TAG, "GOT VIDEOS");
                    mIntent.putExtra("VIDEOS_DATA", videos);
                    mIntent.putExtra("MOVIE_DATA", mMovies.get(index));
                }

                @Override
                public void onSuccessReviews(ReviewsResponse reviews) {

                }

                @Override
                public void onFailure(Exception e) {

                }
            });

            requestHandler.execute(call);
        }

        private void getReviews(int id) {
            Call<ReviewsResponse> call = service.getReviews(id, BuildConfig.TMDB_API_KEY);
            AsyncReviewRequestHandler requestHandler = new AsyncReviewRequestHandler(call, mContext, new AsyncEventListener() {
                @Override
                public void onSuccessVideos(VideoResponse videos) {

                }

                @Override
                public void onSuccessReviews(ReviewsResponse reviews) {
                    Log.d(TAG, "GOT REVIEWS");
                    mIntent.putExtra("REVIEW_DATA", reviews);
                    mContext.startActivity(mIntent);
                }

                @Override
                public void onFailure(Exception e) {

                }

                @Override
                public void onSuccessMovies(List movies) {

                }
            });

            requestHandler.execute(call);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ButterKnife.bind(this, view);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(mMovies.get(position).getTitle());

        Picasso.get()
                .load(mMovies.get(position).getPosterPath())
                .placeholder(R.drawable.picasso_loading_placeholder)
                .error(R.drawable.picasso_error_placeholder)
                .into(holder.mThumbnail, new Callback() {
                    @Override
                    public void onSuccess() { }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "Picasso can't load the image.");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}

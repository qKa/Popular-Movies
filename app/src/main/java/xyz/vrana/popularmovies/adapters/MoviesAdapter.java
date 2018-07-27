package xyz.vrana.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.vrana.popularmovies.DetailsActivity;
import xyz.vrana.popularmovies.MyDiffCallback;
import xyz.vrana.popularmovies.R;
import xyz.vrana.popularmovies.api.TmdbService;
import xyz.vrana.popularmovies.api.models.Movie;
import xyz.vrana.popularmovies.api.models.MoviesResponse;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    public static String TAG = MoviesAdapter.class.getSimpleName();

    private Context mContext;
    private List<Movie> mMovies;
    private int mRowLayout;

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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    int id = mMovies.get(index).getId();
                    Log.d(TAG, "Clicked: " + mMovies.get(index).getTitle());

                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra("MOVIE_DATA", mMovies.get(index));
                    mContext.startActivity(intent);
                }
            });
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

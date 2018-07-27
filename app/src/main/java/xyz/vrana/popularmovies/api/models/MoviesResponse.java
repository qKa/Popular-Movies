package xyz.vrana.popularmovies.api.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {

    @SerializedName("page")
    private int mPage;

    @SerializedName("total_results")
    private int mTotalResults;

    @SerializedName("total_pages")
    private int mTotalPages;

    @SerializedName("results")
    private List<Movie> mResults;

    public List<Movie> getResults() {
        return mResults;
    }

}

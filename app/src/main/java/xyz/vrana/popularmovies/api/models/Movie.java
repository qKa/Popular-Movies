package xyz.vrana.popularmovies.api.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import xyz.vrana.popularmovies.R;

public class Movie implements Parcelable {
    @SerializedName("id")
    private Integer mId;

    @SerializedName("original_title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("vote_count")
    private Integer mVoteCount;

    @SerializedName("vote_average")
    private Double mVoteAverage;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("backdrop_path")
    private String mBackdrop;

    @SerializedName("popularity")
    private Double mPopularity;

    public Movie(Integer id, String title, String posterPath, String overview, Double voteAverage, String releaseDate, String backdrop, Double popularity ) {
        this.mId = id;
        this.mTitle = title;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mVoteAverage = voteAverage; // user rating
        this.mReleaseDate = releaseDate;
        this.mBackdrop = backdrop;
        this.mPopularity = popularity;
    }

    public Integer getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w500/";

    public String getPosterPath() {
        if (!(mPosterPath.contains(IMAGE_BASE_URL)))
            return IMAGE_BASE_URL+IMAGE_SIZE+mPosterPath;
        else
            return mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public String getReleaseDate() {

        return mReleaseDate;
    }

    private static final String BACKDROP_URL = "http://image.tmdb.org/t/p/original/";

    public String getBackdrop() {
        if (!(mBackdrop.contains(BACKDROP_URL)))
            return BACKDROP_URL+mBackdrop;
        else
            return mBackdrop;
    }

    private Movie(Parcel source) {
        mId = source.readInt();
        mTitle = source.readString();
        mPosterPath = source.readString();
        mOverview = source.readString();
        mVoteAverage = source.readDouble();
        mReleaseDate = source.readString();
        mBackdrop = source.readString();
        mPopularity = source.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mReleaseDate);
        dest.writeString(mBackdrop);
        dest.writeDouble(mPopularity);
    }
}

package xyz.vrana.popularmovies.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("id")
    private Integer mId;

    @SerializedName("original_title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("vote_average")
    private Double mRating;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("release_date")
    private String mReleaseDate;

    public Movie(Integer id, String title, String posterPath, Double rating, String releaseDate ) {
        this.mId = id;
        this.mTitle = title;
        this.mPosterPath = posterPath;
        this.mRating = rating;
        this.mReleaseDate = releaseDate;
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

    public Double getVoteAverage() {
        return mRating;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {

        return mReleaseDate;
    }

    private Movie(Parcel source) {
        mId = source.readInt();
        mTitle = source.readString();
        mPosterPath = source.readString();
        mRating = source.readDouble();
        mOverview = source.readString();
        mReleaseDate = source.readString();
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
        dest.writeDouble(mRating);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate); }
}

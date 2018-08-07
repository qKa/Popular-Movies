package xyz.vrana.popularmovies.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse implements Parcelable {

    @SerializedName("id")
    private int mId;

    @SerializedName("page")
    private int mPage;

    @SerializedName("results")
    private List<Review> mResults;

    @SerializedName("total_pages")
    private int mTotalPages;

    @SerializedName("total_results")
    private int mTotalResults;

    public int getId() {
        return mId;
    }

    public List<Review> getReviewItemList() {
        return mResults;
    }

    private ReviewsResponse(Parcel in) {
        mId = in.readInt();
        mPage = in.readInt();
        mResults = in.createTypedArrayList(Review.CREATOR);
        mTotalPages = in.readInt();
        mTotalResults = in.readInt();
    }

    public static final Creator<ReviewsResponse> CREATOR = new Creator<ReviewsResponse>() {
        @Override
        public ReviewsResponse createFromParcel(Parcel in) {
            return new ReviewsResponse(in);
        }

        @Override
        public ReviewsResponse[] newArray(int size) {
            return new ReviewsResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mPage);
        dest.writeTypedList(mResults);
        dest.writeInt(mTotalPages);
        dest.writeInt(mTotalResults);
    }
}

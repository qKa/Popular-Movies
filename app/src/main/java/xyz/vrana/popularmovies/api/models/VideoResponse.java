package xyz.vrana.popularmovies.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse implements Parcelable {

    @SerializedName("id")
    private String mId;

    @SerializedName("results")
    private List<Video> mResults;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public List<Video> getResults() {
        return mResults;
    }

    public void setResults(List<Video> results) {
        this.mResults = results;
    }

    protected VideoResponse(Parcel in) {
        mId = in.readString();
        mResults = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<VideoResponse> CREATOR = new Creator<VideoResponse>() {
        @Override
        public VideoResponse createFromParcel(Parcel in) {
            return new VideoResponse(in);
        }

        @Override
        public VideoResponse[] newArray(int size) {
            return new VideoResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeTypedList(mResults);
    }
}

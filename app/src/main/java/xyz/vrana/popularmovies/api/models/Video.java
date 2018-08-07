package xyz.vrana.popularmovies.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {

    @SerializedName("name")
    private String mName;

    @SerializedName("key")
    private String mKey;

    public String getName() {
        return mName;
    }

    public String getKey() {
        return mKey;
    }

    private Video(Parcel in) {
        mName = in.readString();
        mKey = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mKey);
    }
}


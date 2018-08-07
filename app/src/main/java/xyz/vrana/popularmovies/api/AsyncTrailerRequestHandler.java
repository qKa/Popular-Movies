package xyz.vrana.popularmovies.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import xyz.vrana.popularmovies.api.models.VideoResponse;

public class AsyncTrailerRequestHandler extends AsyncTask<Call, Void, VideoResponse> {

    private static final String TAG = AsyncTrailerRequestHandler.class.getSimpleName();

    private AsyncEventListener mAsyncEventListener;
    private Call<VideoResponse> mCall;
    private Context mContext;

    public AsyncTrailerRequestHandler(Call call, Context context, AsyncEventListener eventListener) {
        mCall = call;
        mContext = context;
        mAsyncEventListener = eventListener;
    }

    @Override
    protected VideoResponse doInBackground(Call... params) {
        try {
            mCall = params[0];
            Response<VideoResponse> response = mCall.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(VideoResponse result) {
        if (result != null) {
            if (mAsyncEventListener != null) {
                mAsyncEventListener.onSuccessVideos(result);
            }
        } else {
            Log.d(TAG, "Results empty/null");
        }
    }
}

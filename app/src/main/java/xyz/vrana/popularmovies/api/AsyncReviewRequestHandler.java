package xyz.vrana.popularmovies.api;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import xyz.vrana.popularmovies.api.models.ReviewsResponse;

public class AsyncReviewRequestHandler extends AsyncTask<Call, Void, ReviewsResponse> {

    private AsyncEventListener asyncEventListener;
    private Call<ReviewsResponse> myCall;
    private Context myContext;

    public AsyncReviewRequestHandler(Call call, Context context, AsyncEventListener eventListener) {
        myCall = call;
        myContext = context;
        asyncEventListener = eventListener;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ReviewsResponse doInBackground(Call... params) {
        try {
            myCall = params[0];
            Response<ReviewsResponse> response = myCall.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ReviewsResponse result) {
        if (result != null) {
            if (asyncEventListener != null) {
                asyncEventListener.onSuccessReviews(result);
            }
        } else {
            Toast.makeText(myContext, "No result", Toast.LENGTH_SHORT).show();
        }
    }
}

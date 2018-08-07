package xyz.vrana.popularmovies.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xyz.vrana.popularmovies.api.models.MoviesResponse;
import xyz.vrana.popularmovies.api.models.ReviewsResponse;
import xyz.vrana.popularmovies.api.models.VideoResponse;

public interface TmdbService {

    @GET("movie/{order_by}")
    Call<MoviesResponse> getMovies(@Path("order_by") String order_by, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<VideoResponse> getVideos(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);
}

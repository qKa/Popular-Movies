package xyz.vrana.popularmovies.api;

import java.util.List;

import xyz.vrana.popularmovies.api.models.MoviesResponse;
import xyz.vrana.popularmovies.api.models.ReviewsResponse;
import xyz.vrana.popularmovies.api.models.VideoResponse;

public interface AsyncEventListener {
    void onSuccessMovies(List<MoviesResponse> movies);
    void onSuccessVideos(VideoResponse videos);
    void onSuccessReviews(ReviewsResponse reviews);
    void onFailure(Exception e);
}

package xyz.vrana.popularmovies.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.vrana.popularmovies.R;
import xyz.vrana.popularmovies.api.models.ReviewsResponse;

public class ReviewsFragment extends ListFragment {
    private List mReviewComments = new ArrayList<>();
    private ReviewsResponse mReviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mReviews = getArguments().getParcelable("reviews");
            getReviewItems(mReviews);
        }

        View view = inflater.inflate(R.layout.reviews_fragment, container, false);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mReviewComments);
        setListAdapter(adapter);
    }

    public List<String> getReviewItems(ReviewsResponse reviews) {
        for (int i = 0; i < reviews.getReviewItemList().size(); i++) {
            mReviewComments.add(reviews.getReviewItemList().get(i).getContent());
        }
        return mReviewComments;
    }
}

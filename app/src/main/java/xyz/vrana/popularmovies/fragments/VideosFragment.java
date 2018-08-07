package xyz.vrana.popularmovies.fragments;

import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.vrana.popularmovies.R;
import xyz.vrana.popularmovies.api.models.VideoResponse;

public class VideosFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private Context mContext;
    private List<String> mTitles = new ArrayList<>();
    private VideoResponse mVideoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mVideoList = getArguments().getParcelable("videos");
            getTitles(mVideoList);
        }

        View view = inflater.inflate(R.layout.videos_fragment, container, false);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mTitles);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String videoUrl = mVideoList.getResults().get(i).getKey();
        Log.d("Video url: ", videoUrl);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoUrl));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoUrl));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public List<String> getTitles(VideoResponse videoList) {
        for (int i = 0; i < videoList.getResults().size(); i++) {
            mTitles.add(videoList.getResults().get(i).getName());
        }
        return mTitles;
    }
}


package com.example.root.bakingapp.Fragment;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.bakingapp.Pojo.Step;
import com.example.root.bakingapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

/**
 * Created by root on 1/17/18.
 */

public class DescriptionFragment extends Fragment {
    public final static String KEY_POSITION = "position";
    private static final String SELECTED_POSITION ="vedioPostion" ;
    int mCurrentPosition = -1;
    private SimpleExoPlayer player;
    private SimpleExoPlayerView mPlayerView;


    TextView mVersionDescriptionTextView;
    ArrayList<Step> steps = new ArrayList<>();
    private long positionVideo;
    private Uri videoUri=null;

    public DescriptionFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        mVersionDescriptionTextView = (TextView) view.findViewById(R.id.version_description);
        mPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.player_view);

        // If the Activity is recreated, the savedInstanceStare Bundle isn't empty
        // we restore the previous version name selection set by the Bundle.
        // This is necessary when in two pane layout
        positionVideo = C.TIME_UNSET;
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
            steps = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.steps));
            positionVideo = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
        } else {

            Bundle extra = getArguments();
            Bundle bundle = extra.getBundle(getResources().getString(R.string.bundle));
            steps = bundle.getParcelableArrayList(getResources().getString(R.string.steps));

        }
        return view;
    }
    //-------------------------------------------------------------------------
    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(player);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "Baking app");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }
    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        player.stop();
        player.release();
        player = null;
    }
//
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    //  ------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();

        // During the startup, we check if there are any arguments passed to the fragment.
        // onStart() is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method below
        // that sets the description text
        Bundle args = getArguments();
        if (args != null) {
            setDescription(args.getInt(KEY_POSITION));
            Bundle bundle = args.getBundle(getResources().getString(R.string.bundle));
            steps = bundle.getParcelableArrayList(getResources().getString(R.string.steps));

        } else if (mCurrentPosition != -1) {
            // Set description based on savedInstanceState defined during onCreateView()
            setDescription(mCurrentPosition);
        }
    }

    public void setDescription(int descriptionIndex) {
        mVersionDescriptionTextView.setText(steps.get(descriptionIndex).getShortDescription());
        mCurrentPosition = descriptionIndex;
        videoUri=Uri.parse(steps.get(descriptionIndex).getVideoURL());
        initializePlayer(videoUri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current description selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION, mCurrentPosition);
        outState.putParcelableArrayList(getResources().getString(R.string.steps), steps);
        positionVideo = player.getCurrentPosition(); //then, save it on the bundle.
        outState.putLong(SELECTED_POSITION, positionVideo);

    }



}
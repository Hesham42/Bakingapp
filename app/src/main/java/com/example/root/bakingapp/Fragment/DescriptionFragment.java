package com.example.root.bakingapp.Fragment;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.bakingapp.Pojo.Recipe;
import com.example.root.bakingapp.Pojo.Step;
import com.example.root.bakingapp.R;
import com.example.root.bakingapp.Service.COMM;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * Created by root on 1/17/18.
 */

public class DescriptionFragment extends Fragment {

    public final static String KEY_POSITION = "position";
    int mCurrentPosition = -1;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer player;
    Handler mainHandler;
    TrackSelection.Factory videoTrackSelectionFactory;
    TrackSelector trackSelector;
    LoadControl loadControl;
    DataSource.Factory dataSourceFactory;
    MediaSource videoSource;
    Uri uri;
    String userAgent;
    static final DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    private static final String VIDEO_URI =
            "http://www-itec.uni-klu.ac.at/ftp/datasets/DASHDataset2014/BigBuckBunny/4sec/BigBuckBunny_4s_onDemand_2014_05_09.mpd";


    TextView mVersionDescriptionTextView;
    ArrayList<Step> steps = new ArrayList<>();

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
        simpleExoPlayerView = (SimpleExoPlayerView)view.findViewById(R.id.player_view);

        // If the Activity is recreated, the savedInstanceStare Bundle isn't empty
        // we restore the previous version name selection set by the Bundle.
        // This is necessary when in two pane layout
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
            steps = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.steps));

        } else {

            Bundle extra = getArguments();
            Bundle bundle = extra.getBundle(getResources().getString(R.string.bundle));
            steps = bundle.getParcelableArrayList(getResources().getString(R.string.steps));

        }
        createPlayer();
        attachPlayerView();
        preparePlayer();
        return view;
    }


    //-------------------------------------------------------------------------
// Create TrackSelection Factory, Track Selector, Handler, Load Control, and ExoPlayer Instance
    public void createPlayer() {
        mainHandler = new Handler();
        videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        loadControl = new DefaultLoadControl();
        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
    }

    // Set player to SimpleExoPlayerView
    public void attachPlayerView() {
        simpleExoPlayerView.setPlayer(player);
    }

    // Build Data Source Factory, Dash Media Source, and Prepare player using videoSource
    public void preparePlayer() {
        uriParse();
        dataSourceFactory = buildDataSourceFactory(bandwidthMeter);
        videoSource = new DashMediaSource(uri, buildDataSourceFactory(null), new DefaultDashChunkSource.Factory(dataSourceFactory), mainHandler, null);
        player.prepare(videoSource);
    }

    // Parse VIDEO_URI and Save at uri variable
    public void uriParse() {
        uri = Uri.parse(VIDEO_URI);
    }

    // Build Data Source Factory using DefaultBandwidthMeter and HttpDataSource.Factory
    private DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(getActivity(), bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    // Build Http Data Source Factory using DefaultBandwidthMeter
    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    // Activity onStop, player must be release because of memory saving
    @Override
    public void onStop() {
        super.onStop();
        player.release();
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
        userAgent = Util.getUserAgent(getActivity(),"SimpleDashExoPlayer");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current description selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION, mCurrentPosition);
        outState.putParcelableArrayList(getResources().getString(R.string.steps), steps);

    }


}

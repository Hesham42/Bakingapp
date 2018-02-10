
package com.example.root.bakingapp.Fragment;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;

import static android.view.View.GONE;

/**
 * Created by root on 1/17/18.
 */

public class DescriptionFragment extends Fragment {
    public final static String KEY_POSITION = "position";
    private static final String SELECTED_POSITION = "vedioPostion";
    int mCurrentPosition = -1;
    private SimpleExoPlayer player;
    private SimpleExoPlayerView mPlayerView;
    ImageView ImgthumbnailUrl;
    TextView current;
    FloatingActionButton next;
    FloatingActionButton back;
    TextView mVersionDescriptionTextView;
    ArrayList<Step> steps = new ArrayList<>();
    private long positionVideo;


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
        next = view.findViewById(R.id.next_button);
        back = view.findViewById(R.id.back_button);
        current = view.findViewById(R.id.currentStep);
        ImgthumbnailUrl = view.findViewById(R.id.thumbnailUrl);
        positionVideo = C.TIME_UNSET;
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
            steps = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.steps));
            positionVideo = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
        } else {
            try {
                Bundle extra = getArguments();

                Bundle bundle = extra.getBundle(getResources().getString(R.string.bundle));
                mCurrentPosition = extra.getInt(KEY_POSITION);
                steps = bundle.getParcelableArrayList(getResources().getString(R.string.steps));


            }catch (Exception e){
                Log.e("guinness","in the Descirption"+e);
            }

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition == 0 || mCurrentPosition == -1) {

                } else {
                    mCurrentPosition--;
                    View_fun();
                }

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition == steps.size() - 1) {

                } else {

                    mCurrentPosition++;
                    View_fun();

                }
            }
        });

        View_fun();
        return view;
    }

    private void View_fun() {
        if (mCurrentPosition <= 0) {
            back.setVisibility(GONE);
        } else {
            back.setVisibility(View.VISIBLE);
        }
        if (mCurrentPosition >= steps.size() - 1) {
            next.setVisibility(View.GONE);
        } else {
            next.setVisibility(View.VISIBLE);
        }
        releasePlayer();
        if (mCurrentPosition==-1){
            Log.e("guinness","Description with postion -1");

        }else{
            if (steps.get(mCurrentPosition).getVideoURL().isEmpty()
                    && steps.get(mCurrentPosition).getThumbnailURL().isEmpty()) {
                mPlayerView.setVisibility(GONE);
                ImgthumbnailUrl.setVisibility(GONE);
            } else if (!steps.get(mCurrentPosition).getVideoURL().isEmpty()) {
                String videoUrl = steps.get(mCurrentPosition).getVideoURL();
                ImgthumbnailUrl.setVisibility(GONE);
                mPlayerView.setVisibility(View.VISIBLE);
                initializePlayer(Uri.parse(videoUrl));

            } else {
                String imageUrl = steps.get(mCurrentPosition).getThumbnailURL();
                mPlayerView.setVisibility(GONE);
                ImgthumbnailUrl.setVisibility(View.VISIBLE);

                Glide.with(getActivity())
                        .load(imageUrl)
                        .into(ImgthumbnailUrl);
            }
            mVersionDescriptionTextView.setText(steps.get(mCurrentPosition).getShortDescription());
            current.setText((mCurrentPosition + 1) + "/" + steps.size());
        }

    }

    //-------------------------------------------------------------------------
    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(player);
            player.seekTo(positionVideo);
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
        if (player != null) {
            player.release();
            player = null;
        }
    }


    @Override
    public void onStop() {
        super.onStop();
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
            mCurrentPosition = args.getInt(KEY_POSITION);
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
        View_fun();

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
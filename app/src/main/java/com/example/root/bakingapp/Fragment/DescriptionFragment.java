package com.example.root.bakingapp.Fragment;


import android.app.Fragment;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

/**
 * Created by root on 1/17/18.
 */

public class DescriptionFragment extends Fragment {

    public final static String KEY_POSITION = "position";
    int mCurrentPosition = -1;

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

        // If the Activity is recreated, the savedInstanceStare Bundle isn't empty
        // we restore the previous version name selection set by the Bundle.
        // This is necessary when in two pane layout
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(KEY_POSITION);
            steps = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.steps));

        } else {

            Bundle extra = getArguments();
            Bundle bundle =extra.getBundle(getResources().getString(R.string.bundle));
            steps = bundle.getParcelableArrayList(getResources().getString(R.string.steps));

        }

        return view;
    }


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
            Bundle bundle =args.getBundle(getResources().getString(R.string.bundle));
            steps = bundle.getParcelableArrayList(getResources().getString(R.string.steps));

        } else if (mCurrentPosition != -1) {
            // Set description based on savedInstanceState defined during onCreateView()
            setDescription(mCurrentPosition);
        }
    }

    public void setDescription(int descriptionIndex) {
        mVersionDescriptionTextView.setText(steps.get(descriptionIndex).getShortDescription());
        mCurrentPosition = descriptionIndex;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current description selection in case we need to recreate the fragment
        outState.putInt(KEY_POSITION, mCurrentPosition);
        outState.putParcelableArrayList(getResources().getString(R.string.steps), steps);

    }


}

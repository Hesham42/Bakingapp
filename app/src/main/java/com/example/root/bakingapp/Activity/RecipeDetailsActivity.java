package com.example.root.bakingapp.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.root.bakingapp.Fragment.DescriptionFragment;
import com.example.root.bakingapp.Pojo.Ingredient;
import com.example.root.bakingapp.Pojo.Step;
import com.example.root.bakingapp.R;
import com.example.root.bakingapp.Utilites.OnVersionNameSelectionChangeListener;
import com.example.root.bakingapp.Fragment.StepFragment;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity
        implements OnVersionNameSelectionChangeListener {
    ArrayList<Step> steps = new ArrayList<>();
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    public StepFragment stepFragment;
    DescriptionFragment descriptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra(getResources().getString(R.string.bundle));
        steps = bundle.getParcelableArrayList(getResources().getString(R.string.steps));
        ingredients = bundle.getParcelableArrayList(getResources().getString(R.string.ingredients));
        String name = bundle.getString(getResources().getString(R.string.recipe_name));
        android.support.v7.app.ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Check whether the Activity is using the layout verison with the fragment_container
        // FrameLayout and if so we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {
            // However if we are being restored from a previous state, then we don't
            // need to do anything and should return or we could end up with overlapping Fragments
            if (savedInstanceState != null) {
                return;
            }
            stepFragment = new StepFragment();
            Bundle arg = getIntent().getBundleExtra(getResources().getString(R.string.bundle));
            if (arg != null) {
                stepFragment.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, stepFragment)
                        .commit();
            } else {
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList(getResources().getString(R.string.steps),
                        (ArrayList<? extends Parcelable>) steps);
                bundle1.putParcelableArrayList(getResources().getString(R.string.ingredients),
                        (ArrayList<? extends Parcelable>) ingredients);

                stepFragment.setArguments(bundle1);
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, stepFragment)
                        .commit();
            }

        }else {
            Log.e("guinness","the Fragment Container == null in RecipeDetailsActivity");
            stepFragment = new StepFragment();
            Bundle arg = getIntent().getBundleExtra(getResources().getString(R.string.bundle));
            if (arg != null) {
                stepFragment.setArguments(arg);

                getFragmentManager().beginTransaction()
                        .add(R.id.names_fragment, stepFragment)
                        .commit();

            }else {
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList(getResources().getString(R.string.steps),
                        (ArrayList<? extends Parcelable>) steps);
                bundle1.putParcelableArrayList(getResources().getString(R.string.ingredients),
                        (ArrayList<? extends Parcelable>) ingredients);

                stepFragment.setArguments(bundle1);
                getFragmentManager().beginTransaction()
                        .add(R.id.names_fragment, stepFragment)
                        .commit();
            }
        }
    }


    @Override
    public void OnSelectionChanged(int versionNameIndex) {
        descriptionFragment = (DescriptionFragment)
                getFragmentManager()
                        .findFragmentById(R.id.description_fragment);

        if (descriptionFragment != null)
        {
            // If description is available, we are in two pane layout
            // so we call the method in DescriptionFragment to update its content
            Log.e("guinness","the descriptionFragment != null in RecipeDetailsActivity");

            stepFragment.setRecipes(versionNameIndex, steps, ingredients);
            descriptionFragment.setDescription(versionNameIndex,steps);
        }
        else
        {
            Log.e("guinness","the descriptionFragment == null in RecipeDetailsActivity");
            descriptionFragment = (DescriptionFragment)
                    getFragmentManager()
                            .findFragmentById(R.id.description_fragment);
            Bundle args = new Bundle();
            args.putInt(DescriptionFragment.KEY_POSITION, versionNameIndex);
            args.putBundle(getResources().getString(R.string.bundle), getIntent().getBundleExtra(getResources().getString(R.string.bundle)));
            if (args != null) {
                descriptionFragment.setArguments(args);
        }
        else {
                Bundle bundle = new Bundle();
                bundle.putInt(DescriptionFragment.KEY_POSITION, versionNameIndex);
                bundle.putParcelableArrayList(getResources().getString(R.string.steps),
                        (ArrayList<? extends Parcelable>) steps);
                descriptionFragment.setArguments(bundle);

            }

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the backStack so the User can navigate back
            fragmentTransaction.replace(R.id.fragment_container, descriptionFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getResources().getString(R.string.steps), steps);
        outState.putParcelableArrayList(getResources().getString(R.string.ingredients), ingredients);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        steps = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.steps));
        ingredients = savedInstanceState.getParcelableArrayList(getResources().getString(R.string.ingredients));
    }
}

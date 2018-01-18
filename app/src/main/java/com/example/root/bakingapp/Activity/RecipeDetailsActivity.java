package com.example.root.bakingapp.Activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.root.bakingapp.Fragment.DescriptionFragment;
import com.example.root.bakingapp.Pojo.Ingredient;
import com.example.root.bakingapp.Pojo.Step;
import com.example.root.bakingapp.R;
import com.example.root.bakingapp.Utilites.OnVersionNameSelectionChangeListener;
import com.example.root.bakingapp.Fragment.StepFragment;

import java.util.ArrayList;

public class RecipeDetailsActivity extends Activity implements OnVersionNameSelectionChangeListener {
    ArrayList<Step> steps=new ArrayList<>();
    ArrayList<Ingredient> ingredients= new ArrayList<>();;
    public StepFragment stepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();
        Bundle bundle = new Bundle();
        bundle = getIntent().getBundleExtra(getResources().getString(R.string.bundle));
        if (bundle != null) {
            steps = bundle.getParcelableArrayList(getResources().getString(R.string.steps));
            ingredients = bundle.getParcelableArrayList(getResources().getString(R.string.ingredients));

        }
        else
        {
            Log.d("guinness","bundel==null");
        }
        // Check whether the Activity is using the layout verison with the fragment_container
        // FrameLayout and if so we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However if we are being restored from a previous state, then we don't
            // need to do anything and should return or we could end up with overlapping Fragments
            if (savedInstanceState != null) {

                return;
            }

            // Create an Instance of Fragment
            stepFragment =new StepFragment();
            stepFragment.setArguments(getIntent().getBundleExtra(getResources().getString(R.string.bundle)));
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, stepFragment)
                    .commit();
        }
    }


    @Override
    public void OnSelectionChanged(int versionNameIndex) {
        DescriptionFragment descriptionFragment = (DescriptionFragment) getFragmentManager()
                .findFragmentById(R.id.description_fragment);

        if (descriptionFragment != null) {
            // If description is available, we are in two pane layout
            // so we call the method in DescriptionFragment to update its content
            stepFragment.setRecipes(versionNameIndex,steps,ingredients);
            descriptionFragment.setDescription(versionNameIndex);
        } else {
            DescriptionFragment newDesriptionFragment = new DescriptionFragment();
            Bundle args = new Bundle();
            args.putInt(DescriptionFragment.KEY_POSITION, versionNameIndex);
            args.putBundle(getResources().getString(R.string.bundle), getIntent().getBundleExtra(getResources().getString(R.string.bundle)));
            newDesriptionFragment.setArguments(args);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the backStack so the User can navigate back
            fragmentTransaction.replace(R.id.fragment_container, newDesriptionFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}

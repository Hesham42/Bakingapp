package com.example.root.bakingapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import com.example.root.bakingapp.Activity.HomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.root.bakingapp.TestUtils.withRecyclerView;

/**
 * Created by root on 1/19/18.
 */

public class RecyclerViewTest  extends ActivityInstrumentationTestCase2<HomeActivity> {


    public RecyclerViewTest() {
        super(HomeActivity.class);
    }

    @Override protected void setUp() throws Exception {

        getActivity();
    }
    public void testItemClick() {
        onView(withRecyclerView(R.id.recipesList).atPositionOnView(0, R.id.recipe_steps_count))
                .check(matches(withText("7")));
        onView(withRecyclerView(R.id.recipesList).atPositionOnView(0, R.id.recipe_name))
                .check(matches(withText("Nutella Pie")));
        onView(withRecyclerView(R.id.recipesList).atPositionOnView(0, R.id.recipe_servings))
                .check(matches(withText("8")));
        onView(ViewMatchers.withId(R.id.recipesList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
    }

    public void testFollowButtonClick() {
        onView(withId(R.id.ingredientList)).check(matches(isDisplayed()));
        onView(withId(R.id.stepsList)).check(matches(isDisplayed()));


        onView(ViewMatchers.withId(R.id.stepsList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        onView(withId(R.id.version_description)).check(matches(isDisplayed()));
    }

}

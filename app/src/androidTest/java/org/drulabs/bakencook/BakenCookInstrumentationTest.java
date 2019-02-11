package org.drulabs.bakencook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;

import org.drulabs.bakencook.ui.home.HomeActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.Intents;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4ClassRunner.class)
public class BakenCookInstrumentationTest {

    @Rule
    public ActivityTestRule<HomeActivity> rule = new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testRecipeSearchHeaderTextDisplayWithNavigation() {
        onView(withId(R.id.action_search)).perform(click());

        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText(
                "lasagna"), pressImeActionButton());

        onView(withId(R.id.tv_search_text)).check(matches(withText("lasagna (1)")));

        onView(withId(R.id.tv_next)).perform(click());

        onView(withId(R.id.tv_search_text)).check(matches(withText("lasagna (2)")));

        onView(withId(R.id.tv_previous)).perform(click());

        onView(withId(R.id.tv_search_text)).check(matches(withText("lasagna (1)")));

        onView(withId(R.id.tv_previous)).perform(click());

        onView(withId(R.id.tv_search_text)).check(matches(withText("lasagna (1)")));
    }

    @Test
    public void testRecipeFavoritedAndDisplayed() {

        String recipeName = "Chicken Caesar Pasta Salad";

        String favoriteMenuItem = rule.getActivity().getString(R.string.txt_favorites);

        onView(allOf(withId(R.id.img_star), hasSibling(withText(recipeName)))).perform(click());

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry
                .getInstrumentation().getContext());

        onView(withText(favoriteMenuItem)).perform(click());

        onView(allOf(withId(R.id.tvRecipeName), hasSibling(withText(recipeName))))
                .check(matches(withText(recipeName)));
        onView(allOf(withId(R.id.img_star), hasSibling(withText(recipeName)))).perform(click());

        onView(withId(R.id.tv_error_favorites)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeImageClickIntent() {

        String recipeName = "Chicken Pesto Pasta Salad";
        String actionURL = "http://www.recipezaar.com/Chicken-Pesto-Pasta-Salad-300402";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(actionURL));

        intending(allOf(hasData(Uri.parse(actionURL)), hasAction(Intent.ACTION_VIEW)))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

        onView(allOf(withId(R.id.img_recipe), hasSibling(withText(recipeName)))).perform(click());

        intended(allOf(hasData(Uri.parse(actionURL)), hasAction(Intent.ACTION_VIEW)));
    }

    /*
     *         Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
     *         RecipeDatabase recipeDatabase = Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class)
     *                 .allowMainThreadQueries()
     *                 .build();
     *
     *         LocalDataSource localDataSource = new LocalDataSourceImpl(new PersistenceDataMapper(),
     *                 recipeDatabase.getRecipeDao());
     *         RemoteDataSource remoteDataSource = new NetworkDataSource(new NetworkDataMapper(),
     *                 new FakeRecipeRetrofitService());
     *
     *         RecipeRepository recipeRepository = new RecipeRepositoryImpl(new DataDomainMapper(),
     *                 localDataSource, remoteDataSource);
     *
     *         HomeVMFactory factory = new HomeVMFactory(
     *                 new PresentationDomainMapper(),
     *                 new SaveRecipeTask(recipeRepository, Schedulers.trampoline(),
     *                         Schedulers.trampoline()),
     *                 new DeleteRecipeTask(recipeRepository, Schedulers.trampoline(),
     *                         Schedulers.trampoline()),
     *                 new GetRecipesTask(recipeRepository, Schedulers.trampoline(),
     *                         Schedulers.trampoline())
     *         );
     */
}

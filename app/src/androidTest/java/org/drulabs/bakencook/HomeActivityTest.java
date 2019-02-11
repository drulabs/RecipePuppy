package org.drulabs.bakencook;

import android.content.Context;

import org.drulabs.bakencook.ui.home.HomeActivity;
import org.drulabs.bakencook.utils.FakeRecipeRetrofitService;
import org.drulabs.data.mapper.DataDomainMapper;
import org.drulabs.data.repository.LocalDataSource;
import org.drulabs.data.repository.RecipeRepositoryImpl;
import org.drulabs.data.repository.RemoteDataSource;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.usecases.DeleteRecipeTask;
import org.drulabs.domain.usecases.GetRecipesTask;
import org.drulabs.domain.usecases.SaveRecipeTask;
import org.drulabs.network.mapper.NetworkDataMapper;
import org.drulabs.network.source.NetworkDataSource;
import org.drulabs.persistence.db.RecipeDatabase;
import org.drulabs.persistence.mapper.PersistenceDataMapper;
import org.drulabs.persistence.source.LocalDataSourceImpl;
import org.drulabs.presentation.factory.HomeVMFactory;
import org.drulabs.presentation.mapper.PresentationDomainMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.room.Room;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import io.reactivex.schedulers.Schedulers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)
public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> rule =
            new ActivityTestRule<HomeActivity>(HomeActivity.class) {
                @Override
                protected void beforeActivityLaunched() {
                    super.beforeActivityLaunched();
                }
            };

    @Before
    public void setUp() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        RecipeDatabase recipeDatabase = Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class)
                .allowMainThreadQueries()
                .build();

        LocalDataSource localDataSource = new LocalDataSourceImpl(new PersistenceDataMapper(),
                recipeDatabase.getRecipeDao());
        RemoteDataSource remoteDataSource = new NetworkDataSource(new NetworkDataMapper(),
                new FakeRecipeRetrofitService());

        RecipeRepository recipeRepository = new RecipeRepositoryImpl(new DataDomainMapper(),
                localDataSource, remoteDataSource);

        HomeVMFactory factory = new HomeVMFactory(
                new PresentationDomainMapper(),
                new SaveRecipeTask(recipeRepository, Schedulers.trampoline(),
                        Schedulers.trampoline()),
                new DeleteRecipeTask(recipeRepository, Schedulers.trampoline(),
                        Schedulers.trampoline()),
                new GetRecipesTask(recipeRepository, Schedulers.trampoline(),
                        Schedulers.trampoline())
        );
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
    }

    @Test
    public void testRecipeFavoritedAndDisplayed() {
        onView(withId(R.id.rvRecipeList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//        onData(allOf(is(instanceOf(List.class)), hasEntry()))
//                .perform(click());
    }

}

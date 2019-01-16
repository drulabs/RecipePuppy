package org.drulabs.persistence;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.drulabs.persistence.db.RecipeDao;
import org.drulabs.persistence.db.RecipeDatabase;
import org.drulabs.persistence.entities.DBRecipe;
import org.drulabs.persistence.utils.Generator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

@RunWith(AndroidJUnit4.class)
public class LocalDataSourceTest {

    private RecipeDatabase recipeDatabase;
    private RecipeDao dao;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getContext();
        recipeDatabase = Room.inMemoryDatabaseBuilder(context, RecipeDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = recipeDatabase.getRecipeDao();
    }

    @After
    public void tearDown() {
        recipeDatabase.close();
    }

    @Test
    public void recipeReadWriteTest() {
        DBRecipe dbRecipe = Generator.generateRecipe("myRecipe");

        dao.saveRecipe(dbRecipe);

        TestSubscriber<DBRecipe> testSubscriber = dao.getSavedRecipes().test();

        testSubscriber.assertValue(
                recipe -> recipe.getRecipeName().equals(dbRecipe.getRecipeName())
                        && recipe.getCreateTime() == dbRecipe.getCreateTime()
        );
    }

    @Test
    public void lookupRecipeTest() {
        DBRecipe dbRecipe = Generator.generateRecipe("111");

        dao.saveRecipe(dbRecipe);

        TestObserver<DBRecipe> testObserver = dao.lookupRecipe("Test111").test();

        testObserver.assertValue(dbRecipe);
    }

}

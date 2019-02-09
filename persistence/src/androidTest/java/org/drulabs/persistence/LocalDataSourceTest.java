package org.drulabs.persistence;

import android.content.Context;

import org.drulabs.persistence.db.RecipeDao;
import org.drulabs.persistence.db.RecipeDatabase;
import org.drulabs.persistence.entities.DBRecipe;
import org.drulabs.persistence.utils.Generator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import io.reactivex.observers.TestObserver;

@RunWith(AndroidJUnit4ClassRunner.class)
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
        DBRecipe dbRecipe1 = Generator.generateRecipe("111");
        DBRecipe dbRecipe2 = Generator.generateRecipe("222");
        DBRecipe dbRecipe3 = Generator.generateRecipe("333");

        dao.saveRecipe(dbRecipe1, dbRecipe2, dbRecipe3);

        TestObserver<List<DBRecipe>> testObserver = dao.getSavedRecipes().test();

        testObserver.assertValue(dbRecipes -> dbRecipes.contains(dbRecipe1) &&
                dbRecipes.contains(dbRecipe2) &&
                dbRecipes.contains(dbRecipe3));
        testObserver.assertNotComplete();
    }

    @Test
    public void success_lookupRecipeTest() {
        DBRecipe dbRecipe1 = Generator.generateRecipe("111");
        DBRecipe dbRecipe2 = Generator.generateRecipe("222");
        DBRecipe dbRecipe3 = Generator.generateRecipe("333");

        dao.saveRecipe(dbRecipe1, dbRecipe2, dbRecipe3);

        TestObserver<DBRecipe> testObserver = dao.lookupRecipe("Test222").test();

        testObserver.assertValue(dbRecipe -> dbRecipe.equals(dbRecipe2));
    }

    @Test
    public void failure_lookupRecipeTest() {
        DBRecipe dbRecipe1 = Generator.generateRecipe("111");
        DBRecipe dbRecipe2 = Generator.generateRecipe("222");
        DBRecipe dbRecipe3 = Generator.generateRecipe("333");

        dao.saveRecipe(dbRecipe1, dbRecipe2, dbRecipe3);

        TestObserver<DBRecipe> testObserver = dao.lookupRecipe("Test000").test();

        testObserver.assertNoValues();
    }

    @Test
    public void success_lastSavedRecipeTest() {
        DBRecipe dbRecipe1 = Generator.generateRecipe("111", 111);
        DBRecipe dbRecipe2 = Generator.generateRecipe("222", 222);
        DBRecipe dbRecipe3 = Generator.generateRecipe("333", 333);

        dao.saveRecipe(dbRecipe1, dbRecipe2, dbRecipe3);

        TestObserver<DBRecipe> testObserver = dao.getLastSavedRecipe().test();

        testObserver.assertValue(dbRecipe -> {
            System.out.println(dbRecipe);
            return dbRecipe.equals(dbRecipe3);
        });
    }

    @Test
    public void failure_lastSavedRecipeTest() {
        DBRecipe dbRecipe1 = Generator.generateRecipe("111", 111);
        DBRecipe dbRecipe2 = Generator.generateRecipe("222", 222);
        DBRecipe dbRecipe3 = Generator.generateRecipe("333", 333);

        dao.saveRecipe(dbRecipe1, dbRecipe2, dbRecipe3);

        TestObserver<DBRecipe> testObserver = dao.getLastSavedRecipe().test();

        testObserver.assertValue(dbRecipe -> !dbRecipe.equals(dbRecipe1));
    }

}

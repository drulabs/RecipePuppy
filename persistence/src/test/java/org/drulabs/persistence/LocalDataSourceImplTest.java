package org.drulabs.persistence;

import org.drulabs.data.entities.DataRecipe;
import org.drulabs.data.repository.LocalDataSource;
import org.drulabs.persistence.db.RecipeDao;
import org.drulabs.persistence.entities.DBRecipe;
import org.drulabs.persistence.mapper.PersistenceDataMapper;
import org.drulabs.persistence.mapper.PersistenceMapper;
import org.drulabs.persistence.source.LocalDataSourceImpl;
import org.drulabs.persistence.utils.Generator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class LocalDataSourceImplTest {

    @Mock
    private RecipeDao dao;

    private PersistenceMapper<DataRecipe> mapper = new PersistenceDataMapper();
    private LocalDataSource localDataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        localDataSource = new LocalDataSourceImpl(mapper, dao);
    }

    @Test
    public void testGetSavedRecipes() {
        DBRecipe dbRecipe = Generator.generateRecipe("01");
        DataRecipe dataRecipe = mapper.mapTo(dbRecipe);
        List<DBRecipe> dbRecipes = new ArrayList<>();
        dbRecipes.add(dbRecipe);
        when(dao.getSavedRecipes()).thenReturn(Observable.just(dbRecipes));

        TestObserver<List<DataRecipe>> testObserver = localDataSource.getSavedRecipes().test();
        testObserver.assertValue(
                recipes -> recipes.get(0).getName().equalsIgnoreCase(dataRecipe.getName())
                        && recipes.get(0).getDetailsUrl().equalsIgnoreCase(dataRecipe.getDetailsUrl())
        );

        verify(dao, times(1)).getSavedRecipes();
    }

    @Test
    public void testSaveRecipe() {
        DBRecipe dbRecipe = Generator.generateRecipe("01");
        DataRecipe dataRecipe = mapper.mapTo(dbRecipe);

        TestObserver testObserver = localDataSource.saveRecipe(dataRecipe).test();
        testObserver.assertComplete();

        verify(dao, times(1)).saveRecipe(dbRecipe);
    }

    @Test
    public void testDeleteRecipe() {
        DBRecipe dbRecipe = Generator.generateRecipe("01");
        DataRecipe dataRecipe = mapper.mapTo(dbRecipe);

        TestObserver testObserver = localDataSource.deleteRecipe(dataRecipe).test();
        testObserver.assertComplete();

        verify(dao, times(1)).deleteRecipe(dbRecipe);
    }

    @Test
    public void testDeleteAllRecipes() {
        TestObserver testObserver = localDataSource.deleteAllRecipes().test();
        testObserver.assertComplete();

        verify(dao, times(1)).deleteAllRecipes();
    }

    @Test
    public void testGetLastSavedRecipe() {
        DBRecipe dbRecipe = Generator.generateRecipe("01");
        DataRecipe dataRecipe = mapper.mapTo(dbRecipe);

        when(dao.getLastSavedRecipe()).thenReturn(Single.just(dbRecipe));

        TestObserver<DataRecipe> testObserver = localDataSource.getLastSavedRecipe().test();
        testObserver.assertValue(recipe ->
                recipe.getName().equalsIgnoreCase(dataRecipe.getName())
                        && recipe.getDetailsUrl().equalsIgnoreCase(dataRecipe.getDetailsUrl())
        );

        verify(dao, times(1)).getLastSavedRecipe();
    }

    @Test
    public void testRecipeSearch() {
        DBRecipe dbRecipe = Generator.generateRecipe("00");
        DataRecipe dataRecipe = mapper.mapTo(dbRecipe);

        when(dao.lookupRecipe("Test00")).thenReturn(Single.just(dbRecipe));

        TestObserver<DataRecipe> testObserver = localDataSource.lookupRecipe("Test00").test();
        testObserver.assertValue(recipe ->
                recipe.getName().equalsIgnoreCase(dataRecipe.getName())
                        && recipe.getDetailsUrl().equalsIgnoreCase(dataRecipe.getDetailsUrl())
        );

        verify(dao, times(1)).lookupRecipe("Test00");
    }

}

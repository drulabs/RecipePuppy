package org.drulabs.data.repository;

import org.drulabs.data.entity.DataRecipe;
import org.drulabs.data.mapper.DomainMapper;
import org.drulabs.data.mapper.DomainDataMapper;
import org.drulabs.data.utils.Generator;
import org.drulabs.domain.entities.Recipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class DataRecipeRepositoryTest {

    private DomainMapper<Recipe> mapper = new DomainDataMapper();
    private RecipeRepository recipeRepository;

    @Mock
    private LocalDataSource localDataSource;
    @Mock
    private RemoteDataSource remoteDataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        recipeRepository = new RecipeRepositoryImpl(mapper, localDataSource,
                remoteDataSource);
    }

    @Test
    public void testGetRecipes() {
        DataRecipe dataRecipe1 = Generator.generateDomainRecipe();
        DataRecipe dataRecipe2 = Generator.generateDomainRecipe();
        DataRecipe dataRecipe3 = Generator.generateDomainRecipe();

        String searchQuery = "query text";
        int pageNum = 1;

        when(remoteDataSource.getRecipes(searchQuery, pageNum))
                .thenReturn(Observable.just(dataRecipe1, dataRecipe2, dataRecipe3));

        TestObserver<Recipe> testObserver = recipeRepository.getRecipes(searchQuery, pageNum)
                .test();

        testObserver.assertValues(
                mapper.mapTo(dataRecipe1),
                mapper.mapTo(dataRecipe2),
                mapper.mapTo(dataRecipe3)
        );

        verify(remoteDataSource, times(1))
                .getRecipes(searchQuery, pageNum);
    }

    @Test
    public void testGetSavedRecipes() {
        DataRecipe dataRecipe1 = Generator.generateDomainRecipe();
        DataRecipe dataRecipe2 = Generator.generateDomainRecipe();
        DataRecipe dataRecipe3 = Generator.generateDomainRecipe();

        when(localDataSource.getSavedRecipes())
                .thenReturn(Observable.just(dataRecipe1, dataRecipe2, dataRecipe3));

        TestObserver<Recipe> testObserver = recipeRepository.getSavedRecipes().test();

        testObserver.assertValues(
                mapper.mapTo(dataRecipe1),
                mapper.mapTo(dataRecipe2),
                mapper.mapTo(dataRecipe3)
        );

        verify(localDataSource, times(1)).getSavedRecipes();
    }

    @Test
    public void testSaveRecipe() {
        DataRecipe dataRecipe = Generator.generateDomainRecipe();
        Recipe recipe = mapper.mapTo(dataRecipe);

        when(localDataSource.saveRecipe(dataRecipe))
                .thenReturn(Completable.complete());

        TestObserver testObserver = recipeRepository.saveRecipe(recipe).test();

        testObserver.assertComplete();

        verify(localDataSource, times(1)).saveRecipe(dataRecipe);
    }

    @Test
    public void testDeleteRecipe() {
        DataRecipe dataRecipe = Generator.generateDomainRecipe();
        Recipe recipe = mapper.mapTo(dataRecipe);

        when(localDataSource.deleteRecipe(dataRecipe))
                .thenReturn(Completable.complete());

        TestObserver testObserver = recipeRepository.deleteRecipe(recipe).test();

        testObserver.assertComplete();

        verify(localDataSource, times(1)).deleteRecipe(dataRecipe);
    }

    @Test
    public void testDeleteAllRecipes() {

        when(localDataSource.deleteAllRecipes())
                .thenReturn(Completable.complete());

        TestObserver testObserver = recipeRepository.deleteAllRecipes().test();

        testObserver.assertComplete();

        verify(localDataSource, times(1)).deleteAllRecipes();
    }

    @Test
    public void testGetLastSavedRecipe() {
        DataRecipe dataRecipe = Generator.generateDomainRecipe();
        Recipe recipe = mapper.mapTo(dataRecipe);

        when(localDataSource.getLastSavedRecipe())
                .thenReturn(Single.just(dataRecipe));

        TestObserver<Recipe> testObserver = recipeRepository.getLastSavedRecipe().test();

        testObserver.assertValue(recipe);

        verify(localDataSource, times(1)).getLastSavedRecipe();
    }

}

package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.Recipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.utils.TestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class GetRecipesTest {

    private GetRecipes getRecipes;
    @Mock
    private RecipeRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        getRecipes = new GetRecipes(repository, Schedulers.trampoline(), Schedulers
                .trampoline());
    }

    @Test
    public void testGetRecipesUseCaseResponse() {

        RecipeRequest recipeRequest = TestFactory.getRecipeRequest();

        Recipe recipe1 = TestFactory.getRecipe();
        Recipe recipe2 = TestFactory.getRecipe();
        Recipe recipe3 = TestFactory.getRecipe();

        when(repository.getRecipes(recipeRequest.getSearchQuery(), recipeRequest.getPageNum()))
                .thenReturn(Observable.just(recipe1, recipe2, recipe3));

        TestObserver<Recipe> observer = getRecipes.run(recipeRequest).test();

        observer.assertValues(recipe1, recipe2, recipe3);
    }

    @Test
    public void testGetRecipesUseCaseParams() {

        RecipeRequest recipeRequest = TestFactory.getRecipeRequest();

        Recipe recipe1 = TestFactory.getRecipe();
        Recipe recipe2 = TestFactory.getRecipe();
        Recipe recipe3 = TestFactory.getRecipe();

        when(repository.getRecipes(recipeRequest.getSearchQuery(), recipeRequest.getPageNum()))
                .thenReturn(Observable.just(recipe1, recipe2, recipe3));

        TestObserver<Recipe> observer = getRecipes.run(recipeRequest).test();

        verify(repository, times(1))
                .getRecipes(recipeRequest.getSearchQuery(), recipeRequest.getPageNum());
    }

}

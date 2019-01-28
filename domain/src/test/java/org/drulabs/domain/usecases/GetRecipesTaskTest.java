package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.entities.RecipeRequest;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.utils.TestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class GetRecipesTaskTest {

    private GetRecipesTask getRecipesTask;
    @Mock
    private RecipeRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        getRecipesTask = new GetRecipesTask(repository, Schedulers.trampoline(), Schedulers
                .trampoline());
    }

    @Test
    public void testGetRecipesUseCaseResponse() {

        RecipeRequest recipeRequest = TestFactory.getRecipeRequest();

        DomainRecipe domainRecipe1 = TestFactory.getRecipe();
        DomainRecipe domainRecipe2 = TestFactory.getRecipe();
        DomainRecipe domainRecipe3 = TestFactory.getRecipe();

        when(repository.getRecipes(recipeRequest.getSearchQuery(), recipeRequest.getPageNum()))
                .thenReturn(Observable.just(domainRecipe1, domainRecipe2, domainRecipe3));

        TestObserver<List<DomainRecipe>> observer = getRecipesTask.build(recipeRequest).test();

        observer.assertValue(domainRecipes ->
                domainRecipes.contains(domainRecipe1)
                        && domainRecipes.contains(domainRecipe2)
                        && domainRecipes.contains(domainRecipe3));
    }

    @Test
    public void testGetRecipesUseCaseParams() {

        RecipeRequest recipeRequest = TestFactory.getRecipeRequest();

        DomainRecipe domainRecipe1 = TestFactory.getRecipe();
        DomainRecipe domainRecipe2 = TestFactory.getRecipe();
        DomainRecipe domainRecipe3 = TestFactory.getRecipe();

        when(repository.getRecipes(recipeRequest.getSearchQuery(), recipeRequest.getPageNum()))
                .thenReturn(Observable.just(domainRecipe1, domainRecipe2, domainRecipe3));

        TestObserver<List<DomainRecipe>> observer = getRecipesTask.build(recipeRequest).test();

        verify(repository, times(1))
                .getRecipes(recipeRequest.getSearchQuery(), recipeRequest.getPageNum());
    }

}

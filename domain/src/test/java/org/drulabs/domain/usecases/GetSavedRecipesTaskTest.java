package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.DomainRecipe;
import org.drulabs.domain.repository.RecipeRepository;
import org.drulabs.domain.utils.TestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class GetSavedRecipesTaskTest {

    private GetSavedRecipesTask getSavedRecipesTask;
    @Mock
    private RecipeRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        getSavedRecipesTask = new GetSavedRecipesTask(repository, Schedulers.trampoline(), Schedulers
                .trampoline());
    }

    @Test
    public void testGetSavedRecipesUseCase() {

        DomainRecipe domainRecipe1 = TestFactory.getRecipe();
        DomainRecipe domainRecipe2 = TestFactory.getRecipe();
        DomainRecipe domainRecipe3 = TestFactory.getRecipe();

        List<DomainRecipe> domainRecipes = new ArrayList<>();
        domainRecipes.add(domainRecipe1);
        domainRecipes.add(domainRecipe2);
        domainRecipes.add(domainRecipe3);

        when(repository.getSavedRecipes()).thenReturn(Observable.just(domainRecipes));
        TestObserver<List<DomainRecipe>> observer = getSavedRecipesTask.build(null).test();

        observer.assertValue(domainRecipes);
    }

}

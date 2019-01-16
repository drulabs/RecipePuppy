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

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class GetLastSavedRecipesTestTask {

    private GetLastSavedRecipeTask getLastSavedRecipeTask;
    @Mock
    private RecipeRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        getLastSavedRecipeTask = new GetLastSavedRecipeTask(repository, Schedulers.trampoline(), Schedulers
                .trampoline());
    }

    @Test
    public void testGetSavedRecipesUseCase() {

        DomainRecipe domainRecipe1 = TestFactory.getRecipe();

        when(repository.getLastSavedRecipe()).thenReturn(Single.just(domainRecipe1));
        TestObserver<DomainRecipe> observer = getLastSavedRecipeTask.run(null).test();

        observer.assertValue(domainRecipe1);
    }

}

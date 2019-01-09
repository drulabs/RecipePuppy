package org.drulabs.domain.usecases;

import org.drulabs.domain.entities.Recipe;
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
public class GetLastSavedRecipesTest {

    private GetLastSavedRecipe getLastSavedRecipe;
    @Mock
    private RecipeRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        getLastSavedRecipe = new GetLastSavedRecipe(repository, Schedulers.trampoline(), Schedulers
                .trampoline());
    }

    @Test
    public void testGetSavedRecipesUseCase() {

        Recipe recipe1 = TestFactory.getRecipe();

        when(repository.getLastSavedRecipe()).thenReturn(Single.just(recipe1));
        TestObserver<Recipe> observer = getLastSavedRecipe.run(null).test();

        observer.assertValue(recipe1);
    }

}
